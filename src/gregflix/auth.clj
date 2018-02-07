(ns gregflix.auth
	(:require [gregflix.user :as users]
				[cemerick.friend :as friend]
				(cemerick.friend [credentials :as creds]
									 [workflows :as workflows])))

(defn check-user [{:keys [username password] :as creds}]	
	(when-let [user (users/find-by-username username)]
		(when (creds/bcrypt-verify password (:password user))
			{:identity (:username user) :roles #{::user} :user user})))

(defn authenticate [routes]
	(friend/authenticate routes
			{:credential-fn check-user
			 :workflows [(workflows/interactive-form)]}))