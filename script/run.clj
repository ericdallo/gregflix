(use 'ring.adapter.jetty)
(require '[gregflix.web :as web])

(run-jetty #'web/app {:port 8080})
