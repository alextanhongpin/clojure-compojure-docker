(defproject hello-world "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-json "0.5.0"]
                 [seancorfield/next.jdbc "1.0.13"]
                 [org.postgresql/postgresql "42.1.3"]
                 [com.mchange/c3p0 "0.9.5.4"]
                 [buddy/buddy-auth "2.2.0"]
                 [buddy/buddy-sign "3.1.0"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler hello-world.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
