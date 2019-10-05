(ns gregflix.controller.home
  (:require [cemerick.friend :as friend]
            [gregflix.db.movie :as db-movie]
            [gregflix.db.serie :as db-serie]))

(defn home []
  (let [current-user (:user (friend/current-authentication))
        series (db-serie/find-all-group-by-slug (:id current-user))
        series-seasons (db-serie/find-all-seasons)
        series-episodes (db-serie/find-all-episodes)]
    {:series (shuffle series)
     :series-seasons series-seasons
     :series-episodes series-episodes
     :movies (reverse (sort-by (juxt :new :created_at)
                               (shuffle (db-movie/find-all))))}))
