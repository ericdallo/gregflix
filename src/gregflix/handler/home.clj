(ns gregflix.handler.home
	(:require [gregflix.serie :as series]
			  [gregflix.movie :as movies]))

(defn home []
	(let[series (series/find-all-group-by-slug)
		series-seasons (series/find-all-seasons)
		series-episodes (series/find-all-episodes)]
		{:series series,
		 :series-seasons series-seasons,
		 :series-episodes series-episodes,
	 	 :movies (movies/find-all)}))