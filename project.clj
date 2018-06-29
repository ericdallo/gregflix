(defproject gregflix "0.0.1"
  :description "Gregflix"
  :url "http://example.com/FIXME"
  :dependencies
      [[org.clojure/clojure "1.9.0"]
       [ring/ring-core "1.6.3"]
       [ring/ring-devel "1.6.3"]
       [ring/ring-jetty-adapter "1.6.3"]
       [compojure "1.6.0"]
       [com.cemerick/friend "0.2.3"]
       [selmer "1.11.6"]
       [korma "0.4.3"]
       [mysql/mysql-connector-java "5.1.6"]
       [migratus "1.0.6"]
       [migratus-lein "0.5.7"]]
  :plugins 
      [[lein-ring "0.12.3"]
      [lein-sass "0.4.0"]
      [migratus-lein "0.5.7"]]
  :ring {:handler gregflix.web/app}
  :sass {:src "resources/sass"
         :output-directory "resources/public/css"
         :style :compressed}
  :hooks [leiningen.sass]
  :migratus {:store :database
             :migration-dir "migrations"
             :db {:classname "com.mysql.jdbc.Driver"
                  :subprotocol "mysql"
                  :subname "//localhost/gregflix"
                  :user "root"
                  :password "123mudar"}}
  :main gregflix.main)
