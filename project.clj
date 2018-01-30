(defproject gregflix "0.0.1"
  :description "Gregflix"
  :url "http://example.com/FIXME"
  :dependencies
      [[org.clojure/clojure "1.9.0"]
       [ring/ring-core "1.6.3"]
       [ring/ring-devel "1.6.3"]
       [ring/ring-jetty-adapter "1.6.3"]
       [ring-json-params "0.1.3"]
       [compojure "1.6.0"]
       [clj-json "0.3.2"]]
  :dev-dependencies
      [[lein-run "2.8.1"]]
  :plugins [[lein-ring "0.12.3"]]
  :ring {:handler gregflix.web/app}
  :main gregflix.web)
