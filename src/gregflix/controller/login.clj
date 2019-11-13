(ns gregflix.controller.login
  (:require [gregflix.db.login-audit :as db-login-audit]
            [gregflix.db.user :as db-user]
            [gregflix.db.config :as db-config]))

(defn- get-client-ip [req]
  (if-let [ips (get-in req [:headers "x-forwarded-for"])]
    (-> ips
        (clojure.string/split #",")
        first)
    (:remote-addr req)))

(defn- get-user-id
  [{{:keys [username]} :params
    {:keys [db]} :components}]
  (->> username
       (db-user/find-by-username db)
       :user/id))

(defn- get-device [req]
  (if (.contains (get-in req [:headers "user-agent"]) "Mobile")
    "MOBILE"
    "DESKTOP"))

(defn audit [request]
  (db-login-audit/save
   (db-config/datomic-conn)
   (get-client-ip request)
   (get-user-id request)
   (get-device request)))
