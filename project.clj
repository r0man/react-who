(defproject sablono "0.8.4-SNAPSHOT"
  :description "Lisp style templating for Facebook's React."
  :url "http://github.com/r0man/sablono"
  :author "r0man"
  :min-lein-version "2.0.0"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.omcljs/om "1.0.0-beta2"]]
  :profiles {:dev {:dependencies [[criterium "0.4.4"]
                                  [devcards "0.2.4" :exclusions [sablono]]
                                  [doo "0.1.10"]
                                  [figwheel-sidecar "0.5.16-SNAPSHOT"]
                                  [funcool/tubax "0.2.0"]
                                  [org.clojure/test.check "0.9.0"]
                                  [perforate-x "0.1.0"]
                                  [reagent "0.7.0"]
                                  [rum "0.11.2" :exclusions [sablono]]]
                   :plugins [[lein-cljsbuild "1.1.7"]
                             [lein-doo "0.1.10"]
                             [lein-figwheel "0.5.15"]
                             [perforate "0.3.4"]]
                   :resource-paths ["test-resources" "target"]}
             :provided {:dependencies [[cljsjs/create-react-class "15.6.2-0"]
                                       [cljsjs/react "16.3.0-1"]
                                       [cljsjs/react-dom "16.3.0-1"]
                                       [cljsjs/react-dom-server "16.3.0-1"]
                                       [org.clojure/clojurescript "1.10.238"]]}
             :repl {:dependencies [[com.cemerick/piggieback "0.2.2"]]
                    :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}}
  :aliases {"benchmark" ["doo" "node" "benchmark" "once"]
            "ci" ["do" ["clean"] ["test"] ["test.nashorn"] ["test.node"] ["test.phantom"] ["benchmark"]]
            "deploy" ["do" "clean," "deploy" "clojars"]
            "test.nashorn" ["doo" "nashorn" "advanced" "once"]
            "test.node" ["doo" "node" "nodejs" "once"]
            "test.phantom" ["doo" "phantom" "advanced" "once"]}
  :clean-targets ^{:protect false} [:target-path]
  :cljsbuild {:builds
              [{:id "devcards"
                :compiler
                {:asset-path "devcards"
                 :aot-cache true
                 :main sablono.test.runner
                 :output-to "target/public/sablono.js"
                 :output-dir "target/public/devcards"
                 :optimizations :none
                 :pretty-print true
                 :source-map true
                 :verbose false}
                :figwheel {:devcards true}
                :source-paths ["src" "test"]}
               {:id "benchmark"
                :compiler
                {:asset-path "target/benchmark/out"
                 :aot-cache true
                 :main sablono.benchmark
                 :npm-deps
                 {:benchmark "1.0.0"
                  :react "16.3.0"
                  :react-dom "16.3.0"}
                 :install-deps true
                 :output-dir "target/benchmark/out"
                 :output-to "target/benchmark/sablono.js"
                 :optimizations :none
                 :target :nodejs
                 :pretty-print true
                 :verbose false}
                :source-paths ["src" "benchmark"]}
               {:id "nodejs"
                :compiler
                {:asset-path "target/nodejs/out"
                 :aot-cache true
                 :main sablono.test.runner
                 :npm-deps
                 {:benchmark "1.0.0"
                  :react "16.3.0"
                  :react-dom "16.3.0"}
                 :install-deps true
                 :optimizations :none
                 :output-dir "target/nodejs/out"
                 :output-to "target/nodejs/sablono.js"
                 :pretty-print true
                 :source-map true
                 :target :nodejs
                 :verbose false}
                :source-paths ["src" "test"]}
               {:id "none"
                :compiler
                {:asset-path "target/none/out"
                 :aot-cache true
                 :main sablono.test.runner
                 :output-to "target/none/sablono.js"
                 :output-dir "target/none/out"
                 :optimizations :none
                 :pretty-print true
                 :source-map true
                 :verbose false}
                :source-paths ["src" "test"]}
               {:id "advanced"
                :compiler
                {:asset-path "target/advanced/out"
                 :aot-cache true
                 :main sablono.test.runner
                 :output-dir "target/advanced/out"
                 :optimizations :advanced
                 :output-to "target/advanced/sablono.js"
                 ;; Polyfills needed for PhantomJS and Nashorn
                 :preamble ["polyfills/symbol.js"
                            "polyfills/symbol.iterator.js"
                            "polyfills/map.js"
                            "polyfills/set.js"
                            "polyfills/number.isnan.js"]
                 :pretty-print true
                 :verbose false}
                :source-paths ["src" "test"]}
               {:id "sample"
                :compiler
                {:asset-path "target/sample/out"
                 :aot-cache true
                 :main example.core
                 :output-dir "target/sample/out"
                 :output-to "target/sample/sablono.js"
                 :optimizations :advanced
                 :pseudo-names true
                 :pretty-print true
                 :verbose false}
                :source-paths ["src" "sample"]}]}
  :deploy-repositories [["releases" :clojars]]
  :perforate {:environments [{:namespaces [sablono.benchmark]}]}
  :test-selectors {:benchmark :benchmark
                   :default (complement :benchmark)})
