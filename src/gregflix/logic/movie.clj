(ns gregflix.logic.movie
  (:require [clj-time.core :as t]
            [clj-time.coerce :as tc]))

(defn- with-new [movie date]
  (let [created-at    (tc/from-date (:movie/created-at movie))
        two-weeks-ago (t/minus date (t/days 14))
        new?          (t/after? created-at two-weeks-ago)]
    (assoc movie :movie/new? new?)))

(defn sorted-with-new
  [date movies]
  (->> movies
       (map #(with-new % date))
       shuffle
       (sort-by (juxt :movie/new :movie/created-at))
       reverse))
