(ns gregflix.serie
  (:use korma.core
  		gregflix.db))

(declare series)

(defentity series
  (table :serie)
  (entity-fields :id :title :slug :description :preview :url :season :episode))

(defn find-all []
		(select series
			(group :slug)))

(defn find-by [slug season episode]
	(first 
		(select series
			(where {:slug slug, :season season, :episode episode})
			(limit 1))))