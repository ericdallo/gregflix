(ns gregflix.controller.serie
  (:require [cemerick.friend :as friend]
            [gregflix.db.current-serie :as db-current-serie]
            [gregflix.db.serie :as db-serie]))

(defn get-all [slug season episode]
  (let [current-user (:user (friend/current-authentication))
        previous-episode (db-serie/find-by slug season (- episode 1))
        next-episode (db-serie/find-by slug season (+ episode 1))
        serie (db-serie/find-by slug season episode)]
    (db-current-serie/save-current-episode current-user serie)
    {:video serie
     :previous-episode (not (nil? previous-episode))
     :next-episode (not (nil? next-episode))}))
