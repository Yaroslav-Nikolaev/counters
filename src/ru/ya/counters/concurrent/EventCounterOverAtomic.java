package ru.ya.counters.concurrent;

import ru.ya.counters.CountedEvent;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This implementation suggests that write will be more often operation than reads.
 * @param <T>
 */
public class EventCounterOverAtomic<T extends CountedEvent> extends SeriesEventCounter<T> {
    private final static long ZERO = 0;

    public EventCounterOverAtomic(ScheduledExecutorService executorService) {
        super(executorService);
    }

    private final AtomicLong counter = new AtomicLong(0);

    @Override
    protected Long getQuantityInPeriod() {
        return counter.getAndSet(ZERO);
    }

    @Override
    public void countEvent(CountedEvent countedEvent) {
        counter.incrementAndGet();
    }
}
