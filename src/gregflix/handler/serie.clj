(ns gregflix.handler.serie
	(:require [gregflix.serie :as series]))

(defn show [slug season episode]
	{:video (series/find-by slug season episode)})