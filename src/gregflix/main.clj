(ns gregflix.main
  (:gen-class)
  (:require [gregflix.web :as web]
            [ring.adapter.jetty :as ring]))

(defn -main [& args]
  (ring/run-jetty web/app {:port 8080}))
