(ns gregflix.db.movie
  (:require [korma.core :as k]
            [datomic.api :as d]))

(declare movies related-movies)

(k/defentity movies
  (k/table :movie)
  (k/entity-fields :id :title :slug :description :url)
  (k/has-many related-movies))

(k/defentity related-movies
  (k/table :related_movie)
  (k/entity-fields :current_movie_id :related_movie_id)
  (k/belongs-to movies {:fk :related_movie_id}))

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

(defn find-all-related-by [current-movie-id]
  (k/select related-movies
            (k/with movies
                    (k/fields :title :slug :url))
            (k/where {:current_movie_id current-movie-id})))
