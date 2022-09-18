(ns a0xf.apis.youtube.common
  (:require [clojure.java.io :as io]))

(defmacro stderr [& body]
  `(binding [*out* *err*]
     ~@body
     (flush)))

(defn run-interactively? []
  ;; https://github.com/babashka/babashka/blob/master/examples/htmx_todoapp.clj#L235
  (= *file* (System/getProperty "babashka.file")))

(defn script-name []
  (.getName (io/file *file*)))

(defn get-paginated-list
  "helper to get token-based paginated list"
  [init-token     ;; token of first page, nil for example
   get-page-fn    ;; token -> page
   get-token-fn   ;; page -> token (of next page or nil on last page)
   get-content-fn ;; page -> list[item]
   ]
  (loop [acc nil
         token init-token]
    (let [page (get-page-fn token)
          next-token (get-token-fn page)
          content (get-content-fn page)
          new-acc (concat acc content)]
      (stderr (println "next page token:" next-token))
      (if next-token
        (recur new-acc next-token)
        new-acc))))
