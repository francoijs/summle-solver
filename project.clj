(defproject summle-solver "0.1.0-SNAPSHOT"
  :min-lein-version "2.0.0"
  :description "Solver for summle.net"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
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
