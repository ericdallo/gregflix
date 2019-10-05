(ns gregflix.db.login-audit
  (:require [korma.core :as k]))

(declare logins users)

(k/defentity logins
  (k/table :login_audit)
  (k/entity-fields :id :ip :device)
  (k/belongs-to users))

(defn save [ip user-id device]
  (k/insert logins
            (k/values {:ip ip :user_id user-id :device device})))
