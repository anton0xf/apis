#!/bin/sh

PL_ID="$1"

RESOURCE='https://www.googleapis.com/youtube/v3/playlistItems'
# TODO get it from file or env
API_KEY="${YOUTUBE_API_KEY:-$(cat .api-key)}"
BASE_REQUEST="${RESOURCE}?key=${API_KEY}&playlistId=${PL_ID}"
BASE_REQUEST="${BASE_REQUEST}&part=id,snippet&maxResults=50"

parse_response() {
    ITEMS_EXPR='.items[] | .snippet | {title, videoId: .resourceId.videoId}'
    jq "{nextPageToken, items: [$ITEMS_EXPR]}"
}

do_request() {
    REQUEST="$BASE_REQUEST"
    if [ -n "$1" ]; then
        REQUEST="$REQUEST&pageToken=$1"
    fi
    echo "request: $REQUEST" >&2
    curl -s "$REQUEST" | parse_response
}

while :; do
    RESP="$(do_request "$PAGE_TOKEN")"
    PAGE_TOKEN="$(echo "$RESP" | jq -r '.nextPageToken')"
    echo "next page token: $PAGE_TOKEN" >&2
    echo "$RESP" | jq -r '.items[] | [.videoId, .title] | @tsv'
    [ -z "$PAGE_TOKEN" ] || [ "$PAGE_TOKEN" = "null" ] && exit
    sleep 1
done
