(ns gregflix.handler.home
	(:require [gregflix.serie :as series]
			  [gregflix.movie :as movies]
			  [cemerick.friend :as friend]))

(defn home []
	(let [current-user (get (friend/current-authentication) :user)
		series (series/find-all-group-by-slug (get current-user :id))
		series-seasons (series/find-all-seasons)
		series-episodes (series/find-all-episodes)]
		{:series (shuffle series),
		 :series-seasons series-seasons,
		 :series-episodes series-episodes,
	 	 :movies (reverse (sort-by :new (shuffle (movies/find-all))))}))