
# Annotation Logger
Java Annotation-driven CSV logging system for FRC.

Annotation Logger (better name pending) is an easy to use, clean, and fast way for Java teams to log Boolean, String, and numerical fields and methods to CSV files for later reference.

# Usage
## Getting Started
To ensure Annotation Logger runs when the robot is enabled, call
```java
LoggingSystem.getInstance().start()
``` 

on robot enable, call

```java
LoggingSystem.getInstance.end()
```

on robot disable, and call
```java
LoggingSystem.getInstance().queueLogs();
```
periodically when the robot is enabled.
## Logging a class
Add this line to the constructor of the class you want to log:
```java
LoggingSystem.getInstance().registerObject(this.getClass(), this);
```
Then simply mark the fields and/or methods you want logged with `@Log`
```java
@Log
int myInt;

@Log
public String myString() {
  return "Hello!";
}
 ```
 Works on fields and zero parameter getters for Strings, doubles, ints, and Booleans.
## Output
Annotation Logger writes each class to it's own CSV.  Logs are stored in `/home/lvuser/logs` on the RoboRIO.

# Performance
Annotation Logger reflects over logged classes at startup, and caches references to fields and methods.  The increased startup time is a non-issue for FRC, as CAN devices and the radio will take much longer to come online.  

Runtime performance is equivalent to previously used reflective and direct access methods.  The main thread retrieves values and adds them to the queue, while the writer thread polls from the queue and writes to disk.

Detailed benchmarks can be found [here](/src/jmh/java/me/yizhang/annotationlogger/README.md).