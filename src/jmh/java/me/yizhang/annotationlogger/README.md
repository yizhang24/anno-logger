
# Benchmarks
Benchmarks run using [Java Microbench Harness](https://github.com/openjdk/jmh) and the [jmh-gradle-plugin](https://github.com/melix/jmh-gradle-plugin).

Results are by default measured in ms/1,000,000 rows written, and combine operation time for both reading values and writing to storage.
# Sample results
Tests run on a i7-11800H @ 2.3 GHz
```
Benchmark           Mode  Cnt    Score    Error  Units
Main.test1678m        ss   25  203.034 ± 16.101  ms/op
Main.test1678r        ss   25  211.159 ±  9.083  ms/op
Main.test254          ss   25  168.016 ±  4.574  ms/op
Main.testAnnoLogger   ss   25  189.744 ±  9.434  ms/op
```
## Didn't we already benchmark these?
In 2020, we ran a simple stopwatch test of various logging methods.  However, an error was made in the setup of the benchmarks, resulting in a false measurement of 
`99 ms / 100000 rows` for our manual implementation 
```java
ArrayList<Double> x = new ArrayList<Double>();
for(int i = 0; i < num_rows; i++) {
  x.add(data.datax);
  x.add(data.datay);
  
  mStorage.addData(x);
  
  x.clear(); // Here!
  ls.Log();
}
```
The ArrayList.clear() call was executed before the logger wrote to file, resulting in a blank output file.
```
1678r.csv - 8790 KB
254r.csv - 9766 KB
1678m.csv - 977 KB
```
# Running your own benchmarks

Compile with
```
gradle jmhJar
```
Then run with 
```
 java -jar build/libs/annotationlogger-jmh.jar
```
Benchmark options can be found in [Params.java](Params.java).   To switch benchmark type change the mode in [Main.java](Main.java).