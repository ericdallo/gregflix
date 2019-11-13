(ns gregflix.db.movie
  (:require [datomic.api :as d]))

(defn find-all [db]
  (-> '{:find [[(pull ?movie [*]) ...]]
        :where [[?movie :movie/id ?id]]}
      (d/q db)))

(defn find-by [db slug]
  (-> '{:find [(pull ?movie [*]) .]
        :in [$ ?slug]
        :where [[?movie :movie/slug ?slug]]}
      (d/q db slug)))

(defn find-all-related-by [db current-movie]
  (-> '{:find [[(pull ?related [*]) ...]]
        :in [$ ?current-movie-id]
        :where [[?current-movie :movie/id ?current-movie-id]
                [?related :related-movie/current-movie ?current-movie]]}
      (d/q db (:movie/id current-movie))))
