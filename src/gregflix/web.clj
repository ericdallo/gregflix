(ns gregflix.web
	(:gen-class)
    (:use compojure.core)
    (:use ring.adapter.jetty)
    (:use compojure.handler)
    (:use ring.middleware.reload)
    (:use selmer.parser)
	(:require [compojure.route :as route]))

(selmer.parser/set-resource-path! (clojure.java.io/resource "templates"))

(defroutes all-routes
	(GET "/login" []
		(render-file "login.html" {}))

	(route/resources "/")
	(route/not-found "Not Found"))

(defn -main [& args]
 	(run-jetty (site all-routes) {:port 8080}))