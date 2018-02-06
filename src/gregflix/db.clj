(ns gregflix.db
  (:use korma.db))

(defdb db (mysql
            { :classname "com.mysql.jdbc.Driver"
              :subprotocol "mysql"
              :subname "//localhost/gregflix"
              :user "root"
              :password "123mudar"}))