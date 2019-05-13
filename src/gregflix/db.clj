(ns gregflix.db
  (:use korma.db))

(defdb db (mysql
            { :classname "com.mysql.jdbc.Driver"
              :subprotocol "mysql"
              :subname "//mysql/gregflix"
              :user "root"
              :password "P9xTYHvqyDkEwfEEKKsNcvU4GvyEKPMgXqwm4qkuPB38UE4V9RfRsq"})) ;TODO externalize
