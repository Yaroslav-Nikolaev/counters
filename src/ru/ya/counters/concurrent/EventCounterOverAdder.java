package ru.ya.counters.concurrent;

import ru.ya.counters.CountedEvent;

import java.util.concurrent.atomic.LongAdder;

public class EventCounterOverAdder extends SeriesEventCounter<CountedEvent> {
    private final LongAdder counter = new LongAdder();

    @Override
    public void count(CountedEvent countedEvent) {
        counter.increment();
    }


    @Override
    protected Long getQuantityInPeriod() {
        return counter.sumThenReset();
    }
}
