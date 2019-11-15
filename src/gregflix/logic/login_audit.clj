(ns gregflix.logic.login-audit)

(defn- user-agent->device [user-agent]
  (if (.contains user-agent "Mobile")
    "MOBILE"
    "DESKTOP"))

(defn new-login-audit
  [user-id ip user-agent]
  #:login-audit{:id (java.util.UUID/randomUUID)
                :ip ip
                :user [:user/id user-id]
                :device (user-agent->device user-agent)
                :created-at (java.util.Date.)
                :updated-at (java.util.Date.)})
