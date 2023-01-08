(ns scrap.aoc-tasks
  (:gen-class)
  (:require [babashka.curl :as curl]
            ;; [pl.danieljanus.tagsoup :refer [parse]]
            ))

;; https://adventofcode.com/2022

(comment
  (def page2022 (curl/get "https://adventofcode.com/2022"))

  (keys page2022)
  ;; => (:status :headers :body :err :process :exit)

  (:body page2022)
  )

(defn -main [& args]
  (-> (curl/get "https://adventofcode.com/2022")
      :body ;; parse
      println))
