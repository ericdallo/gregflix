(ns gregflix.controller.login
  (:require [gregflix.db.config :as db-config]
            [gregflix.db.login-audit :as db-login-audit]
            [gregflix.db.user :as db-user]
            [gregflix.logic.login-audit :as l-login-audit]))

(defn- get-user-id
  [{{:keys [username]} :params
    {:keys [db]} :components}]
  (->> username
       (db-user/find-by-username db)
       :user/id))

(defn- get-header [request header]
  (get-in request [:headers header]))

(defn- request->ip [request]
  (if-let [ips (get-header request "x-forwarded-for")]
    (-> ips
        (clojure.string/split #",")
        first)
    (:remote-addr request)))

(defn audit [request]
  (let [user-id (get-user-id request)
        user-agent (get-header request "user-agent")
        ip (request->ip request)
        login-audit (l-login-audit/new-login-audit user-id ip user-agent)]
    (db-login-audit/save login-audit (db-config/datomic-conn))))
