(ns summle.core
  (:use compojure.core)
  (:require
   [ring.adapter.jetty :as jetty]
   ))

(defroutes main-routes
  (GET "/" [] "Hello World"))

(defn -main []
  (jetty/run-jetty main-routes {:port 5000}))
