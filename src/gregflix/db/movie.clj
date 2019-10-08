(ns gregflix.db.movie
  (:require [datomic.api :as d]))

(defn find-all [db]
  (-> '[:find  (pull ?movie [*])
        :where [?movie :movie/id ?id]]
      (d/q db)
      flatten))

(defn find-by [db slug]
  (-> '[:find  (pull ?movie [*])
        :in $ ?slug
        :where [?movie :movie/slug ?slug]]
      (d/q db slug)
      ffirst))

(defn find-all-related-by [db current-movie]
  (-> '[:find  (pull ?related [*])
        :in $ ?current-movie-id
        :where [?related :related-movie/current-movie ?current-movie-id]]
      (d/q db (:db/id  current-movie))
      flatten))