(ns gregflix.login.handler
	(:require [gregflix.login.audit.core :as login-audit]
			  		[gregflix.user.core :as user]))

(defn- get-client-ip [req]
  (if-let [ips (get-in req [:headers "x-forwarded-for"])]
		(-> ips 
			(clojure.string/split #",") 
			first)
    (:remote-addr req)))

(defn- get-user-id [req]
	(let [username (:username (:params req))]
		(get (user/find-by-username username) :id)))

(defn- get-device [req]
	(if (.contains (get-in req [:headers "user-agent"]) "Mobile")
		"MOBILE"
		"DESKTOP"))

(defn audit [request]
	(login-audit/save 
		(get-client-ip request) 
		(get-user-id request) 
		(get-device request)))