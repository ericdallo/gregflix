(ns gregflix.interceptor.auth
  (:require [cemerick.friend :as friend]
            [cemerick.friend.credentials :as creds]
            [cemerick.friend.util :refer [gets]]
            [cemerick.friend.workflows :as workflows]
            [gregflix.controller.login :as c-login]
            [gregflix.db.config :as db-config]
            [gregflix.db.user :as db-user]
            [ring.util.request :as req]))

(defn- check-user [{:keys [username password]}]
  (let [db (db-config/datomic-db)]
    (when-let [user (db-user/find-by-username db username)]
      (when (creds/bcrypt-verify password (:user/password user))
        {:identity (:user/username user)
         :roles #{:gregflix/user}
         :user user}))))

(defn- username
  [form-params params]
  (or (get form-params "username")
      (:username params "")))

(defn- password
  [form-params params]
  (or (get form-params "password")
      (:password params "")))

(defn- auth-session [user-record request]
  (c-login/audit request)
  (workflows/make-auth user-record
                       {::friend/workflow :interactive-form
                        ::friend/redirect-on-auth? true}))

(defn- interactive-form
  [& form-config]
  (fn [{:keys [request-method params form-params] :as request}]
    (when (and (= (gets :login-uri form-config (::friend/auth-config request)) (req/path-info request))
               (= :post request-method))
      (let [creds {:username (username form-params params)
                   :password (password form-params params)}
            {:keys [username password]} creds]
        (if-let [user-record (and username
                                  password
                                  ((gets :credential-fn form-config (::friend/auth-config request))
                                   (with-meta creds {::friend/workflow :interactive-form})))]
          (auth-session user-record request)
          ((or (gets :login-failure-handler form-config (::friend/auth-config request))
               #'workflows/interactive-login-redirect)
           (update-in request [::friend/auth-config] merge form-config)))))))

(defn authenticate [routes]
  (friend/authenticate routes
                       {:credential-fn check-user
                        :workflows [(interactive-form)]}))
