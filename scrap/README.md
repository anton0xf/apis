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


