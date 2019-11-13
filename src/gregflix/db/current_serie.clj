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
  (-> '{:find [(pull ?e [*]) .]
        :in [$ ?user-id ?slug]
        :where [[?e :current-serie/user ?user-id]
                [?e :current-serie/slug ?slug]]}
      (d/q db user-id slug)))

(defn upsert![conn user serie]
  (let [current-serie (find-current-serie (d/db conn) (:user/id user) (:serie/slug serie))
        id (or (:current-serie/id current-serie)
               (java.util.UUID/randomUUID))]
      (d/transact conn [(create id user serie)])))
