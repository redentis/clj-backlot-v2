
(ns ooyala-backlot.analytics
    (:require  [ooyala-backlot.base :as base]))

(def ^:dynamic *service* "/v2/analytics/reports")

(defn account-performance
  ([config [from to] filter]
     (base/page-get config [*service* :account :performance filter (str from "..." to)]))
  ([config [from to]]
     (base/page-get config [*service* :account :performance :total (str from "..." to)])))

(defn asset-performance
  ([config asset-id [from to] filter]
     (base/page-get config [*service* :asset asset-id :performance filter (str from "..." to)]))
  ([config asset-id [from to]]
     (base/page-get config [*service* :asset asset-id :performance :total (str from "..." to)])))

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










