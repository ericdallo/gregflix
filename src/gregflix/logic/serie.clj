(ns gregflix.logic.serie
  (:require [clj-time.coerce :as tc]
            [clj-time.core :as t]))

(defn with-current-serie [serie current-serie]
  (-> serie
      (assoc :serie/watched? true)
      (assoc :serie/season (:current-serie/season current-serie))
      (assoc :serie/episode (:current-serie/episode current-serie))))

(defn with-new [serie date]
  (let [created-at    (tc/from-date (:serie/created-at serie))
        two-weeks-ago (t/minus date (t/days 14))
        new?          (t/after? created-at two-weeks-ago)]
    (assoc serie :movie/new? new?)))
