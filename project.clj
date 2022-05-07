(defproject ring-hello-world "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [org.clojure/math.combinatorics "0.1.5"]
                 ])
  ;; :profiles {:dev {:dependencies [[cider/cider-nrepl "0.15.1"]]
  ;;                  :repl-options {:host "0.0.0.0"
  ;;                                 :port 4500
  ;;                                 :nrepl-middleware [cider.nrepl.middleware.out/wrap-out]}}})
