(use '[leiningen.exec :only (deps)])
(deps '[[com.datomic/datomic-free "0.9.5697"]])

(require '[datomic.api :as d])

(def conn (d/connect (second *command-line-args*)))

(def url-prefix "//file.gregflix.site/movies/#2/#3.mp4")

(defn to-url [slug file-name]
  (-> url-prefix
      (clojure.string/replace #"#2" slug)
      (clojure.string/replace #"#3" file-name)))

(defn find-last-id []
  (-> '{:find [(max ?id) .]
        :where [[?e :movie/id ?id]]}
      (d/q (d/db conn))))

(defn create-movie [args]
  (let [tx-tempid (d/tempid :db.part/tx)
        last-id (find-last-id)]
    [[:db/add tx-tempid :movie/id (inc last-id)]
     [:db/add tx-tempid :movie/title (nth args 0)]
     [:db/add tx-tempid :movie/slug  (nth args 1)]
     [:db/add tx-tempid :movie/url  (to-url (nth args 1) (nth args 2))]
     [:db/add tx-tempid :movie/description  (nth args 3)]
     [:db/add tx-tempid :movie/created-at (java.util.Date.)]]))

(->> (-> *command-line-args*
         vec
         (subvec 2)
         create-movie)
     (d/transact conn))
