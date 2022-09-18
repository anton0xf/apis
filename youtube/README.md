* [YouTube API Getting Started](https://developers.google.com/youtube/v3/getting-started)
* [Playlists/list](https://developers.google.com/youtube/v3/docs/playlists/list)

Get youtube api key and save it into .api-key here or set in YOUTUBE_API_KEY environment variable.

```sh
# get playlist as tsv file, e.g. PLnbH8YQPwKbnofSQkZE05PKzPXzbDCVXv
$ ./playlist-to-tsv.sh "$PLAYLIST_ID" > out/playlist.tsv
# convert to list of Markdown links
<out/playlist.tsv awk -F'\t' '{print "watch [" $2 "](https://www.youtube.com/watch?v=" $1 ")"}'
```
