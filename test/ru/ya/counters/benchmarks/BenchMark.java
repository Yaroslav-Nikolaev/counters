package ru.ya.counters.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import ru.ya.counters.CountedEvent;
import ru.ya.counters.EventCounter;
import ru.ya.counters.concurrent.EventCounterOverAdder;
import ru.ya.counters.concurrent.EventCounterOverAtomic;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class BenchMark {
    private final static CountedEvent sample = new CountedEvent() {
    };
    private final static int EVENTS_NUMBER = Integer.MAX_VALUE;

    private final static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private final static EventCounter<CountedEvent> ATOMIC_EVENT_COUNTER = new EventCounterOverAtomic<>(scheduledExecutorService);
    private final static EventCounter<CountedEvent> ADDER_EVENT_COUNTER = new EventCounterOverAdder<>(scheduledExecutorService);

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testAtomic() {
        IntStream.range(0, EVENTS_NUMBER).parallel().forEach(i -> ATOMIC_EVENT_COUNTER.countEvent(sample));
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testAdder() {
        IntStream.range(0, EVENTS_NUMBER).parallel().forEach(i -> ADDER_EVENT_COUNTER.countEvent(sample));
    }
}
