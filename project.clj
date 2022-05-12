(defproject summle-solver "0.1.0"
  :min-lein-version "2.0.0"
  :description "Solver for summle.net"
  :url "https://github.com/francoijs/summle-solver"
  :license {:name "GNU GPL v3+"
            :url "http://www.gnu.org/licenses/gpl-3.0.en.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [org.clojure/math.combinatorics "0.1.5"]
                 [ring "1.1.8"]
                 [compojure "1.6.2"]
                 [de.ubercode.clostache/clostache "1.3.1"]
                 [org.clojure/data.json "2.4.0"]
                 ]
  :main summle.core
  )
