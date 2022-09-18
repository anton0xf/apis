Get some playlist content by shell version
```sh
$ ./playlist-to-tsv.sh PLlx2izuC9gji7vdEkiEwIs7IHKY_64Ed2 > out/savv-alg.tsv

request: https://www.googleapis.com/youtube/v3/playlistItems?key=AI..kc&playlistId=PLlx2izuC9gji7vdEkiEwIs7IHKY_64Ed2&part=id,snippet&maxResults=50
next page token: EAAaBlBUOkNESQ
request: https://www.googleapis.com/youtube/v3/playlistItems?key=AI..kc&playlistId=PLlx2izuC9gji7vdEkiEwIs7IHKY_64Ed2&part=id,snippet&maxResults=50&pageToken=EAAaBlBUOkNESQ
next page token: null
```

Then check new implementation
```sh
$ diff out/savv-alg.tsv <(./playlist-to-tsv.bb "PLlx2izuC9gji7vdEkiEwIs7IHKY_64Ed2")

playlist id: PLlx2izuC9gji7vdEkiEwIs7IHKY_64Ed2
request: [curl --show-error --dump-header /tmp/babashka.curl16274228519034007235.headers --compressed --silent --location https://www.googleapis.com/youtube/v3/playlistItems?key=AI..DE&playlistId=PLlx2izuC9gji7vdEkiEwIs7IHKY_64Ed2&part=id%2Csnippet&maxResults=50&pageToken=]
next page token: EAAaBlBUOkNESQ
request: [curl --show-error --dump-header /tmp/babashka.curl10893575813701667074.headers --compressed --silent --location https://www.googleapis.com/youtube/v3/playlistItems?key=AI..DE&playlistId=PLlx2izuC9gji7vdEkiEwIs7IHKY_64Ed2&part=id%2Csnippet&maxResults=50&pageToken=EAAaBlBUOkNESQ]
next page token: nil
```
