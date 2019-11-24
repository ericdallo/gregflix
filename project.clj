(defproject gregflix "1.0.0"
  :description "Gregflix"
  :url "https://gregflix.site"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-devel "1.6.3"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [compojure "1.6.0"]
                 [com.cemerick/friend "0.2.3"]
                 [com.datomic/datomic-free "0.9.5697"]
                 [selmer "1.11.6"]
                 [clj-time "0.15.2"]
                 [org.clojure/data.csv "0.1.4"]
                 [midje "1.9.9"]]
  :plugins [[lein-ring "0.12.3"]
            [lein-sass "0.4.0"]
            [lein-midje "3.2.1"]
            [lein-exec "0.3.7"]]
  :ring {:handler gregflix.web/app}
  :sass {:src "resources/sass"
         :output-directory "resources/public/css"
         :style :compressed}
  :main gregflix.main)
