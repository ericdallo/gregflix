(ns gregflix.movie
  (:use korma.core
  		gregflix.db))

(declare movies related-movies)

(defentity movies
  (table :movie)
  (entity-fields :id :title :slug :description :url)
  (has-many related-movies))

(defentity related-movies
  (table :related_movie)
  (entity-fields :current_movie_id :related_movie_id)
  (belongs-to movies {:fk :related_movie_id}))

(defn find-all []
		(select movies))

(defn find-by [slug]
	(first 
		(select movies
			(where {:slug slug})
			(limit 1))))

(defn find-all-related-by [current-movie-id]
    (select related-movies
    	(with movies 
    		(fields :title :slug :url))
        (where {:current_movie_id current-movie-id})))