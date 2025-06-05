#!/usr/bin/env bb
(require '[clojure.string :as str]
         '[clojure.java.io :as io]
         '[cheshire.core :as json])

(defn parse-file [path]
  (with-open [in (io/reader path)]
    (json/parse-stream in true)))

(def wildcard-prefix "*://*.")

(defn convert-wildcard-pattern [p]
  (assert (str/starts-with? p wildcard-prefix))
  (-> (.substring p (.length wildcard-prefix))
      (str/replace "." "\\.")
      list
      (conj "^https?://(.*)\\.")
      str/join))

(defn convert-wildcard-patterns [es]
  (letfn [(convert [e]
            (-> e
                (assoc :type "regex")
                (update :pattern convert-wildcard-pattern)))
          (convert? [e]
            (and (= "wildcard" (:type e))
                 (str/starts-with? (:pattern e) wildcard-prefix)))]
    (map #(if (convert? %) (convert %) %) es)))

(comment
  (def data (parse-file "/home/anton0xf/Downloads/FoxyProxy/FoxyProxy_2025-05-08(1).json"))

  (keys data) ;; => (:mode :sync :autoBackup :passthrough :theme :container :commands :data)

  (-> data :data first keys)
  ;; => (:exclude :color :proxyDNS :include :password :city :username :cc :type
  ;;     :hostname :port :tabProxy :title :pac :active :pacString)

  (->> data :data (map :title)) ;; => ("ssh -D 8082 a0xf-sh1" "local tor")

  (->> data :data first :include first)
  ;; => {:type "regex", :title "tor", :pattern "^.*://.*\\.onion/", :active false}

  (filter (complement :active) [{:active false} {:active true}]) ;; => ({:active false})
  (->> data :data first :include (filter (complement :active)))
  (->> data :data first :include (filter :active)
       (filter (comp #(= % "wildcard") :type))
       first)
  ;; => {:type "wildcard", :title "telegram", :pattern "*://*.telegram.org/", :active true}
  (->> data :data first :include
       (filter (comp #(= % "wildcard") :type))
       (filter (complement #(str/starts-with? (:pattern %) wildcard-prefix))))

  (str/starts-with? "*://*.telegram.org/" "*://*.")
  (str/replace-first "*://*.telegram.org/" "*://*." "^https?://(.*)\\.")
  (-> "*://*.telegram.org/"
      (.substring (.length wildcard-prefix))
      (str/replace "." "\\.")
      list (conj "^https?://(.*)\\.") str/join) ;; => "^https?://(.*)\\.telegram\\.org/"
  (convert-wildcard-pattern "*://*.telegram.org/") ;; => "^https?://(.*)\\.telegram\\.org/"
  )

(defn -main [& args])

(when (= *file* (System/getProperty "babashka.file"))
  (apply -main *command-line-args*))
