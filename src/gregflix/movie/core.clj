(ns gregflix.movie.core
  (:require [korma.core :as k]))

(declare movies related-movies)

(k/defentity movies
  (k/table :movie)
  (k/entity-fields :id :title :slug :description :url)
  (k/has-many related-movies))

(k/defentity related-movies
  (k/table :related_movie)
  (k/entity-fields :current_movie_id :related_movie_id)
  (k/belongs-to movies {:fk :related_movie_id}))

(defn find-all []
  (k/exec-raw ["
      select m.id, m.title, m.slug, m.description, m.url, m.created_at,
      (m.created_at >= (NOW() - INTERVAL 14 DAY)) as new
      from movie m"] :results))

(defn find-by [slug]
  (first
   (k/select movies
             (k/where {:slug slug})
             (k/limit 1))))

(defn find-all-related-by [current-movie-id]
  (k/select related-movies
            (k/with movies
                    (k/fields :title :slug :url))
            (k/where {:current_movie_id current-movie-id})))
