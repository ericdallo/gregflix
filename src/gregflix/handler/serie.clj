(ns gregflix.handler.serie
	(:require [gregflix.serie :as series]
			  [gregflix.currentserie :as current-series]
			  [cemerick.friend :as friend]))

(defn show [slug season episode]
	(let [current-user (get (friend/current-authentication) :user)
		  previous-episode (series/find-by slug season (- episode 1))
		  next-episode (series/find-by slug season (+ episode 1))
		  serie (series/find-by slug season episode)]
		(current-series/save-current-episode current-user serie)
		{:video serie
	 	 :previous-episode (not (nil? previous-episode))
	 	 :next-episode (not (nil? next-episode))}))

(defn all []
	(let [current-user (get (friend/current-authentication) :user)
		series (series/find-all-group-by-slug (get current-user :id))
		series-seasons (series/find-all-seasons)
		series-episodes (series/find-all-episodes)]
		{:series (shuffle series),
		 :series-seasons series-seasons,
		 :series-episodes series-episodes}))