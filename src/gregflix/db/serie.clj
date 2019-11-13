(ns gregflix.db.serie
  (:require [datomic.api :as d]))

(defn- find-all
  [db]
  (d/q '{:find [[(pull ?e [*]) ...]]
         :where [[?e :serie/slug]]}
       db))

(defn find-all-group-by-slug [db]
  (->> (find-all db)
       (group-by :serie/slug)
       vals
       (map first)))

(defn find-all-seasons [db]
  (->> (find-all db)
       (group-by #(select-keys % [:serie/slug :serie/season]))
       vals
       (map first)
       (sort-by :serie/season)))

(defn find-all-episodes [db]
  (->> (find-all db)
       (group-by #(select-keys % [:serie/slug
                                  :serie/season
                                  :serie/episode]))
       vals
       (map first)
       (sort-by :serie/episode)))

(defn find-by [db slug season episode]
  (-> '{:find  [(pull ?serie [*]) .]
        :in [$ ?slug ?season ?episode]
        :where [[?serie :serie/slug ?slug]
                [?serie :serie/season ?season]
                [?serie :serie/episode ?episode]]}
      (d/q db slug season episode)))
