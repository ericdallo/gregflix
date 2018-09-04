(ns gregflix.handler.movie
	(:require [gregflix.movie :as movies]))

(defn show [slug]
	(let [movie (movies/find-by slug)]
		{:video movie
		 :relateds (movies/find-all-related-by (get movie :id))}))