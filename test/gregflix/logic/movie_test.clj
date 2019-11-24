(ns gregflix.logic.movie-test
  (:require [clj-time.coerce :as tc]
            [clj-time.core :as t]
            [gregflix.logic.movie :as l-movie]
            [midje.sweet :refer :all]))

(defn- to-date [year month day]
  (tc/to-date (t/date-time year month day)))

(def movies-new-last [#:movie{:id 1
                              :new? false
                              :created-at (to-date 2019 9 10)}
                      #:movie{:id 2
                              :new? true
                              :created-at (to-date 2019 10 3)}])

(def ordered-movies-new-last [#:movie{:id 2
                                      :new? true
                                      :created-at (to-date 2019 10 3)}
                              #:movie{:id 1
                                      :new? false
                                      :created-at (to-date 2019 9 10)}])

(def movies-created-last [#:movie{:id 1
                                  :new? true
                                  :created-at (to-date 2019 10 4)}
                          #:movie{:id 2
                                  :new? true
                                  :created-at (to-date 2019 10 5)}])
(def ordered-movies-created-last [#:movie{:id 2
                                          :new? true
                                          :created-at (to-date 2019 10 5)}
                                  #:movie{:id 1
                                          :new? true
                                          :created-at (to-date 2019 10 4)}])

(facts "sorting movies"
  (fact "when new movie is last"
    (l-movie/sorted-with-new (t/date-time 2019 10 6) movies-new-last) => ordered-movies-new-last)

  (fact "when boths are new but created-at is newer"
    (l-movie/sorted-with-new (t/date-time 2019 10 6) movies-created-last) => ordered-movies-created-last))
