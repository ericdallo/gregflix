(ns gregflix.login
  (:use korma.core
  		gregflix.db)
  (:require [gregflix.user :as users]))

(declare logins users)

(defentity logins
  (table :login_audit)
  (entity-fields :id :ip :device)
  (belongs-to users))

(defn save [ip user-id device]
	(insert logins
		(values {:ip ip :user_id user-id :device device})))