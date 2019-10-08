(ns gregflix.controller.home
  (:require [clj-time.core :as t]
            [gregflix.db.current-serie :as db-current-serie]
            [gregflix.db.movie :as db-movie]
            [gregflix.db.serie :as db-serie]
            [gregflix.logic.movie :as l-movie]
            [gregflix.misc :as misc]
            [gregflix.logic.serie :as l-serie]))

(defn- with-current-serie [serie db user-id]
  (if-let [current-serie (db-current-serie/find-current-serie db user-id (:serie/slug serie))]
    (l-serie/with-current-serie serie current-serie)
    serie))

(defn all-movies-and-series [{{:keys [user]} :auth
                              {:keys [db]} :components}]
  (let [current-user-id (:user/id user)
        series (db-serie/find-all-group-by-slug db)
        series-seasons (db-serie/find-all-seasons db)
        series-episodes (db-serie/find-all-episodes db)
        all-movies (db-movie/find-all db)]
    {:series-seasons (map misc/unnamespaced series-seasons)
     :series-episodes (map misc/unnamespaced series-episodes)
     :series (->> series
                  (map #(with-current-serie % db current-user-id))
                  (map #(l-serie/with-new % (t/now)))
                  (map misc/unnamespaced)
                  shuffle)
     :movies (->> all-movies
                  (l-movie/sorted-with-new (t/now))
                  (map misc/unnamespaced))}))
