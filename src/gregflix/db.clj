(ns gregflix.db
  (:use korma.db))

(defdb db (mysql
            { :classname "com.mysql.jdbc.Driver"
              :subprotocol "mysql"
              :subname "//mysql/gregflix"
              :user "root"
              :password "123mudar"}))