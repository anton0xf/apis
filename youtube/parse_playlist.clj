#!/usr/bin/env bb
(ns script
  (:require [clojure.java.io :as io]
            ;; https://github.com/babashka/babashka/blob/master/examples/httpkit_server.clj#L2
            [clojure.pprint :refer [pprint]]
            [babashka.curl :as curl]
            [cheshire.core :as json]))

(def playlist-items-base-url
  "https://www.googleapis.com/youtube/v3/playlistItems")

(defn script-name []
  (.getName (io/file *file*)))

(defmacro stderr [& body]
  `(binding [*out* *err*]
     ~@body
     (flush)))

(defn try-get-arg []
  (when-not *command-line-args*
    (stderr (printf "Usage: ./%s playlist_id%n" (script-name)))
    (System/exit 1))
  (first *command-line-args*))

(defn get-playlist-id []
  (let [id (try-get-arg)]
    (stderr (println "playlist id:" id))
    id))

(defn get-api-key [] "")

(defn get-playlist-items-page [id]
  (try (curl/get playlist-items-base-url
                 {:query-params
                  {"key" (get-api-key)
                   "playlistId" id
                   "part" "id,snippet"
                   "maxResults" 50}})
       (catch Exception ex
         (stderr (println (ex-message ex))
                 (pprint (-> (ex-data ex)
                             :body
                             (json/parse-string true))))
         ;; (System/exit 2)
         )))

(defn get-playlist-items [id]
  (let [page (get-playlist-items-page id)]
    page))

;; https://github.com/babashka/babashka/blob/master/examples/htmx_todoapp.clj#L235
(when (= *file* (System/getProperty "babashka.file"))
  (let [id (get-playlist-id)
        items (get-playlist-items id)]
    items))

