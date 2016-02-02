package ru.ya.timetric.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import ru.ya.timetric.counters.CountedEvent;
import ru.ya.timetric.counters.EventCounter;
import ru.ya.timetric.counters.SlicedEventCounterOverAdder;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class BenchMark {
    private final static CountedEvent sample = new CountedEvent() {
    };
    private final static int EVENTS_NUMBER = Integer.MAX_VALUE;

    private final static EventCounter<CountedEvent> ATOMIC_EVENT_COUNTER = new SlicedEventCounterOverAdder<>();
    private final static EventCounter<CountedEvent> ADDER_EVENT_COUNTER = new SlicedEventCounterOverAdder<>();

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
