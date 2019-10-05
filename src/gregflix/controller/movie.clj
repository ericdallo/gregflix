(ns gregflix.controller.movie
  (:require [gregflix.db.movie :as movie]))

(defn get-all [slug]
  {:video (movie/find-by slug)
   :relateds (movie/find-all-related-by (:id (movie/find-by slug)))})
