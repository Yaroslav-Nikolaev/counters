package ru.ya.counters.concurrent;

import ru.ya.counters.CountedEvent;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This implementation suggests that write will be often but not too much, in case with heavy write better will be use {@link ru.ya.counters.concurrent.WeakEventCounterOverAdder}.
 *
 * @param <T>
 */
public class WeekEventCounterOverAtomic<T extends CountedEvent> extends SeriesEventCounter<T> {
    private final static long ZERO = 0;

    public WeekEventCounterOverAtomic(ScheduledExecutorService executorService) {
        super(executorService);
    }

    private final AtomicLong counter = new AtomicLong(0);

    @Override
    protected Long getQuantityInPeriodAndReset() {
        return counter.getAndSet(ZERO);
    }

    @Override
    public void countEvent(CountedEvent countedEvent) {
        counter.incrementAndGet();
    }

    protected final long getQuantityInThisMoment() {
        return counter.get();
    }
}
