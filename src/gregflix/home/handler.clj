(ns gregflix.home.handler
  (:require [cemerick.friend :as friend]
            [gregflix.movie.core :as movie]
            [gregflix.serie.core :as serie]))

(defn home []
  (let [current-user (get (friend/current-authentication) :user)
        series (serie/find-all-group-by-slug (get current-user :id))
        series-seasons (serie/find-all-seasons)
        series-episodes (serie/find-all-episodes)]
    {:series (shuffle series)
     :series-seasons series-seasons
     :series-episodes series-episodes
     :movies (reverse (sort-by (juxt :new :created_at)
                               (shuffle (movie/find-all))))}))
