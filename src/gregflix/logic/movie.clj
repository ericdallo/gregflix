(ns gregflix.logic.movie)

(defn sorted-by-new
  [movies]
  (->> movies
       shuffle
       (sort-by (juxt :new :created_at))
       reverse))
