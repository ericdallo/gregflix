(ns gregflix.db.config
  (:require [korma.db :as k]
            [datomic.api :as d]))

(def datomic-uri (str "datomic:free://localhost:4334/gregflix?password="
                      (or (System/getenv "DATOMIC_PASSWORD")
                          "123mudar")))

(k/defdb db (k/mysql
             {:classname "com.mysql.jdbc.Driver"
              :subprotocol "mysql"
              :subname "//mysql/gregflix"
              :user "root"
              :password (System/getenv "DATABASE_PASSWORD")}))

(defn datomic-db []
  (d/db (d/connect datomic-uri)))
