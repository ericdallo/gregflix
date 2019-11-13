(ns gregflix.db.current-serie
  (:require [datomic.api :as d]))

(defn- create
  [id user serie]
  #:current-serie{:id         id 
                  :user       (:user/id user)
                  :slug       (:serie/slug serie)
                  :season     (:serie/season serie)
                  :episode    (:serie/episode serie)
                  :created-at (java.util.Date.)})

(defn find-current-serie [db user-id slug]
  (-> '{:find [(pull ?cs [*]) .]
        :in [$ ?user-id ?slug]
        :where [[?user :user/id ?user-id]
                [?cs :current-serie/user ?user]
                [?cs :current-serie/slug ?slug]]}
      (d/q db user-id slug)))

(defn upsert![conn user serie]
  (if-let [current-serie (find-current-serie (d/db conn) (:user/id user) (:serie/slug serie))]
    (let [updated-serie (merge current-serie {:current-serie/season (:serie/season serie)
                                              :current-serie/episode (:serie/episode serie)})]
      (d/transact conn [updated-serie]))
    (d/transact conn [(create (java.util.UUID/randomUUID) user serie)])))
