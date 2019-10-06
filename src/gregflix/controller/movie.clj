(ns gregflix.controller.movie
  (:require [gregflix.db.movie :as db-movie]
            [gregflix.misc :as misc]))

(defn get-all [{{:keys [slug]} :params
                {:keys [db]}   :components}]
  (let [movie (db-movie/find-by db slug)]
    {:video (misc/unnamespaced movie)
     :relateds (db-movie/find-all-related-by (:movie/id movie))}))
