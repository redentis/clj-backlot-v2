(ns ooyala-backlot.syndications
    "{
  \"id\": \"1acfea12db3405\",
  \"name\": \"an internal name for this syndication\",
  // If true, all content is included in this syndication
  \"include_all_content\": false,
  // If \"include_all_content\" is false, only content matching these labels will be included
// in this syndication. \"include_labels\" is a list of label IDs
  \"include_labels\": [\"label_id_1\"],
  // Filter what assets are included in this syndication. See \"Stream Filtering\" for details.
// Valid device values: [\"iphone\", \"ipad\", \"flash_enabled\"]
// Valid container values: [\"mp4\", \"flv\", \"3gp\", \"ts\", \"ismv\", \"aac\"]
  \"include_encodings\": [
    { \"device\": \"iphone\" },
    { \"container\": \"mp4\" }
  ]
  // The types of assets to include in this syndication. Valid values are:
// [video, ad, remote_asset, live_stream]
  \"asset_types\": [\"video\"],
  // Valid values are [advanced, boxee, google, iphone, ipad, mp4, itunes, roku, source, youtube]
  \"type\": \"iphone\"

  //
// These fields are only present for advanced, boxee, iphone, ipad, mp4, roku, and
// source syndications:
//
  // The title of this feed. This is included inside of the <channel> tag in the feed.
  \"title\": \"Latest racing videos\",
  \"description\": \"Amateur Tokyo drift!\",
  // The URL of this syndication's MRSS feed.
  \"syndication_url\": \"http://api.ooyala.com/v2/syndications/1acfea12db3405/feed?pcode=3k2jkla9\"
  // The URL to be included inside of the <channel> tag in the feed.
  \"destination_url\": \"http://mywebsite.com\",
  // If true, the feed's URL must be accessed using an access_key parameter.
// See Access Key Restrictions for details.
  \"require_access_key\": false,

  //
// These fields are only present for YouTube syndications:
//
  \"username\": \"my_youtube_login\",
  \"password\": \"my_youtube_password\",
  \"should_create_youtube_videos\": true,
  \"should_delete_youtube_videos\": true

  //
// These fields are only present for iTunes syndications:
//
  \"title\": \"Title of the podcast\",
  \"description\": \"Description of the podcast\",
  // A comma-separated list of keywords for iTunes
  \"keywords\": \"80s, rock\",
  \"author\": \"Rick Astley\",
  \"category\": \"Music\"
}"
    (:require  [ooyala-backlot.base :as base]))

; Syndications --------------------------------------------------

(def *service* "/v2/syndications")

(defn list-syndications [config & [params]]
    (base/page-get config [*service*] params))

(defn get-syndication [config syndication-id & [params]]
    (base/get config [*service* syndication-id] params))

(defn create-syndication [config body & [params]]
   (base/post config [*service*] (merge params {:body body})))
  
(defn update-syndication [config syndication-id body & [params]]
    (base/patch config [*service* syndication-id] (merge params {:body body})))

(defn delete-syndication [config syndication-id & [params]]
    (base/delete config [*service* syndication-id] params))

; Syndication keys --------------------------------------------------

(defn enable-syndication-keys [config syndication-id & [params]]
   (base/patch config [*service* syndication-id] (merge params {:body "{\"require_access_keys\" : true}"})))

(defn list-syndication-keyss [config syndication-id & [params]]
    (base/page-get config [*service* syndication-id "access_keys"] params))

(defn create-syndication-key [config syndication-id body & [params]]
   (base/post config [*service* syndication-id "access_keys"] (merge params {:body body})))

(defn delete-syndication-key [config syndication-id access-key & [params]]
    (base/delete config [*service* syndication-id "access_keys" access-key] params))