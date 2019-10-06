(ns gregflix.logic.movie-test
  (:require [gregflix.logic.movie :as l-movie]
            [midje.sweet :refer :all]))

(def movies-new-last [{:id 1
                       :new false
                       :created_at "2019-01-01"}
                      {:id 2
                       :new true
                       :created_at "2019-02-01"}])

(def ordered-movies-new-last [{:id 2
                               :new true
                               :created_at "2019-02-01"}
                              {:id 1
                               :new false
                               :created_at "2019-01-01"}])

(def movies-created-last [{:id 1
                           :new true
                           :created_at "2019-01-01"}
                          {:id 2
                           :new true
                           :created_at "2019-02-01"}])
(def ordered-movies-created-last [{:id 2
                                   :new true
                                   :created_at "2019-02-01"}
                                  {:id 1
                                   :new true
                                   :created_at "2019-01-01"}])

(facts "sorting movies"
  (fact "when new movie is last"
    (l-movie/sorted-by-new movies-new-last) => ordered-movies-new-last)

  (fact "when boths are new but created-at is newer"
    (l-movie/sorted-by-new movies-created-last) => ordered-movies-created-last))
