(ns migrations.migrator
  (:require [clojure.java.io :as io]
            [datomic.api :as d]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [clj-time.coerce :as c]
            [clojure.data.csv :as csv]))

(def tz (t/time-zone-for-id "America/Sao_Paulo"))
(def data-fmt (f/formatter "EEE MMM dd HH:mm:ss yyyy" tz))

(def local-uri "datomic:free://localhost:4334/gregflix?password=123mudar")
(def prod-uri (str "datomic:free://gregflix.site:4334/gregflix?password="))

(defn uuid [] (java.util.UUID/randomUUID))

(defn db [uri] (d/db (d/connect uri)))

(defn schema-names
  []
  (-> "resources/migrations/schema"
      io/file
      .list))

(defn get-schema-from-file
  [schema-name]
  (-> (str "migrations/schema/" schema-name)
      io/resource
      slurp
      read-string))

(defn load-schema-to-db
  [uri schema-name]
  (d/transact (d/connect uri) (get-schema-from-file schema-name)))

(defn load-all-schemas-to-local
  []
  (->> (schema-names)
       (map #(load-schema-to-db local-uri %))))

(defn recreate-db-local-db
  []
  (d/delete-database local-uri)
  (d/create-database local-uri))

(defn get-all-schemas
  [uri]
  (d/q '{:find [?attr ?type ?card]
         :where [[_ :db.install/attribute ?a]
                 [?a :db/valueType ?t]
                 [?a :db/cardinality ?c]
                 [?a :db/ident ?attr]
                 [?t :db/ident ?type]
                 [?c :db/ident ?card]]}
       (db uri)))

(defn safe-non-nil
  [data function]
  (when (and data
             (not (empty? data)))
    (function data)))

(defn remove-keys-if [m pred]
  (apply merge (for [[k v] m :when (not (pred v))] {k v})))


(defn find-id-by-user-temp-id
  [temp-id]
  (-> '{:find [?id .]
        :in [$ ?temp-id]
        :where [[?user :user/temp-id ?temp-id]
                [?user :user/id ?id]]}
      (d/q (db local-uri) temp-id)))

(defn find-id-by-movie-temp-id
  [temp-id]
  (-> '{:find [?id .]
        :in [$ ?temp-id]
        :where [[?movie :movie/temp-id ?temp-id]
                [?movie :movie/id ?id]]}
      (d/q (db local-uri) temp-id)))

(defn fix-types [m]
  (apply merge (for [[k v] m]

                 (cond
                   (= k :current-serie/user)
                   {k [:user/id (find-id-by-user-temp-id (Long/valueOf v))]}

                   (or (= k :related-movie/current-movie)
                       (= k :related-movie/related-movie))
                   {k [:movie/id (find-id-by-movie-temp-id (Long/valueOf v))]}

                   (or (= k :user/id)
                       (= k :current-serie/id)
                       (= k :movie/id)
                       (= k :serie/id)
                       (= k :related-movie/id))
                   {k (uuid)}

                   (or (= k :user/temp-id)
                       (= k :movie/temp-id))
                   {k (Long/valueOf v)}

                   (.contains (name k) "-at")
                   {k (c/to-date (f/parse data-fmt v))}

                   (or (= k :current-serie/season)
                       (= k :current-serie/episode)
                       (= k :serie/season)
                       (= k :serie/episode))
                   {k (Integer/parseInt v)}
                  
                   :else
                   {k v}))))

(defn fix-csv-data
  [row]
  (-> row
       (remove-keys-if empty?)
       (dissoc :user/updated-at)
       (dissoc :db/cas)
       (dissoc :db/retractEntity)
;       (dissoc :user/temp-id)
 ;      (dissoc :movie/temp-id)
       fix-types))

(defn parse-csv [path]
  (let [reader (io/reader path)
        csv-data (csv/read-csv reader)
        data (map zipmap
                  (->> (first csv-data) ;; First row is the header
                       (map keyword) ;; Drop if you want string keys instead
                       repeat)
                  (rest csv-data))]
    (map fix-csv-data data)))

(defn restore
  []
  (let [conn (d/connect local-uri)
        data (parse-csv "resources/migrations/dump.csv")]
    data
    (map (fn [row]
           (clojure.pprint/pprint (-> [] (conj row)))
           @(d/transact conn (-> [] (conj row))))
         data)))

(d/q '{:find [(count ?rm)]
       :in [$]
       :where [
               [?movie :movie/slug "star-wars-episode-vii"]
               [?rm :related-movie/current-movie ?movie]]}
     (db local-uri))
