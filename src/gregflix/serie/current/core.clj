(ns gregflix.serie.current.core
  (:require [korma.core :as k]))

(declare current-series)

(k/defentity current-series
  (k/table :current_serie)
  (k/entity-fields :user_id :serie_slug :season :episode))

(defn save [user-id serie-slug episode season]
  (k/insert current-series
            (k/values {:user_id user-id
                       :serie_slug serie-slug
                       :episode episode
                       :season season})))

(defn update-to [user-id serie-slug episode season]
  (k/update current-series
            (k/set-fields {:episode episode
                           :season season})
            (k/where {:user_id user-id
                      :serie_slug serie-slug})))

(defn save-current-episode [user serie]
  (let [user-id (:id user)
        serie-slug (:slug serie)
        episode (:episode serie)
        season (:season serie)]
    (if
        (nil? (first (k/select current-series
                               (k/where {:user_id user-id,
                                         :serie_slug serie-slug})
                               (k/limit 1))))
      (save user-id serie-slug episode season)
      (update-to user-id serie-slug episode season))))
