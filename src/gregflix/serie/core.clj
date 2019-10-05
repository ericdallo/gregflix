(ns gregflix.serie.core
  (:require [korma.core :as k]))

(declare series)

(k/defentity series
  (k/table :serie)
  (k/entity-fields :id :title :slug :description :url :season :episode :episode_name))

(defn find-all-group-by-slug [user-id]
  (k/exec-raw ["select s.id, s.title, s.slug, s.description, s.url,
            if (cs.season is null, s.season, cs.season) as season,
            if (cs.episode is null, s.episode, cs.episode) as episode,
            (cs.id is not null) as watched,
            (s.created_at >= (NOW() - INTERVAL 14 DAY)) as new,
            s.episode_name
            from serie s
            left join current_serie cs on (s.slug = cs.serie_slug and cs.user_id = ?)
            group by slug;"
               [user-id]] :results))

(defn find-all-seasons []
  (k/select series
            (k/group :season, :slug)
            (k/order :slug)
            (k/order :season)))

(defn find-all-episodes []
  (k/select series
            (k/group :season, :episode, :slug)
            (k/order :slug)
            (k/order :season)
            (k/order :episode)))

(defn find-by [slug season episode]
  (first
   (k/select series
             (k/where {:slug slug, :season season, :episode episode})
             (k/limit 1))))
