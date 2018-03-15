(ns gregflix.serie
  (:use korma.core
  		gregflix.db))

(declare series)

(defentity series
  (table :serie)
  (entity-fields :id :title :slug :description :preview :url :season :episode :episode_name))

(defn find-all-group-by-slug []
	(select series
		(group :slug)))

(defn find-all-seasons []
	(select series
		(group :season, :slug)
		(order :slug)
		(order :season)
		))

(defn find-all-episodes []
	(select series
		(group :season, :episode, :slug)
		(order :slug)
		(order :season)
		(order :episode)))

(defn find-by [slug season episode]
	(first 
		(select series
			(where {:slug slug, :season season, :episode episode})
			(limit 1))))