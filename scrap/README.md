# scrap
Some web scrapping tools

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
