(ns gregflix.db.login-audit
  (:require [datomic.api :as d]))

(defn create-audit [ip user-id device]
  [#:login-audit{:ip ip
                 :user [:user/id user-id]
                 :device device
                 :created-at (java.util.Date.)
                 :updated-at (java.util.Date.)}])

(defn save [conn ip user-id device]
  (d/transact conn
              (create-audit ip user-id device)))
