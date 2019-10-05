(ns gregflix.web
  (:require [cemerick.friend :as friend]
            [compojure.core :refer [ANY defroutes GET]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [gregflix.controller.home :as c-home]
            [gregflix.controller.movie :as c-movie]
            [gregflix.controller.serie :as c-serie]
            [gregflix.interceptor.auth :as int-auth]
            [gregflix.interceptor.component :as int-component]
            [ring.util.response :as r-response]
            [selmer.parser :refer [render-file set-resource-path!]]))

(set-resource-path! (clojure.java.io/resource "templates"))

(defroutes app-routes
  (GET "/login" []
       (render-file "login.html" {}))
  (GET "/" request
       (friend/authorize #{:gregflix.interceptor.auth/user}
                         (render-file "home.html"
                                      (c-home/all-movies-and-series request))))
  (GET "/series/:slug/s/:season/e/:episode" request
       (friend/authorize #{:gregflix.interceptor.auth/user}
                         (render-file "show-serie.html"
                                      (c-serie/get-all request))))
  (GET "/movies/:slug" request
       (friend/authorize #{:gregflix.interceptor.auth/user}
                         (render-file "show-movie.html"
                                      (c-movie/get-all request))))

  (friend/logout (ANY "/logout" request (r-response/redirect "/")))
  (route/resources "/")
  (route/not-found (render-file "404.html" {})))

(def app
  (-> app-routes
      int-component/add
      int-auth/authenticate
      handler/site))
