(ns gregflix.handler.serie
	(:require [gregflix.serie :as series]))

(defn show [slug season episode]
	(let [previous-episode (series/find-by slug season (- episode 1))
		  next-episode (series/find-by slug season (+ episode 1))]
		{:video (series/find-by slug season episode)
	 	 :previous-episode (not (nil? previous-episode))
	 	 :next-episode (not (nil? next-episode))}))

	