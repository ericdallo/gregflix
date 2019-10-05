(ns gregflix.movie.handler
  (:require [gregflix.movie.core :as movie]))

(defn show [slug]
  {:video (movie/find-by slug)
   :relateds (movie/find-all-related-by (:id (movie/find-by slug)))})
