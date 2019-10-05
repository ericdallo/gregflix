(ns gregflix.controller.login
  (:require [gregflix.db.login-audit :as db-login-audit]
            [gregflix.db.user :as db-user]))

(defn- get-client-ip [req]
  (if-let [ips (get-in req [:headers "x-forwarded-for"])]
    (-> ips
        (clojure.string/split #",")
        first)
    (:remote-addr req)))

(defn- get-user-id [req]
  (let [username (get (get req :params) :username)]
    (get (db-user/find-by-username username) :id)))

(defn- get-device [req]
  (if (.contains (get-in req [:headers "user-agent"]) "Mobile")
    "MOBILE"
    "DESKTOP"))

(defn audit [request]
  (db-login-audit/save
   (get-client-ip request)
   (get-user-id request)
   (get-device request)))
