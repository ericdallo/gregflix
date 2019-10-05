(ns gregflix.serie.handler
  (:require [cemerick.friend :as friend]
            [gregflix.serie.core :as serie]
            [gregflix.serie.current.core :as current-serie]))

(defn show [slug season episode]
	(let [current-user (:user (friend/current-authentication))
				previous-episode (serie/find-by slug season (- episode 1))
				next-episode (serie/find-by slug season (+ episode 1))
				serie (serie/find-by slug season episode)]
		(current-serie/save-current-episode current-user serie)
		{:video serie
	 	 :previous-episode (not (nil? previous-episode))
	 	 :next-episode (not (nil? next-episode))}))
