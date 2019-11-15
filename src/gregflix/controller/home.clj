(ns gregflix.controller.home
  (:require [clj-time.core :as t]
            [gregflix.db.current-serie :as db-current-serie]
            [gregflix.db.movie :as db-movie]
            [gregflix.db.serie :as db-serie]
            [gregflix.logic.movie :as l-movie]
            [gregflix.misc :as misc]
            [gregflix.logic.serie :as l-serie]))

(defn- with-current-serie
  [serie
   {:user/keys [id]}
   db]
  (if-let [current-serie (db-current-serie/find-current-serie db id (:serie/slug serie))]
    (l-serie/with-current-serie serie current-serie)
    serie))

(defn- series-with-new-and-current-serie
  [series user datetime db]
  (->> series
       (map #(with-current-serie % user db))
       (map #(l-serie/with-new % datetime))
       (map misc/unnamespaced)
       shuffle))

(defn- movies-sorted-by-new
  [movies datetime]
  (->> movies
       (l-movie/sorted-with-new datetime)
       (map misc/unnamespaced)))

(defn all-movies-and-series [{{:keys [user]} :auth
                              {:keys [db]} :components}]
  (let [series (db-serie/find-all-group-by-slug db)
        series-seasons (db-serie/find-all-seasons db)
        series-episodes (db-serie/find-all-episodes db)
        all-movies (db-movie/find-all db)]
    {:series-seasons (map misc/unnamespaced series-seasons)
     :series-episodes (map misc/unnamespaced series-episodes)
     :series (series-with-new-and-current-serie series user (t/now) db)
     :movies (movies-sorted-by-new all-movies (t/now))}))
