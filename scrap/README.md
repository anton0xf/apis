# scrap
Some web scrapping tools

## Libs
* [clj-tagsoup](https://github.com/nathell/clj-tagsoup) - wrapper around
  [TagSoup](http://home.ccil.org/~cowan/XML/tagsoup/).
  It's not supported from 2020 but works well.
  Has some dependency issues.
  Parse to [hiccup](http://github.com/weavejester/hiccup) format
* [Enlive](https://github.com/cgrand/enlive) is a selector-based (à la CSS) templating library for Clojure.
  * https://masnun.com/2016/03/20/web-scraping-with-clojure.html - tutor
* [Jsoup](https://jsoup.org/)
  * https://stackoverflow.com/questions/65591867/using-jsoup-to-parse-a-string-with-clojure
  * https://gist.github.com/eldritchideen/9495299265a5cd04d450 - very simple Jsoup wrapper
  * [Reaver](https://github.com/mischov/reaver) - wrapper around [Jsoup](https://jsoup.org/)
  * https://github.com/mfornos/clojure-soup
* https://github.com/clj-commons/hickory
  * https://github.com/dfuenzalida/lazada-scrape/blob/master/project.clj - example

## Usage
run by lein
```sh
# run scrap.core
$ lein run
# or select main class
$ lein run -m scrap.aoc-tasks
```
or build uberjar and run by java
```sh
$ lein uberjar
# run scrap.core
$ java -jar target/default+uberjar/scrap-0.1.0-SNAPSHOT-standalone.jar
# or select main class
$ java -cp target/default+uberjar/scrap-0.1.0-SNAPSHOT-standalone.jar scrap.aoc_tasks
```

## Tools
### AoC titles
Get Markdown links for all tasks from passed year of Advent of Code
```sh
$ lein run -m scrap.aoc-tasks 2022 | tee out/aoc2022-links.md
$ cat out/aoc2022-links.md
aoc2022: [Day 1: Calorie Counting](https://adventofcode.com/2022/day/1)
aoc2022: [Day 2: Rock Paper Scissors](https://adventofcode.com/2022/day/2)
...
```
