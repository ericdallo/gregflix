(ns gregflix.auth
	(:require [gregflix.user :as users]
			  [gregflix.handler.login :as login-handler]
			  [ring.util.request :as req]
			  [cemerick.friend :as friend]
			  (cemerick.friend [credentials :as creds]
							   [workflows :as workflows]))
	(:use [cemerick.friend.util :only (gets)]))

(defn- check-user [{:keys [username password] :as creds}]
	(when-let [user (users/find-by-username username)]
		(when (creds/bcrypt-verify password (:password user))
			{:identity (:username user) :roles #{::user} :user user})))

(defn- username
  [form-params params]
  (or (get form-params "username") (:username params "")))

(defn- password
  [form-params params]
  (or (get form-params "password") (:password params "")))

(defn- auth-session [user-record request]
	(login-handler/audit request)
	(workflows/make-auth user-record 
          		{::friend/workflow :interactive-form 
          		 ::friend/redirect-on-auth? true}))

(defn- interactive-form
  [& {:keys [login-uri credential-fn login-failure-handler] :as form-config}]
  (fn [{:keys [request-method params form-params] :as request}]
    (when (and (= (gets :login-uri form-config (::friend/auth-config request)) (req/path-info request))
               (= :post request-method))
      (let [creds {:username (username form-params params)
                   :password (password form-params params)}
            {:keys [username password]} creds]
        (if-let [user-record (and username password
                                  ((gets :credential-fn form-config (::friend/auth-config request))
                                   (with-meta creds {::friend/workflow :interactive-form})))]
          (auth-session user-record request)
          ((or (gets :login-failure-handler form-config (::friend/auth-config request)) #'workflows/interactive-login-redirect)
           (update-in request [::friend/auth-config] merge form-config)
           ))))))

(defn authenticate [routes]
	(friend/authenticate routes
			{:credential-fn check-user
			 :workflows [(interactive-form)]}))