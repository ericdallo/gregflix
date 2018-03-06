(ns gregflix.handler.home
	(:require [gregflix.serie :as series]
			  [gregflix.movie :as movies]))

(defn home []
	{:series (series/find-all)
	 :movies (movies/find-all)})