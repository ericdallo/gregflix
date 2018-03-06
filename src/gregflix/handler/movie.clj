(ns gregflix.handler.movie
	(:require [gregflix.movie :as movies]))

(defn show [slug]
	{:video (movies/find-by slug)})