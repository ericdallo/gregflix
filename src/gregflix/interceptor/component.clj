(ns gregflix.interceptor.component
  (:require [cemerick.friend :as friend]
            [gregflix.db.config :as db-config]))

(defn- auth
  [request]
  (update-in request [:auth] merge
             (or (friend/current-authentication)
                 {})))

(defn- db
  [request]
  (assoc-in request [:components :db]
            (db-config/datomic-db)))

(defn add-auth [handler]
  (fn [request]
    (-> request
        auth
        handler)))

(defn add-db [handler]
  (fn [request]
    (-> request
        db
        handler)))
