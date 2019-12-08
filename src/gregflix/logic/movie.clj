(ns gregflix.logic.movie
  (:require [clj-time.core :as t]
            [clj-time.coerce :as tc]))

(defn- with-new [movie date]
  (let [created-at    (tc/from-date (:movie/created-at movie))
        one-month-ago (t/minus date (t/months 1))
        new?          (t/after? created-at one-month-ago)]
    (assoc movie :movie/new? new?)))

(defn sorted-with-new
  [date movies]
  (->> movies
       (map #(with-new % date))
       shuffle
       (sort-by (juxt :movie/new :movie/created-at))
       reverse))
