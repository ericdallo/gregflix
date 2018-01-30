(ns gregflix.web
	(:gen-class)
    (:use compojure.core)
    (:use ring.middleware.json-params)
    (:use ring.adapter.jetty)
    (:require [clj-json.core :as json]))

(defn json-response [data & [status]]
	{:status (or status 200)
	:headers {"Content-Type" "application/json"}
	:body (json/generate-string data)})

(defroutes handler
	(GET "/status" []
		(json-response {"status" "UP"})))
(def app
	(-> handler
		wrap-json-params))

(defn -main [& args]
 (run-jetty app {:port 8080}))