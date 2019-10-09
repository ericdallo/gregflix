(use '[leiningen.exec :only (deps)])
(deps '[[com.datomic/datomic-free "0.9.5697"]])

(require '[datomic.api :as d])

(def conn (d/connect (second *command-line-args*)))

(defn find-last-id []
  (-> '{:find [(max ?id) .]
        :where [[?e :user/id ?id]]}
      (d/q (d/db conn))))

(defn create-user [args]
  (let [tx-tempid (d/tempid :db.part/tx)
        last-id (find-last-id)]
    [[:db/add tx-tempid :user/id (inc last-id)]
     [:db/add tx-tempid :user/username (nth args 0)]
     [:db/add tx-tempid :user/name  (nth args 1)]
     [:db/add tx-tempid :user/password  (nth args 2)]
     [:db/add tx-tempid :user/created-at  (java.util.Date.)]
     [:db/add tx-tempid :user/updated-at  (java.util.Date.)]]))

(->> (-> *command-line-args*
         vec
         (subvec 2)
         create-user)
     (d/transact conn))

#(d/transact conn (create-user (subvec (vec *command-line-args*) 2)))
