(defproject scrap "0.1.0-SNAPSHOT"
  :description "Some web scrapping tools"
  :license {:name "MIT License"}
  :dependencies [[org.clojure/clojure "1.11.1"]]
  :main ^:skip-aot scrap.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
