(use '[leiningen.exec :only (deps)])
(deps '[[com.datomic/datomic-free "0.9.5697"]])

(require '[datomic.api :as d])

(def conn (d/connect (second *command-line-args*)))

(defn parse-int [s]
   (Integer. (re-find  #"\d+" s )))

(defn find-serie-id [slug season episode]
  (-> '{:find  [?e .]
        :in [$ ?slug ?season ?episode]
        :where [[?e :serie/slug ?slug]
                [?e :serie/season ?season]
                [?e :serie/episode ?episode]]}
      (d/q (d/db conn) slug season episode)))

(defn exists-serie? [args]
  (let [serie-id (find-serie-id (nth args 0)
                                (parse-int (nth args 1))
                                (parse-int (nth args 2)))]
    (if serie-id 1 0)))

(-> *command-line-args*
     vec
     (subvec 2)
     exists-serie?
     println)
