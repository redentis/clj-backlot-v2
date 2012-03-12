(ns ooyala-backlot.assets
    (:require  [ooyala-backlot.base :as base]))

(def *service* "/v2/assets")

(defn create-asset 
  "Required properties in the body are: name, asset_type, file_name, file_size.
   \"asset_type\" should be one of [video, remote_asset, channel, live_stream, youtube]

   \"chunk_size\" is an optional parameter. If provided, it will allow you to upload the video in multiple chunks.
   Uploading a big file in separate chunks can make the upload process faster and more reliable. If the given chunk
   size is smaller than the file size, multiple URLs will be returned in the uploading_urls resource."
  [config body & [params]]
   (base/post config [*service*] (merge params {:body body})))
  
(defn update-asset
  "Update the properties of the asset with the given asset-id. The body parameter should speicify a map of properties and values to set.
   Example: (a/update-asset config 'abc123' {:name 'New video title'})"
  [config asset-id body & [params]]
    (base/patch config [*service* asset-id] (merge params {:body body})))

(defn delete-asset
  "Delete the asset with the given asset-id."
  [config asset-id & [params]]
    (base/delete config [*service* asset-id] params))

(defn get-asset
  "Returns a response containing the asset identified by the given asset-id."
  [config asset-id & [params]]
  (base/get config [*service* asset-id] params))

(defn get-asset-streams [config asset-id & [params]]
  (base/get config [*service* asset-id "streams"] params))

(defn get-upload-urls
  "Each URL corresponds to a byte range of the file. If you're not using chunked uploading, you can simply make a
   PUT request with your entire file to the single url given in this response. If you're using chunked uploading,
   upload the file in chunks where each chunk corresponds with one of the URLs given in this response."
  [config asset-id & [params]]
    (base/get config [*service* asset-id "uploader_urls"] params))

(defn set-upload-status
  "Status can be one of [uploaded, failed]. Once the status of the video has been set to \"uploaded\", the asset
   will begin processing. If the upload cannot be completed for some reason, you can abandon the upload by setting
   its status to \"failed\"."
  [config asset-id status & [params]]
    (let [body {:status (base/to-str status)}]
      (base/put config [*service* asset-id "uploader_status"] (merge {:body (base/to-json body)} params))))

(defn list-assets [config & [params]]
    (base/page-get config [*service*] params))

;; Asset metadata --------------------------------------------------

(defn get-asset-metadata [config asset-id & [params]]
    (base/get config [*service* asset-id "metadata"] params))

(defn set-asset-metadata [config asset-id body & [params]]
    (base/put config [*service* asset-id "metadata"] (merge params {:body body})))

(defn delete-asset-metadata [config asset-id & [params]]
    (base/delete config [*service* asset-id "metadata"] params))

(defn update-asset-metadata [config asset-id body & [params]]
    (base/patch config [*service* asset-id "metadata"] (merge params {:body body})))

;; Asset labels --------------------------------------------------

(defn get-asset-labels [config asset-id & [params]]
    (base/page-get config [*service* asset-id "labels"] params))

(defn set-asset-label [config asset-id label-id & [params]]
    (base/put config [*service* asset-id "labels" label-id] params))

(defn delete-asset-label [config asset-id label-id & [params]]
    (base/delete config [*service* asset-id "labels" label-id] params))

(defn set-asset-labels [config asset-id label-ids & [params]]
    (base/post config [*service* asset-id "labels"] (merge params {:body label-ids})))

;; Assigning player  --------------------------------------------------

(defn assign-player [config asset-id player-id & [params]]
    (base/put config [*service* asset-id "player" player-id] params))

; Assigning publishing rule  --------------------------------------------------

(defn assign-publishing-rule [config asset-id rule-id & [params]]
    (base/put config [*service* asset-id "publishing_rule" rule-id] params))

;; Assigning ad set  --------------------------------------------------

(defn assign-adset [config asset-id adset-id & [params]]
    (base/put config [*service* asset-id "ad_set" adset-id] params))

(defn unassign-adset [config asset-id adset-id & [params]]
    (base/delete config [*service* asset-id "ad_set" adset-id] params))

(defn get-asset-adset [config asset-id & [params]]
    (base/get config [*service* asset-id "ad_set"] params))

;; Query  --------------------------------------------------

(defn query
  "Return a paged list of assets matching the given query parameters. At a minimum you should be
   specifying a 'where' clause. Optional clauses are 'order-by' and 'limit'.

   Example: (a/query config {:where 'updated_after > 12345678' :order-by :create-date :limit 10}"
  [config params]
  (base/page-get config [*service*] params))