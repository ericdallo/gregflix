(ns gregflix.handler.home
	(:require [gregflix.serie :as series]
			  [gregflix.movie :as movies]))

(defn home []
	(let[series (series/find-all-group-by-slug)
		series-episodes (series/find-all-episodes)]
		{:series series,
		 :series-episodes series-episodes,
	 	 :movies (movies/find-all)}))