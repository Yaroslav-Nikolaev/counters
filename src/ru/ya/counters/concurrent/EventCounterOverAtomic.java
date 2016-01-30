package ru.ya.counters.concurrent;

import ru.ya.counters.CountedEvent;

import java.util.concurrent.atomic.AtomicLong;

public class EventCounterOverAtomic extends SeriesEventCounter<CountedEvent> {

    private final AtomicLong counter = new AtomicLong(0);

    @Override
    protected Long getQuantityInPeriod() {
        return counter.getAndSet(0);
    }

    @Override
    public void count(CountedEvent countedEvent) {
        counter.incrementAndGet();
    }
}
