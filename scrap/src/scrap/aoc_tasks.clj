(ns scrap.aoc-tasks
  (:gen-class)
  (:require [babashka.curl :as curl]
            [pl.danieljanus.tagsoup :as soup]
            [clojure.string :as str]))

(defmacro stderr [& body]
  (let [res (gensym 'res)]
    `(binding [*out* *err*]
       (let [~res (do ~@body)]
         (flush) ~res))))

(def base-url "https://adventofcode.com")

(defn select-tags [tag html]
  (filter #(= tag (first %)) html))

(defn select-tag [tag html]
  (first (select-tags tag html)))

(defn select-first-by-path [path html]
  (if (empty? path) html
      (recur (rest path)
             (->> html soup/children (select-tag (first path))))))

(defn select-by-path [path html]
  (if (empty? path) (list html)
      (reduce concat
              (for [child (->> html soup/children (select-tags (first path)))]
                (select-by-path (rest path) child)))))

(comment
  (def page2022 (curl/get (str base-url "/" 2022)))

  (keys page2022)
  ;; => (:status :headers :body :err :process :exit)

  (->> page2022 :body soup/parse-string
       (select-by-path [:body :main :pre :a])
       (map #(-> % soup/attributes :href)) first)
  ;; => "/2022/day/25"

  (def day-html
    (->> (str base-url "/2022/day/25")
         curl/get :body soup/parse-string))

  (->> day-html
       (select-first-by-path [:body :main :article :h2])
       soup/children first)
  ;; => "--- Day 25: Full of Hot Air ---"

  (str/replace "--- Day 25: Full of Hot Air ---"
               #"\s*---\s*" "")
  ;; => "Day 25: Full of Hot Air"
  )

(defn http-get [path]
  (curl/get (str base-url path)
            {:raw-args ["--connect-timeout" "1"]}))

(defn get-days-urls [year]
  (->> year (str "/") http-get
       :body soup/parse-string
       (select-by-path [:body :main :pre :a])
       reverse
       (map #(->> % soup/attributes :href))))

(defn get-day-title [path]
  (->> path http-get :body soup/parse-string
       (select-first-by-path [:body :main :article :h2])
       soup/children first))

(defn make-day-md-link [path title]
  (let [title (str/replace title #"\s*---\s*" "")
        url (str base-url path)]
    (format "[%s](%s)" title url)))

(comment
  (def days2022 (get-days-urls 2022))
  (first days2022)
  ;; => "/2022/day/1"
  (get-day-title "/2022/day/25")
  ;; => "--- Day 25: Full of Hot Air ---"
  (make-day-link "/2022/day/25" "--- Day 25: Full of Hot Air ---")
  ;; => "[Day 25: Full of Hot Air](https://adventofcode.com/2022/day/25)"

  (for [path (take 2 days2022)]
    (stderr (println "getting title for" path "...")
            (make-day-link path (get-day-title path))))
  ;; => getting title for /2022/day/1 ...
  ;;    getting title for /2022/day/2 ...
  ;;    ("[Day 1: Calorie Counting](https://adventofcode.com/2022/day/1)"
  ;;     "[Day 2: Rock Paper Scissors](https://adventofcode.com/2022/day/2)")
  )

(defn get-days-md-links [year]
  (for [path (get-days-urls year)]
    (stderr (println "getting title for" path "...")
            (make-day-md-link path (get-day-title path)))))

(comment
  (def days-links-2022 (get-days-md-links 2022))
  (doseq [link days-links-2022]
    (println (str "aoc2022: " link)))
  )

(defn -main [& args]
  (if-let [year (first args)]
    (doseq [link (get-days-md-links year)]
      (println (str "aoc" year ": " link)))
    (stderr (println "Usage: aoc-tasks year"))))
