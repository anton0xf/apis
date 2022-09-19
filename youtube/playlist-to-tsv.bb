#!/usr/bin/env bb
(require '(a0xf.apis.youtube [common :refer [stderr script-name]]
                             [playlist-to-tsv :refer [playlist-to-tsv]]))

(if-let [playlist-id (first *command-line-args*)]
  (playlist-to-tsv playlist-id)
  (do (stderr (printf "Usage: ./%s playlist_id%n" (script-name)))
      (System/exit 1)))

