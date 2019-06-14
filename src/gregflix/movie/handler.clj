(ns gregflix.movie.handler
	(:require [gregflix.movie.core :as movie]))

(defn show [slug]
	(let [movie (movie/find-by slug)]
		{:video movie
		 :relateds (movie/find-all-related-by (:id movie))}))