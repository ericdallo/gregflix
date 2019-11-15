(ns gregflix.controller.movie
  (:require [gregflix.db.movie :as db-movie]
            [gregflix.misc :as misc]))

(defn get-movie [{{:keys [slug]} :params
                {:keys [db]}   :components}]
  (let [movie (db-movie/find-by db slug)
        related-movies (->> (db-movie/find-all-related-by db movie)
                            (map misc/unnamespaced))]
    {:video (misc/unnamespaced movie)
     :relateds related-movies}))
