(defproject scrap "0.1.0-SNAPSHOT"
  :description "Some web scrapping tools"
  :license {:name "MIT License"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [babashka/babashka.curl "0.1.2"]
                 [org.clojure/data.xml "0.0.8"]
                 [clj-tagsoup/clj-tagsoup "0.3.0" :exclusions [org.clojure/clojure]]]
  :main scrap.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
