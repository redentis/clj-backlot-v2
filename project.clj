(defproject ooyala-backlot-v2/ooyala-backlot-v2 "2.0.1-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.5.0"]
                 [org.apache.httpcomponents/httpclient "4.0.3"]
                 [commons-codec "1.4"]
                 [commons-io "1.4"]
                 [cheshire "5.1.0"]
                 [biz.source_code/base64coder "2010-12-19"]]
  :aot [org.apache.http.client.methods.HttpPatch]
  :min-lein-version "2.0.0"
  :jvm-opts ["-Dslime.encoding=UTF-8"]
    :description "API wrapper around the Ooyala Backlot V2 APIs")
