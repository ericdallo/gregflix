(ns gregflix.user.core
  (:use korma.core
  		gregflix.db))

(declare users)

(defentity users
  (table :user)
  (entity-fields :id :username :password))

(defn find-by-username [username]
	(first 
		(select users
			(where {:username username})
			(limit 1))))