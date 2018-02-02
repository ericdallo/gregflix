(defproject gregflix "0.0.1"
  :description "Gregflix"
  :url "http://example.com/FIXME"
  :dependencies
      [[org.clojure/clojure "1.9.0"]
       [ring/ring-core "1.6.3"]
       [ring/ring-devel "1.6.3"]
       [ring/ring-jetty-adapter "1.6.3"]
       [compojure "1.6.0"]
       [selmer "1.11.6"]]
  :plugins 
      [[lein-ring "0.12.3"]
      [lein-sass "0.4.0"]]
  :ring {:handler gregflix.web/app}
  :sass {:src "resources/sass"
         :output-directory "resources/public/css"
         :style :compressed}
  :hooks [leiningen.sass]
  :main gregflix.web)