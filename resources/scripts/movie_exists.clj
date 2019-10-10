(use '[leiningen.exec :only (deps)])
(deps '[[com.datomic/datomic-free "0.9.5697"]])

(require '[datomic.api :as d])

(def conn (d/connect (second *command-line-args*)))

(defn find-movie-id [slug]
  (-> '{:find  [?e .]
        :in [$ ?slug]
        :where [[?e :movie/slug ?slug]]}
      (d/q (d/db conn) slug)))

(defn exists-movie? [slug]
  (let [movie-id (find-movie-id slug)]
    (if movie-id 1 0)))

(->> (nth *command-line-args* 2)
     exists-movie?
     println)
