#!/usr/bin/env bb
(ns script
  (:require [clojure.java.io :as io]
            ;; https://github.com/babashka/babashka/blob/master/examples/httpkit_server.clj#L2
            [clojure.pprint :refer [pprint]]
            [babashka.curl :as curl]
            [cheshire.core :as json]))

(defn run-interactively? []
  ;; https://github.com/babashka/babashka/blob/master/examples/htmx_todoapp.clj#L235
  (= *file* (System/getProperty "babashka.file")))

(def playlist-items-base-url
  "https://www.googleapis.com/youtube/v3/playlistItems")

(defn script-name []
  (.getName (io/file *file*)))

(defmacro stderr [& body]
  `(binding [*out* *err*]
     ~@body
     (flush)))

(defn get-api-key []
  (or (System/getenv "YOUTUBE_API_KEY")
      (slurp ".api-key")))

(defn get-playlist-items-page [id page-token]
  (try (let [res (curl/get playlist-items-base-url
                           {:debug true
                            :query-params
                            {"key" (get-api-key)
                             "playlistId" id
                             "part" "id,snippet"
                             "maxResults" 50
                             "pageToken" page-token}})]
         (stderr (println "request:" (:command res)))
         (-> res
             :body
             (json/parse-string true)))
       (catch Exception ex
         (stderr (println (ex-message ex))
                 (pprint (-> (ex-data ex)
                             :body
                             (json/parse-string true))))
         nil)))

(defn get-video-info [snippet]
  {:title (:title snippet)
   :video-id (-> snippet :resourceId :videoId)})

(defn get-playlist-items [id]
  (loop [res nil next nil]
    (let [page (get-playlist-items-page id next)
          next-page-token (:nextPageToken page)
          items (:items page)
          snippets (map :snippet items)
          page-res (map get-video-info snippets)
          res (concat res page-res)]
      (stderr (println "next page token:" next-page-token))
      (if next-page-token
        (recur res next-page-token)
        res))))

(defn playlist-to-tsv [id]
  (stderr (println "playlist id:" id))
  (doseq [item (get-playlist-items id)]
    (printf "%s\t%s\n" (:video-id item) (:title item)))
  (flush))

(when (run-interactively?)
  (if-let [playlist-id (first *command-line-args*)]
    (playlist-to-tsv playlist-id)
    (do (stderr (printf "Usage: ./%s playlist_id%n" (script-name)))
        (System/exit 1))))

