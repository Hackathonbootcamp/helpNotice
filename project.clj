(defproject help_notice "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.1"]
                 [ring/ring-defaults "0.1.2"]
                 [twitter-api/twitter-api "0.7.7"]
                 [org.clojure/java.jdbc "0.3.6"]
                 [postgresql/postgresql "9.3-1102.jdbc4"]
                 [cheshire "4.0.2"]
                 [clj-time "0.9.0"]]
  :plugins [[lein-ring "0.8.13"]]
  :ring {:handler help-notice.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
