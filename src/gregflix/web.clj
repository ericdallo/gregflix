(ns gregflix.web
	(:use compojure.core)
	(:use ring.middleware.reload)
	(:use selmer.parser)
	(:require [compojure.route :as route]
						[compojure.handler :as handler]
						[gregflix.auth :as auth]
						[cemerick.friend :as friend]
						[gregflix.handler.home :as home-handler]
						[gregflix.handler.serie :as serie-handler]
						[gregflix.handler.movie :as movie-handler]))

(selmer.parser/set-resource-path! (clojure.java.io/resource "templates"))

(defn as-int [s]
   (Integer. (re-find  #"\d+" s )))

(defroutes app-routes
	(GET "/login" [:as req]
		(render-file "login.html" req))
	(GET "/" []
		(friend/authorize #{:gregflix.auth/user} (render-file "home.html" (home-handler/home))))
	(GET "/series" []
		(friend/authorize #{:gregflix.auth/user} (render-file "series.html" (serie-handler/all))))
	(GET "/series/:slug/s/:season/e/:episode" [slug season :<< as-int episode :<< as-int]
		(friend/authorize #{:gregflix.auth/user} (render-file "show-serie.html" (serie-handler/show slug season episode))))
	(GET "/movies/:slug" [slug]
		(friend/authorize #{:gregflix.auth/user} (render-file "show-movie.html" (movie-handler/show slug))))

	(friend/logout (ANY "/logout" request (ring.util.response/redirect "/")))
	(route/resources "/")
	(route/not-found (render-file "404.html" {})))

(def app
	(-> app-routes
		(auth/authenticate)
		(handler/site)
		))