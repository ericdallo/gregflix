(ns gregflix.movie
  (:use korma.core
  		gregflix.db))

(declare movies)

(defentity movies
  (table :movie)
  (entity-fields :id :title :slug :description :preview :url))

(defn find-all []
		(select movies))

(defn find-by [slug]
	(first 
		(select movies
			(where {:slug slug})
			(limit 1))))