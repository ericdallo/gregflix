(ns gregflix.serie
  (:use korma.core
  		gregflix.db))

(declare series)

(defentity series
  (table :serie)
  (entity-fields :id :title :slug :description :url :season :episode :episode_name))

(defn find-all-group-by-slug [user-id]
	(exec-raw ["
		select s.id, s.title, s.slug, s.description, s.url,
  			if (cs.season is null, s.season, cs.season) as season,
  			if (cs.episode is null, s.episode, cs.episode) as episode,
  			s.episode_name
  			from serie s 
  			left join current_serie cs on (s.slug = cs.serie_slug and cs.user_id = ?)
  			group by slug;"
  			[user-id]] :results))

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