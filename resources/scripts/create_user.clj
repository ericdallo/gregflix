(use '[leiningen.exec :only (deps)])
(deps '[[com.datomic/datomic-free "0.9.5697"]])

(require '[datomic.api :as d])

(def conn (d/connect (second *command-line-args*)))

(defn find-last-id []
  (-> '{:find [(max ?id) .]
        :where [[?e :user/id ?id]]}
      (d/q (d/db conn))))

(defn create-user [args]
  [#:user{:id         (java.util.UUID/randomUUID)
          :username   (nth args 0)
          :name       (nth args 1)
          :password   (nth args 2)
          :created-at (java.util.Date.)}])

(->> (-> *command-line-args*
         vec
         (subvec 2)
         create-user)
     (d/transact conn))
