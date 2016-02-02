package ru.ya.timetric.counters;

import ru.ya.timetric.metrics.SlicedValue;

import java.util.concurrent.atomic.LongAdder;

public class SlicedEventCounterOverAdder<T extends CountedEvent> implements EventCounter<T>, SlicedValue<Long> {

    private final LongAdder counter = new LongAdder();

    @Override
    public void countEvent(T event) {
        counter.increment();
    }

    @Override
    public Long getCurrentSlice() {
        return counter.sum();
    }

    @Override
    public Long getCurrentSliceAndReset() {
        return counter.sumThenReset();
    }
}
