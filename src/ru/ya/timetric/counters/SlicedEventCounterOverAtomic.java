package ru.ya.timetric.counters;

import ru.ya.timetric.metrics.SlicedValue;

import java.util.concurrent.atomic.AtomicLong;

public class SlicedEventCounterOverAtomic<T extends CountedEvent> implements EventCounter<T>, SlicedValue<Long> {
    private final static long ZERO = 0;
    private final AtomicLong counter = new AtomicLong(0);

    @Override
    public void countEvent(T event) {
        counter.incrementAndGet();
    }

    @Override
    public Long getCurrentSlice() {
        return counter.get();
    }

    @Override
    public Long getCurrentSliceAndReset() {
        return counter.getAndSet(ZERO);
    }
}
