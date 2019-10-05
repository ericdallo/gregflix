(ns gregflix.controller.home
  (:require [gregflix.db.movie :as db-movie]
            [gregflix.db.serie :as db-serie]
            [gregflix.logic.movie :as l-movie]))

(defn all-movies-and-series [{{:keys [user]} :auth}]
  (let [current-user-id (:id user)
        series (db-serie/find-all-group-by-slug current-user-id)
        series-seasons (db-serie/find-all-seasons)
        series-episodes (db-serie/find-all-episodes)
        all-movies (db-movie/find-all)]
    {:series (shuffle series)
     :series-seasons series-seasons
     :series-episodes series-episodes
     :movies (l-movie/sorted-by-new all-movies)}))
