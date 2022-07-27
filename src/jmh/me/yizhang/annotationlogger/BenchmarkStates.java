package me.yizhang.annotationlogger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import me.yizhang.annotationlogger.Log;

public class BenchmarkStates {

    public static final String path = Params.path;

    public static class TestData {
        public TestData(double x, double y) {
            datax = x;
            datay = y;

            for (Field field : this.getClass().getFields()) {
                field.setAccessible(true);
            }
        }

        @Log
        public double datax;
        @Log
        public double datay;
    }

    @State(Scope.Benchmark)
    public static class TestSample {
        public TestData data;

        private static TestSample mInstance;

        private TestSample() {
            data = new TestData(5, 3.5);
        }

        public static TestSample getInstance() {
            if (mInstance == null) {
                mInstance = new TestSample();
            }
            return mInstance;
        }
    }
    
    /**
     * Each class here represents the methods that surround a benchmark.
     * 
     * Methods tagged with @Setup are run before a benchmark, either at the start of
     * benchmark or before each method iteration.
     * 
     * Methods tagged with @TearDown are run at the end of a benchmark, in order to
     * flush all writers and ensure data is written properly.
     */

    @State(Scope.Thread)
    public static class instances254 {
        public src.perf.com.team254.ReflectingCSVWriter<TestData> mWriter;

        public static final String file = path + "254Log.csv";

        @Setup(Level.Iteration)
        public void setup() {
            // Clean output directory
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdir();
            }

            File output = new File(file);
            if (output.exists()) {
                output.delete();
            }

            try {
                output.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mWriter = new com.team254.lib.ReflectingCSVWriter<>(file, TestData.class);
        }

        @TearDown(Level.Iteration)
        public void teardown() {
            mWriter.write();
            mWriter.flush();
        }
    }

    @State(Scope.Thread)
    public static class instances1678r {

        public src.perf.com.team1678.LoggingSystem reflectingLoggingSystem;
        public src.perf.com.team1678.ReflectingLogStorage<TestData> reflectiveStorage;

        @Setup(Level.Iteration)
        public void setup() {
            // Clean output directory
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdir();
            }

            File output = new File(path + "1678ReflectiveLog.csv");
            if (output.exists()) {
                output.delete();
            }

            try {
                output.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            reflectingLoggingSystem = new com.team1678.lib.LoggingSystem();
            reflectiveStorage = new com.team1678.lib.ReflectingLogStorage<>(TestData.class);
            reflectingLoggingSystem.register(reflectiveStorage, "1678ReflectiveLog.csv");
        }

        @TearDown(Level.Iteration)
        public void teardown() {
            reflectingLoggingSystem.Log();
            reflectingLoggingSystem.Close();
        }
    }

    @State(Scope.Thread)
    public static class instances1678m {

        public src.perf.com.team1678.LoggingSystem manualLoggingSystem;
        public src.perf.com.team1678.LogStorage manualStorage = new com.team1678.lib.LogStorage();

        @Setup(Level.Iteration)
        public void setup() {
            // Clean output directory
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdir();
            }

            File output = new File(path + "1678ManualLog.csv");
            if (output.exists()) {
                output.delete();
            }

            try {
                output.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            manualLoggingSystem = new com.team1678.lib.LoggingSystem();

            ArrayList<String> headers = new ArrayList<String>();
            headers.add("datax");
            headers.add("datay");
            manualStorage.setHeaders(headers);

            manualLoggingSystem.register(manualStorage, "1678ManualLog.csv");
        }

        @TearDown(Level.Iteration)
        public void teardown() {
            manualLoggingSystem.Log();
            manualLoggingSystem.Close();
        }
    }

    @State(Scope.Thread)
    public static class instancesNewLogger {

        public me.yizhang.annotationlogger.LoggingSystem newLoggingSystem;

        @Setup(Level.Iteration)
        public void setup() {
            // Clean output directory
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdir();
            }

            File output = new File(path + "NewLogger.csv");
            if (output.exists()) {
                output.delete();
            }

            try {
                output.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            newLoggingSystem = new me.yizhang.lib.LoggingSystem();
            newLoggingSystem.registerObject(TestData.class, TestSample.getInstance().data);
            newLoggingSystem.setDirectory();
            newLoggingSystem.updateStorage();
        }

        @TearDown(Level.Iteration)
        public void teardown() {
            newLoggingSystem.getWriter().close();
        }
    }

}
