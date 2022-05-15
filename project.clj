(defproject summle-solver "0.1.1"
  :min-lein-version "2.0.0"
  :description "Solver for summle.net"
  :url "https://github.com/francoijs/summle-solver"
  :license {:name "GNU GPL v3+"
            :url "http://www.gnu.org/licenses/gpl-3.0.en.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [ring/ring-core "1.9.5"]
                 [ring/ring-jetty-adapter "1.9.5"]
                 [org.clojure/math.combinatorics "0.1.6"]
                 [ring "1.9.5"]
                 [compojure "1.6.3"]
                 [de.ubercode.clostache/clostache "1.4.0"]
                 [org.clojure/data.json "2.4.0"]
                 ]
  :plugins [[lein-ancient "0.7.0"]
            ]
  :main summle.core
  )
