(ns gregflix.serie.current.core
  (:use korma.core
        gregflix.db))

(declare current-series)

(defentity current-series
  (table :current_serie)
  (entity-fields :user_id :serie_slug :season :episode))

(defn save [user-id serie-slug episode season]
    (insert current-series 
        (values {:user_id user-id
                 :serie_slug serie-slug
                 :episode episode
                 :season season})))

(defn update-to [user-id serie-slug episode season]
    (update current-series 
        (set-fields {:episode episode
                     :season season})
        (where {:user_id user-id
                :serie_slug serie-slug})))

(defn save-current-episode [user serie]
    (let [user-id (get user :id)
          serie-slug (get serie :slug)
          episode (get serie :episode)
          season (get serie :season)]
        (if 
            (nil? (first (select current-series
                (where {:user_id user-id,
                        :serie_slug serie-slug})
                        (limit 1))))
            (save user-id serie-slug episode season)
            (update-to user-id serie-slug episode season))))
