(ns gregflix.db.login-audit
  (:require [datomic.api :as d]))

(defn- create [id ip user-id device]
  #:login-audit{:id id
                :ip ip
                :user [:user/id user-id]
                :device device
                :created-at (java.util.Date.)
                :updated-at (java.util.Date.)})

(defn save [conn ip user-id device]
  (let [audit (-> (java.util.UUID/randomUUID)
                  (create ip user-id device))]
    (d/transact conn [audit])))
