(ns ooyala-backlot.players
    (:require  [ooyala-backlot.base :as base]))

(def *service* "/v2/players")

(defn list-players [config & [params]]
  (base/page-get config [*service*] params))
  
(defn get-player [config player-id & [params]]
  (base/get config [*service* player-id] params))

(defn create-player [config body & [params]]
  (base/post config [*service*] (merge params {:body body})))

(defn delete-player [config player-id & [params]]
  (base/delete config [*service* player-id] params))

(defn update-player [config player-id body & [params]]
  (base/patch config [*service* player-id] (merge params {:body body})))

;(defn set-scrubber-image [config player-id url & [params]]
;  (base/post config [*service* player-id "scrubber_image"] (merge params {:body body})))

(defn set-watermark-image [config player-id body & [params]]
  (base/put config [*service* player-id "watermark"] (merge params {:body body})))

; Player metadata  --------------------------------------------------

(defn get-player-metadata [config player-id & [params]]
  (base/get config [*service* player-id "metadata"] params))

(defn set-player-metadata [config player-id body & [params]]
  (base/put config [*service* player-id "metadata"] (merge params {:body body})))

(defn update-player-metadata [config player-id body & [params]]
  (base/patch config [*service* player-id "metadata"] (merge params {:body body})))

(defn delete-player-metadata [config player-id & [params]]
  (base/delete config [*service* player-id "metadata"] params))
