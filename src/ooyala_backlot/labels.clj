(ns ooyala-backlot.labels
    (:require  [ooyala-backlot.base :as base]))

(def *service* "/v2/labels")

(defn list-labels [config & [params]]
  (base/page-get config [*service*] params))
  
(defn get-label [config label-id & [params]]
  (base/get config [*service* label-id] params))

(defn list-label-assets [config label-id & [params]]
  (base/page-get config [*service* label-id "assets"] params))

(defn list-sublabels [config label-id & [params]]
  (base/page-get config [*service* label-id "children"] params))

(defn create-label [config body & [params]]
  (base/post config [*service*] (merge params {:body body})))

(defn delete-label [config label-id & [params]]
  (base/delete config [*service* label-id] params))

(defn update-label [config label-id body & [params]]
  (base/patch config [*service* label-id] (merge params {:body body})))
