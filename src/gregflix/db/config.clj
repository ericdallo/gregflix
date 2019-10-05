(ns gregflix.db.config
  (:require [korma.db :as k]))

(k/defdb db (k/mysql
             {:classname "com.mysql.jdbc.Driver"
              :subprotocol "mysql"
              :subname "//mysql/gregflix"
              :user "root"
              :password (System/getenv "DATABASE_PASSWORD")}))
