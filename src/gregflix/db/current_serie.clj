(ns gregflix.db.current-serie
  (:require [datomic.api :as d]))

(defn- create
  ([user serie]
   #:current-serie{:user       (:user/id user)
                    :slug       (:serie/slug serie)
                    :season     (:serie/season serie)
                    :episode    (:serie/episode serie)
                    :created-at (java.util.Date.)})
  ([id user serie]
   (-> (create user serie)
       (assoc :db/id id))))

(defn find-current-serie [db user-id slug]
  (-> '{:find [(pull ?e [:db/id :current-serie/slug :current-serie/season :current-serie/episode]) .]
        :in [$ ?user-id ?slug]
        :where [[?e :current-serie/user ?user-id]
                [?e :current-serie/slug ?slug]]}
      (d/q db user-id slug)))

(defn upsert! [conn user serie]
  (let [current-serie (find-current-serie (d/db conn) (:user/id user) (:serie/slug serie))]
    (if-let [id (:db/id current-serie)]
      (d/transact conn [(create id user serie)])
      (d/transact conn [(create user serie)]))))
