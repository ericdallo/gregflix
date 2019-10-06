(ns gregflix.controller.home
  (:require [gregflix.db.movie :as db-movie]
            [gregflix.db.serie :as db-serie]
            [gregflix.logic.movie :as l-movie]
            [clj-time.core :as t]
            [gregflix.misc :as misc]))

(defn all-movies-and-series [{{:keys [user]} :auth
                              {:keys [db]} :components}]
  (let [current-user-id (:user/id user)
        series (db-serie/find-all-group-by-slug current-user-id)
        series-seasons (db-serie/find-all-seasons)
        series-episodes (db-serie/find-all-episodes)
        all-movies (->> (db-movie/find-all db)
                        (l-movie/sorted-with-new (t/now))
                        (map misc/unnamespaced))]
    {:series (shuffle series)
     :series-seasons series-seasons
     :series-episodes series-episodes
     :movies all-movies}))
