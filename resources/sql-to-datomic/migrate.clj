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
(require '[datomic.api :as d])

(def uri "datomic:free://localhost:4334/gregflix?password=123mudar")

(d/create-database uri)

(def conn (d/connect uri))

;; creating schema

(def user-schema [
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

(load-schema "sql-to-datomic/user.edn")

(def db (d/db conn))

(d/q '[:find ?name
       :where [?e :user/username ?name]]
     db)
