(use '[leiningen.exec :only (deps)])
(deps '[[com.datomic/datomic-free "0.9.5697"]])

(require '[datomic.api :as d])

(def conn (d/connect (second *command-line-args*)))

(def url-prefix "//file.gregflix.site/series/#2/s#4/e#5/#3.mp4")

(defn parse-int [s]
   (Integer. (re-find  #"\d+" s )))

(defn to-url [slug file-name season episode]
  (-> url-prefix
      (clojure.string/replace #"#2" slug)
      (clojure.string/replace #"#3" file-name)
      (clojure.string/replace #"#4" season)
      (clojure.string/replace #"#5" episode)))

(defn find-last-id []
  (-> '{:find [(max ?id) .]
        :where [[?e :serie/id ?id]]}
      (d/q (d/db conn))))

(defn create-serie [args]
  [#:serie{:id           (java.util.UUID/randomUUID)
           :title        (nth args 0)
           :slug         (nth args 1)
           :url          (to-url (nth args 1) (nth args 2) (nth args 3) (nth args 4))
           :description  (nth args 6)
           :season       (parse-int (nth args 3))
           :episode      (parse-int (nth args 4))
           :episode-name (nth args 5)
           :created-at   (java.util.Date.)}])

(->> (-> *command-line-args*
         vec
         (subvec 2)
         create-serie)
     (d/transact conn))
