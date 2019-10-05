(ns gregflix.db.user
  (:require [korma.core :as k])
  (:use gregflix.db))

(declare users)

(k/defentity users
  (k/table :user)
  (k/entity-fields :id :username :password))

(defn find-by-username [username]
  (first
   (k/select users
           (k/where {:username username})
           (k/limit 1))))
