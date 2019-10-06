(ns sql-to-datomic.migrate
  (:require [clojure.java.jdbc :as jdbc]
            [datomic.api :as d]
            [clojure.java.io :as io]))

(def *db* {:classname "com.mysql.jdbc.Driver"
     :subprotocol "mysql"
     :subname "//mysql:3306/gregflix"
     :user "root"
     :password ""})

(def query (partial jdbc/query *db*))

(defn dump [table]
  (spit (format "%s.edn" table)
    (with-out-str (-> (format "select * from %s" table)
                      query
                      prn))))

;; to insert to datomic
(def uri "datomic:free://localhost:4334/gregflix?password=123mudar")

(d/create-database uri)

(def conn (d/connect uri))

(defn read-edn
  [filename]
  (-> filename
      io/resource
      slurp
      read-string))

;; reads and loads a schema from EDN file
(defn load-schema
  [filename]
  (d/transact conn (read-edn filename)))

;; find all attrs
(d/q '[:find ?attr ?type ?card
       :where
       [_ :db.install/attribute ?a]
       [?a :db/valueType ?t]
       [?a :db/cardinality ?c]
       [?a :db/ident ?attr]
       [?t :db/ident ?type]
       [?c :db/ident ?card]]
     db)

;; creating user schema

(defn user-to-datomic
  [{:keys [id
           username
           name
           password
           created_at
           updated_at]}]

  {:user/id id
   :user/username username
   :user/name name
   :user/password password
   :user/created-at created_at
   :user/updated-at updated_at})

(def user-schema [
      {:db/ident :user/id
       :db/valueType :db.type/long
       :db/cardinality :db.cardinality/one
       :db/unique :db.unique/identity}
      {:db/ident :user/username
       :db/valueType :db.type/string
       :db/cardinality :db.cardinality/one
       :db/unique :db.unique/identity}
      {:db/ident :user/name
       :db/valueType :db.type/string
       :db/cardinality :db.cardinality/one}
      {:db/ident :user/password
       :db/valueType :db.type/string
       :db/cardinality :db.cardinality/one}
      {:db/ident :user/created-at
       :db/valueType :db.type/instant
       :db/cardinality :db.cardinality/one}
      {:db/ident :user/updated-at
       :db/valueType :db.type/instant
       :db/cardinality :db.cardinality/one}])

(d/transact conn user-schema)

(->> "sql-to-datomic/user.edn" io/resource slurp read-string (map user-to-datomic) (d/transact conn))

(def db (d/db conn))

;; creating login-audit schema

(def login-audit-schema [
      {:db/ident :login-audit/id
       :db/valueType :db.type/long
       :db/cardinality :db.cardinality/one
       :db/unique :db.unique/identity}
      {:db/ident :login-audit/ip
       :db/valueType :db.type/string
       :db/cardinality :db.cardinality/one}
      {:db/ident :login-audit/user
       :db/valueType :db.type/ref
       :db/isComponent true
       :db/cardinality :db.cardinality/one}
      {:db/ident :login-audit/device
       :db/valueType :db.type/string
       :db/cardinality :db.cardinality/one}
      {:db/ident :login-audit/created-at
       :db/valueType :db.type/instant
       :db/cardinality :db.cardinality/one}
      {:db/ident :login-audit/updated-at
       :db/valueType :db.type/instant
       :db/cardinality :db.cardinality/one}])


(defn login-audit-to-datomic
  [{:keys [id
           ip
           user_id
           device
           created_at
           updated_at]}]

  {:login-audit/id id
   :login-audit/ip ip
   :login-audit/user [:user/id user_id]
   :login-audit/device device
   :login-audit/created-at created_at
   :login-audit/updated-at updated_at})

(d/transact conn login-audit-schema)

(->> "sql-to-datomic/login_audit.edn" io/resource slurp read-string (map login-audit-to-datomic) (d/transact conn))

;; creating movie schema
(defn movie-to-datomic
  [{:keys [id
           title
           slug
           description
           url
           created_at]}]

  {:movie/id id
   :movie/title title
   :movie/slug slug
   :movie/url url
   :movie/description description
   :movie/created-at created_at})

(def movie-schema [
      {:db/ident :movie/id
       :db/valueType :db.type/long
       :db/cardinality :db.cardinality/one
       :db/unique :db.unique/identity}
      {:db/ident :movie/title
       :db/valueType :db.type/string
       :db/cardinality :db.cardinality/one}
      {:db/ident :movie/slug
       :db/valueType :db.type/string
       :db/cardinality :db.cardinality/one
       :db/unique :db.unique/identity}
      {:db/ident :movie/url
       :db/valueType :db.type/string
       :db/cardinality :db.cardinality/one}
      {:db/ident :movie/description
       :db/valueType :db.type/string
       :db/cardinality :db.cardinality/one}
      {:db/ident :movie/created-at
       :db/valueType :db.type/instant
       :db/cardinality :db.cardinality/one}])

(d/transact conn movie-schema)

(->> "sql-to-datomic/movie.edn" io/resource slurp read-string (map movie-to-datomic) (d/transact conn))

(def db (d/db conn))

(d/q '[:find ?slug
       :where [_ :movie/slug ?slug]] db)
