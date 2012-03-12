(ns ooyala-backlot.ad-sets  
    (:require  [ooyala-backlot.base :as base]))

(def *service* "/v2/ad_sets")

(defn list-adsets [config & [params]]
    (base/get config [*service*] params))

(defn create-adset [config body & [params]]
   (base/post config [*service*] (merge params {:body body})))
  
(defn update-adset [config adset-id body & [params]]
    (base/patch config [*service* adset-id] (merge params {:body body})))

(defn delete-adset [config adset-id & [params]]
    (base/delete config [*service* adset-id] params))

(defn get-adset [config adset-id & [params]]
    (base/get config [*service* adset-id] params))

