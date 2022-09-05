Get some playlist content by shell version
```sh
$ ./parse-playlist.sh PLlx2izuC9gji7vdEkiEwIs7IHKY_64Ed2 > out/savv-alg.tsv
request: https://www.googleapis.com/youtube/v3/playlistItems?key=AI..kc&playlistId=PLlx2izuC9gji7vdEkiEwIs7IHKY_64Ed2&part=id,snippet&maxResults=50
next page token: EAAaBlBUOkNESQ
request: https://www.googleapis.com/youtube/v3/playlistItems?key=AI..kc&playlistId=PLlx2izuC9gji7vdEkiEwIs7IHKY_64Ed2&part=id,snippet&maxResults=50&pageToken=EAAaBlBUOkNESQ
next page token: null
```

Then check new implementation
```sh
$ diff out/savv-alg.tsv <(./parse_playlist.clj "PLlx2izuC9gji7vdEkiEwIs7IHKY_64Ed2")
```
