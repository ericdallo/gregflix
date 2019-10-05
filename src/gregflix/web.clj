(ns gregflix.web
  (:require [cemerick.friend :as friend]
            [compojure.core :refer [ANY defroutes GET]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [gregflix.auth :as auth]
            [gregflix.home.handler :as home-handler]
            [gregflix.movie.handler :as movie-handler]
            [gregflix.serie.handler :as serie-handler]
            [selmer.parser :refer [render-file set-resource-path!]]
            [ring.util.response :as r-response]))

(set-resource-path! (clojure.java.io/resource "templates"))

(defn- as-int [s]
  (Integer. (re-find  #"\d+" s )))

(defroutes app-routes
  (GET "/login" [:as req]
       (render-file "login.html" req))
  (GET "/" []
       (friend/authorize #{:gregflix.auth/user} (render-file "home.html" (home-handler/home))))
  (GET "/series/:slug/s/:season/e/:episode" [slug season :<< as-int episode :<< as-int]
       (friend/authorize #{:gregflix.auth/user} (render-file "show-serie.html" (serie-handler/show slug season episode))))
  (GET "/movies/:slug" [slug]
       (friend/authorize #{:gregflix.auth/user} (render-file "show-movie.html" (movie-handler/show slug))))

  (friend/logout (ANY "/logout" request (r-response/redirect "/")))
  (route/resources "/")
  (route/not-found (render-file "404.html" {})))

(def app
  (-> app-routes
      (auth/authenticate)
      (handler/site)))
