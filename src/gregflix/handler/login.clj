(ns gregflix.handler.login
	(:require [gregflix.login :as logins]
			  [gregflix.user :as users]))

(defn- get-client-ip [req]
  (if-let [ips (get-in req [:headers "x-forwarded-for"])]
    (-> ips (clojure.string/split #",") first)
    (:remote-addr req)))

(defn- get-user-id [req]
	(let [username (get (get req :params) :username)]
		(get (users/find-by-username username) :id)))

(defn- get-device [req]
	(if (.contains (get-in req [:headers "user-agent"]) "Mobile")
		"MOBILE"
		"DESKTOP"))

(defn audit [request]
	(logins/save (get-client-ip request) (get-user-id request) (get-device request)))