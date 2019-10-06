(ns gregflix.misc
  (:require [clojure.walk :as walk]))

(defn unnamespaced
  "Remove namespace from m hash-map keys.
  Example:
    (unnamespaced :foo{:a 1 :b 2}) => {:a 1 :b 2}
  "
  [m]
  (walk/postwalk
   (fn [x] (if (keyword? x)
             (keyword (name x))
             x)) m))
