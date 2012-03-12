(ns ooyala-backlot.rules
    (:require  [ooyala-backlot.base :as base]))

(def *service* "/v2/publishing_rules")

(defn list-rules [config & [params]]
    (base/page-get config [*service*] params))

(defn create-rule [config body & [params]]
   (base/post config [*service*] (merge params {:body body})))
  
(defn update-rule [config rule-id body & [params]]
    (base/patch config [*service* rule-id] (merge params {:body body})))

(defn delete-rule [config rule-id & [params]]
    (base/delete config [*service* rule-id] params))

(defn get-rule [config rule-id & [params]]
    (base/get config [*service* rule-id] params))

