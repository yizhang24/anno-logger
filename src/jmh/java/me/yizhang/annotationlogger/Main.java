package me.yizhang.annotationlogger;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = Params.warmup_iterations, batchSize = Params.num_rows)
@Measurement(iterations = Params.measured_iterations, batchSize = Params.num_rows)
@Fork(1)
public class Main {

    @Benchmark
    public void test254(BenchmarkStates.instances254 instance) {
        instance.mWriter.add(BenchmarkStates.TestSample.getInstance().data);
        instance.mWriter.write();
    }

    @Benchmark
    public void test1678r(BenchmarkStates.instances1678r instance) {
        instance.reflectiveStorage.Add(BenchmarkStates.TestSample.getInstance().data);
        instance.reflectingLoggingSystem.Log();
    }

    @Benchmark
    public void test1678m(BenchmarkStates.instances1678m instance) {
        ArrayList<Double> x = new ArrayList<Double>();
        x.add(BenchmarkStates.TestSample.getInstance().data.datax);
        x.add(BenchmarkStates.TestSample.getInstance().data.datay);
        instance.manualStorage.addData(x);
        instance.manualLoggingSystem.Log();
        x.clear();
    }

    @Benchmark
    public void testNewLogger(BenchmarkStates.instancesNewLogger instance) {
        instance.newLoggingSystem.queueLogs();
        instance.newLoggingSystem.mLogWriter.log();
    }
}