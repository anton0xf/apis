#!/usr/bin/env bb
(ns script
  (:require [clojure.java.io :as io]
            ;; https://github.com/babashka/babashka/blob/master/examples/httpkit_server.clj#L2
            [clojure.pprint :refer [pprint]]
            [babashka.curl :as curl]
            [cheshire.core :as json]))

;; to develop with CIDER nREPL see https://docs.cider.mx/cider/platforms/babashka.html
;; $ bb nrepl-server
;; > Started nREPL server at 127.0.0.1:1667
;; Then type C-c C-x c j (M-x cider-connect-clj) 127.0.0.1 RET 1667 RET

;; TODO: extract to util
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

;; task specific code
(def playlist-items-base-url
  "https://www.googleapis.com/youtube/v3/playlistItems")

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
  (get-paginated-list
   nil #(get-playlist-items-page id %)
   :nextPageToken
   #(->> % :items (map :snippet) (map get-video-info))))

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

