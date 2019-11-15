(ns gregflix.db.login-audit
  (:require [datomic.api :as d]))

(defn save [login-audit conn]
  (d/transact conn [login-audit]))
