package ru.ya.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import ru.ya.counters.EventCounter;
import ru.ya.counters.concurrent.EventCounterOverAdder;
import ru.ya.counters.concurrent.EventCounterOverAtomic;
import ru.ya.counters.CountedEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class BenchMark {
    private final static CountedEvent sample = new CountedEvent() {
    };

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testAtomic() {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        EventCounter<CountedEvent> eventCounter = new EventCounterOverAtomic();
        IntStream.range(0, 20000).forEach(i -> executorService.submit(() -> eventCounter.count(sample)));
        executorService.shutdown();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testAdder() {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        EventCounter<CountedEvent> eventCounter = new EventCounterOverAdder();
        IntStream.range(0, 20000).forEach(i -> executorService.submit(() -> eventCounter.count(sample)));
        executorService.shutdown();
    }


//    public static void main(String[] args) throws RunnerException {
//
//        Options opt = new OptionsBuilder()
//                .include(ru.ya.benchmarks.BenchMark.class.getSimpleName())
//                .warmupIterations(5)
//                .measurementIterations(5)
//                .forks(1)
//                .build();
//
//
//        new Runner(opt).run();
//
//    }

}
