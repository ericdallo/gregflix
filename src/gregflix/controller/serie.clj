(ns gregflix.controller.serie
  (:require [compojure.coercions :as comp-coercions]
            [gregflix.db.current-serie :as db-current-serie]
            [gregflix.db.serie :as db-serie]))

(defn get-all [{{:keys [slug season episode]} :params
                {:keys [user]} :auth}]
  (let [season-as-int (comp-coercions/as-int season)
        episode-as-int (comp-coercions/as-int episode)
        previous-episode (db-serie/find-by slug season-as-int (- episode-as-int 1))
        next-episode (db-serie/find-by slug season-as-int (+ episode-as-int 1))
        serie (db-serie/find-by slug season-as-int episode-as-int)]
    (db-current-serie/save-current-episode user serie)
    {:video serie
     :previous-episode (boolean previous-episode)
     :next-episode (boolean next-episode)}))
