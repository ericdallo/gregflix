(ns gregflix.web
	(:use compojure.core)
	(:use ring.middleware.reload)
	(:use selmer.parser)
	(:require [compojure.route :as route]
						[compojure.handler :as handler]
						[gregflix.auth :as auth]
						[cemerick.friend :as friend]))

(selmer.parser/set-resource-path! (clojure.java.io/resource "templates"))

(defroutes app-routes
	(GET "/login" [:as req]
		(render-file "login.html" req))
	(GET "/" []
		(friend/authorize #{:gregflix.auth/user} (render-file "home.html" {})))

	(friend/logout (ANY "/logout" request (ring.util.response/redirect "/")))
	(route/resources "/")
	(route/not-found "Not Found"))

(def app
	(handler/site
		(auth/authenticate app-routes)))