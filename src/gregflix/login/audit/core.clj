(ns gregflix.login.audit.core
  (:use korma.core
  		gregflix.db)
  (:require [gregflix.user.core :as users]))

(declare logins users)

(defentity logins
  (table :login_audit)
  (entity-fields :id :ip :device)
  (belongs-to users))

(defn save [ip user-id device]
	(insert logins
		(values {:ip ip :user_id user-id :device device})))