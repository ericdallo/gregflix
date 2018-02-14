(ns gregflix.main
	(:gen-class)
	(:use ring.adapter.jetty)
	(:require [gregflix.web :as web]))

(defn -main [& args]
	(run-jetty web/app {:port 8080}))