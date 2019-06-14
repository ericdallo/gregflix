(ns gregflix.home.handler
	(:require [gregflix.serie.core :as serie]
			  [gregflix.movie.core :as movie]
			  [cemerick.friend :as friend]))

(defn home []
	(let [current-user (:user (friend/current-authentication))
		series (serie/find-all-group-by-slug (:id current-user))
		series-seasons (serie/find-all-seasons)
		series-episodes (serie/find-all-episodes)]
		{:series (shuffle series),
		 :series-seasons series-seasons,
		 :series-episodes series-episodes,
	 	 :movies (reverse (sort-by (juxt :new :created_at) (shuffle (movie/find-all))))}))