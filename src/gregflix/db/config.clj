(ns gregflix.db.config
  (:require [datomic.api :as d]))

(def datomic-uri
  (let [host     (or (System/getenv "DATOMIC_DB_HOST")
                     "localhost")
        password (or (System/getenv "DATOMIC_DB_PASSWORD")
                     "123mudar")]
    (str "datomic:free://" host ":4334/gregflix?password=" password)))

(defn datomic-conn []
  (d/connect datomic-uri))

(defn datomic-db []
  (d/db (datomic-conn)))
