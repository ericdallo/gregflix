(ns migrations.migrator
  (:require [clojure.java.io :as io]
            [datomic.api :as d]))

(def local-uri "datomic:free://localhost:4334/gregflix?password=123mudar")

(def conn d/connect)

(defn db [uri] (d/db (conn uri)))

(defn schema-names
  []
  (-> "resources/migrations/schema"
      io/file
      .list))

(defn get-schema-from-file
  [schema-name]
  (-> (str "migrations/schema/" schema-name)
      io/resource
      slurp
      read-string))

(defn load-schema-to-db
  [uri schema-name]
  (d/transact (conn uri) (get-schema-from-file schema-name)))

(defn load-all-schemas-to-local
  []
  (->> (schema-names)
       (map #(load-schema-to-db local-uri %))))

(defn recreate-db-local-db
  []
  (d/delete-database local-uri)
  (d/create-database local-uri))

(defn get-all-schemas
  [uri]
  (d/q '{:find [?attr ?type ?card]
         :where [[_ :db.install/attribute ?a]
                 [?a :db/valueType ?t]
                 [?a :db/cardinality ?c]
                 [?a :db/ident ?attr]
                 [?t :db/ident ?type]
                 [?c :db/ident ?card]]}
       (db uri)))
