(defproject ad-selector "0.0.0"
  :description "Selection logic for matching ad space to advert"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/core.async "0.4.490"]
                 [compojure "1.6.1"]
                 [motif "0.1.0"]]
  :source-paths ["src" "src/main/clojure"]
  :test-paths ["src/test" "src/test/clojure"]
  :target-path "target/%s/"
  :profiles
    {:dev {:dependencies [[org.clojure/tools.namespace "0.3.0-alpha4"]
                          [criterium "0.4.4"]]
           :resource-paths ["src/test/resources"]}})
