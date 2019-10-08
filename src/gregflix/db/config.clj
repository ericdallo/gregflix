(ns gregflix.db.config
  (:require [datomic.api :as d]))

(def datomic-uri (str "datomic:free://localhost:4334/gregflix?password="
                      (or (System/getenv "DATOMIC_PASSWORD")
                          "123mudar")))

(defn datomic-conn []
  (d/connect datomic-uri))

(defn datomic-db []
  (d/db (datomic-conn)))
