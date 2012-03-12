(ns ooyala-backlot.base
    (:use     [clojure.string :only (join)])
    (:require [cheshire.core :as json]
              [clj-http.client :as http])
    (:import (biz.source_code.base64Coder Base64Coder)
             (java.security MessageDigest)
             (java.net.URLEncoder))
    (:refer-clojure :exclude (get)))

(defn update [m k f & args]
  (assoc m k (apply f (m k) args)))

(defn to-json [o] (json/generate-string o))

(defn parse-json [s] (json/parse-string s :t))

;(defprotocol Config
;  (api-key [this])
;  (secret-key [this])
;  (default-expiry [this])
;  (api-base [this]))

(def default-api-config {
    :scheme "https"
    :host "api.ooyala.com"
    :default-expiry (* 60 60 24)})
                         
(defn base64-encode
  "Encodes the given sequence of bytes into a Base64 String."
  [bytes]    
  (String. (Base64Coder/encode bytes)))
  
(defn sha256-digest
  "Generate the SHA256 digest of the given string and return as an array of  bytes."
  [^String v]
  (-> (MessageDigest/getInstance "SHA-256") (.digest (.getBytes v))))

(defn to-str
  "Returns the string form of the given parameter. This method returns the 'name' of keywords
   and symbols."
  [v]
  (if (or (keyword? v) (symbol? v)) (name v) (str v)))

(defn- flatten-map
  "Converts a given map to a list (seq) of strings representing the key-value pair. Each pair
   uses either '=' or the given separator in the string. For example 'key=value'."
  ([m] (flatten-map \= m))
  ([key-sep m]
    (map (fn [[k v]] (str (to-str k) key-sep (to-str v))) m)))

(defn- gen-sig-base
  "Assembles the basic string value that the API signature mechanism requires from the given parts.
   Arguments are:
   - config: a configuration object (see Config protocol);
   - method: a keyword representing the HTTP operation;
   - path:   a vector of path elements;
   - params: a map of API parameters include :body if the API method requires this."
  [config method path params body]
      (str (:secret-key config)
           (.toUpperCase (to-str method))
           (join \/ path)
           (join (flatten-map params))
           body))

(defn generate-sig 
  "Calculates the signature component of the API call as per the documentation ... This
   function does *not* URL encode the signature."
  ([config method path params body]
     (generate-sig (gen-sig-base config method path params body)))
  ([sig-base]
     (apply str
            (filter #(not (= \= %))
                    (take 43                   
                          (base64-encode (sha256-digest sig-base)))))))
  
(defn- required-api-params
  "Returns a map of the required API parameters based on the given config."
  [config] 
  (sorted-map
      :api_key (:api-key config)
      :expires  (+ (quot (System/currentTimeMillis) 1000) (:default-expiry config))))
    
(defn- encode-body 
  "Examines the given map for the ':body' key. If found, replaces the contents with a
   JSON encoded representation as this is what the API requires."
  [params]
      (if-let [body (:body params)]
        (json/generate-string body)
        nil))

(defn- default-request
  "Returns a map of the default HTTP client request headers and keywords."
  []
  {:throw-exceptions false :content-type "text/json" :log-url true})

(defn wrap-api-request [client]
  (fn [req]
    (let [resp (client req)]
      (case (get-in resp [:headers "content-type"])
        "application/json"
        (update resp :body parse-json)
        resp))))

(defn wrap-request [request]
  (-> request
      wrap-api-request))

(def request (wrap-request #'http/request))

(defn- api-call [method]
  (fn [config path & [params]]
    (let [safe-path (map name path)
          all-params (merge default-api-config config)
          all-api-params (merge (required-api-params all-params) (dissoc params :body))
          body (encode-body params)
          sig (generate-sig config method safe-path all-api-params body)
          {:keys [scheme host port]} all-params]
      (request (merge (default-request)
                      {:method method
                       :scheme scheme
                       :server-name host
                       :server-port port
                       :uri (join \/ safe-path)
                       :query-params (assoc all-api-params :signature sig)
                       :body body})))))


(def get (api-call :get))

(defn page-get [config path & [params]]
  (let [data      (get config path params)
        next-page (:next_page data)
        {:keys [scheme host port]} config]
        (lazy-seq (cons data
                        (if next-page (http/request (merge (default-request)
                                         {:method :get
                                          :scheme scheme
                                          :server-name host
                                          :server-port port
                                          :uri next-page}))
                                      ())))))

(def head (api-call :head))

(def post (api-call :post))
  
(def put (api-call :put))

(def delete (api-call :delete))
  
(def patch (api-call :patch))
