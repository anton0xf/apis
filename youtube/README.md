[YouTube API Getting Started](https://developers.google.com/youtube/v3/getting-started)
[Playlists/list](https://developers.google.com/youtube/v3/docs/playlists/list)

```sh
# get playlist as tsv file, e.g. PLnbH8YQPwKbnofSQkZE05PKzPXzbDCVXv
$ ./parse-playlist.sh "$PLAYLIST_ID" > playlist.tsv
# convert to list of Markdown links
<playlist.tsv awk -F'\t' '{print "watch [" $2 "](https://www.youtube.com/watch?v=" $1 ")"}'
```
