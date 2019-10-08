(ns gregflix.controller.serie
  (:require [compojure.coercions :as comp-coercions]
            [gregflix.db.current-serie :as db-current-serie]
            [gregflix.db.serie :as db-serie]
            [gregflix.misc :as misc]
            [gregflix.db.config :as db-config]))

(defn get-serie [{{:keys [slug season episode]} :params
                {:keys [user]} :auth
                {:keys [db]} :components}]
  (let [season-as-int (comp-coercions/as-int season)
        episode-as-int (comp-coercions/as-int episode)
        previous-episode (db-serie/find-by db slug season-as-int (- episode-as-int 1))
        next-episode (db-serie/find-by db slug season-as-int (+ episode-as-int 1))
        serie (db-serie/find-by db slug season-as-int episode-as-int)]
    (db-current-serie/upsert! (db-config/datomic-conn) user serie)
    {:video (misc/unnamespaced serie)
     :previous-episode (boolean previous-episode)
     :next-episode (boolean next-episode)}))
