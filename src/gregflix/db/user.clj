(ns gregflix.db.user
  (:require [datomic.api :as d]))

(defn find-by-username [db username]
  (-> '{:find [(pull ?user [*]) .]
        :in   [$ ?username]
        :where [[?user :user/username ?username]]}
      (d/q db username)))
