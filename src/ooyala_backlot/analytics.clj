(ns ooyala-backlot.analytics
    (:require  [ooyala-backlot.base :as base]))

(def ^:dynamic *service* "/v2/analytics/reports")

(defn account-performance
  ([[from to] filter]
     (base/page-get [*service* :account :performance filter (str from "..." to)]))
  ([[from to]]
     (base/page-get [*service* :account :performance :total (str from "..." to)])))

(defn asset-performance
  ([asset-id [from to] filter]
     (base/page-get [*service* :asset asset-id :performance filter (str from "..." to)]))
  ([asset-id [from to]]
     (base/page-get [*service* :asset asset-id :performance :total (str from "..." to)])))

(defn by-geo [param]
  (clojure.string/join "/"
                       (loop [ret []
                              aspects [:countries :regions :cities]]
                         (if (seq aspects)
                           (let [aspect (first aspects)
                                 value (aspect param)
                                 is-all (and (not (nil? value)) (= :all value))]
                             (if (nil? value)
                               (recur ret (next aspects))
                               (if is-all
                                 (conj ret (name aspect))
                                 (recur (conj ret (name aspect) value)
                                        (next aspects)))))
                           ret))))

(defn by [aspect & param]
  (case  aspect
        :domains   (str "domains/" (first param))
        :dmas      (str "dmas/" (first param))
        :platforms (str "platforms/" (first param))
        :devices   (str "devices/" (first param))
        :geo       (by-geo (first param))
        (keyword aspect)))










