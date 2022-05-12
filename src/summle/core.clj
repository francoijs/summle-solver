(ns summle.core
  (:use compojure.core)
  (:require
   [ring.adapter.jetty :as jetty]
   [clostache.parser :as clostache]
   [compojure.route :as route]
   [compojure.handler]
   [clojure.data.json :as json]
   [summle.solver]
   ))

(defn read-template [template-name]
  (slurp (clojure.java.io/resource
    (str "templates/" template-name ".mustache"))))

(defn render-template [template-file params]
  (clostache/render (read-template template-file) params))

;; View functions
(defn index []
  (render-template "index" {:greeting "Bonjour"}))

;; Routing
(defroutes main-routes
  (GET "/solve" [result & cards]
       (json/write-str (summle.solver/solve
                        (map #(Integer/parseUnsignedInt %) (vals cards))
                        (Integer/parseUnsignedInt result))))
  (GET "/" [] (index))
  (route/resources "/")
  (route/not-found "404 Not Found")
  )

;; Required to extract params from the query
(def routes-handler (-> main-routes compojure.handler/api))

(defn -main []
  (jetty/run-jetty routes-handler {:port 5000}))
