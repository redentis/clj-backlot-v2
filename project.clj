(defproject ooyala-backlot-v2 "1.0.0-SNAPSHOT"
  :description "API wrapper around the Ooyala Backlot V2 APIs"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [org.apache.httpcomponents/httpclient "4.0.3"]
                 [commons-codec "1.4"]
                 [commons-io "1.4"]
                 [cheshire            "1.1.4"]
                 [biz.source_code/base64coder "2010-12-19"]]
  :aot [org.apache.http.client.methods.HttpPatch]
  :jvm-opts ["-Dslime.encoding=UTF-8"]
)
;  :jvm-opts ["-Xdebug" "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8888"]
