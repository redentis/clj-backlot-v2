(ns ooyala-backlot.assets
    (:require  [ooyala-backlot.base :as base]))

(def ^:dynamic *service* "/v2/assets")

(defn create-asset 
  "Required properties in the body are: name, asset_type, file_name, file_size.
   \"asset_type\" should be one of [video, remote_asset, channel, live_stream, youtube]

   \"chunk_size\" is an optional parameter. If provided, it will allow you to upload the video in multiple chunks.
   Uploading a big file in separate chunks can make the upload process faster and more reliable. If the given chunk
   size is smaller than the file size, multiple URLs will be returned in the uploading_urls resource."
  [body & [params]]
   (base/post [*service*] (merge params {:body body})))
  
(defn update-asset
  "Update the properties of the asset with the given asset-id. The body parameter should speicify a map of properties and values to set.
   Example: (a/update-asset config 'abc123' {:name 'New video title'})"
  [asset-id body & [params]]
    (base/patch  [*service* asset-id] (merge params {:body body})))

(defn delete-asset
  "Delete the asset with the given asset-id."
  [asset-id & [params]]
    (base/delete [*service* asset-id] params))

(defn get-asset
  "Returns a response containing the asset identified by the given asset-id."
  [asset-id & [params]]
  (base/get [*service* asset-id] params))

(defn get-asset-streams
  "Return a list of streams associated with the given asset-id."
  [asset-id & [params]]
  (base/get [*service* asset-id "streams"] params))

(defn get-upload-urls
  "Each URL corresponds to a byte range of the file. If you're not using chunked uploading, you can simply make a
   PUT request with your entire file to the single url given in this response. If you're using chunked uploading,
   upload the file in chunks where each chunk corresponds with one of the URLs given in this response."
  [asset-id & [params]]
    (base/get [*service* asset-id "uploader_urls"] params))

(defn set-upload-status
  "Status can be one of [uploaded, failed]. Once the status of the video has been set to \"uploaded\", the asset
   will begin processing. If the upload cannot be completed for some reason, you can abandon the upload by setting
   its status to \"failed\"."
  [asset-id status & [params]]
    (let [body {:status (base/to-str status)}]
      (base/put [*service* asset-id "uploader_status"] (merge {:body (base/to-json body)} params))))

(defn list-assets [& [params]]
    (base/page-get [*service*] params))

;; Asset metadata --------------------------------------------------

(defn get-asset-metadata [asset-id & [params]]
    (base/get [*service* asset-id "metadata"] params))

(defn set-asset-metadata [asset-id body & [params]]
    (base/put [*service* asset-id "metadata"] (merge params {:body body})))

(defn delete-asset-metadata [asset-id & [params]]
    (base/delete [*service* asset-id "metadata"] params))

(defn update-asset-metadata [asset-id body & [params]]
    (base/patch [*service* asset-id "metadata"] (merge params {:body body})))

;; Asset labels --------------------------------------------------

(defn get-asset-labels [asset-id & [params]]
    (base/page-get [*service* asset-id "labels"] params))

(defn set-asset-label [asset-id label-id & [params]]
    (base/put [*service* asset-id "labels" label-id] params))

(defn delete-asset-label [asset-id label-id & [params]]
    (base/delete [*service* asset-id "labels" label-id] params))

(defn set-asset-labels [asset-id label-ids & [params]]
    (base/post [*service* asset-id "labels"] (merge params {:body label-ids})))

;; Assigning player  --------------------------------------------------

(defn assign-player [asset-id player-id & [params]]
    (base/put [*service* asset-id "player" player-id] params))

; Assigning publishing rule  --------------------------------------------------

(defn assign-publishing-rule [asset-id rule-id & [params]]
    (base/put [*service* asset-id "publishing_rule" rule-id] params))

;; Assigning ad set  --------------------------------------------------

(defn assign-adset [asset-id adset-id & [params]]
    (base/put [*service* asset-id "ad_set" adset-id] params))

(defn unassign-adset [asset-id & [params]]
    (base/delete [*service* asset-id "ad_set"] params))

(defn get-asset-adset [asset-id & [params]]
    (base/get [*service* asset-id "ad_set"] params))

;; Query  --------------------------------------------------

(defn query
  "Return a paged list of assets matching the given query parameters. At a minimum you should be
   specifying a 'where' clause. Optional clauses are 'order-by' and 'limit'.

   Example: (a/query config {:where 'updated_after > 12345678' :order-by :create-date :limit 10}"
  [params]
  (base/page-get [*service*] params))