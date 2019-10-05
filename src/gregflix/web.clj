(ns gregflix.web
  (:require [cemerick.friend :as friend]
            [compojure.core :refer [ANY defroutes GET]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [gregflix.auth :as auth]
            [gregflix.controller.movie :as c-movie]
            [gregflix.controller.serie :as c-serie]
            [gregflix.controller.home :as c-home]
            [ring.util.response :as r-response]
            [selmer.parser :refer [render-file set-resource-path!]]))

(set-resource-path! (clojure.java.io/resource "templates"))

(defn- as-int [s]
  (Integer. (re-find  #"\d+" s )))

(defroutes app-routes
  (GET "/login" [:as req]
       (render-file "login.html" req))
  (GET "/" []
       (friend/authorize #{:gregflix.auth/user}
                         (render-file "home.html" (c-home/home))))
  (GET "/series/:slug/s/:season/e/:episode" [slug season :<< as-int episode :<< as-int]
       (friend/authorize #{:gregflix.auth/user}
                         (render-file "show-serie.html" (c-serie/get-all slug season episode))))
  (GET "/movies/:slug" [slug]
       (friend/authorize #{:gregflix.auth/user}
                         (render-file "show-movie.html" (c-movie/get-all slug))))

  (friend/logout (ANY "/logout" request (r-response/redirect "/")))
  (route/resources "/")
  (route/not-found (render-file "404.html" {})))

(def app
  (-> app-routes
      auth/authenticate
      handler/site))
