(ns gregflix.controller.movie
  (:require [gregflix.db.movie :as db-movie]))

(defn get-all [{{:keys [slug]} :params}]
  {:video (db-movie/find-by slug)
   :relateds (db-movie/find-all-related-by (:id (db-movie/find-by slug)))})
