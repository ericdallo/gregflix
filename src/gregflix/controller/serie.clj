(ns gregflix.controller.serie
  (:require [cemerick.friend :as friend]
            [gregflix.db.serie :as serie]
            [gregflix.serie.current.core :as current-serie]))

(defn get-all [slug season episode]
  (let [current-user (:user (friend/current-authentication))
        previous-episode (serie/find-by slug season (- episode 1))
        next-episode (serie/find-by slug season (+ episode 1))
        serie (serie/find-by slug season episode)]
    (current-serie/save-current-episode current-user serie)
    {:video serie
     :previous-episode (not (nil? previous-episode))
     :next-episode (not (nil? next-episode))}))
