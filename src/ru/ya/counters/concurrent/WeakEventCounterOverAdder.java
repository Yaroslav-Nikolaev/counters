package ru.ya.counters.concurrent;

import ru.ya.counters.CountedEvent;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * This implementation suggests that write will be more often operation than reads(heavy writes).
 * @param <T>
 */
public class WeakEventCounterOverAdder<T extends CountedEvent> extends SeriesEventCounter<T> {
    private final LongAdder counter = new LongAdder();

    public WeakEventCounterOverAdder(ScheduledExecutorService executorService) {
        super(executorService);
    }

    @Override
    public void countEvent(CountedEvent countedEvent) {
        counter.increment();
    }


    @Override
    protected Long getQuantityInPeriodAndReset() {
        return counter.sumThenReset();
    }

    protected final long getQuantityInThisMoment(){
        return counter.sum();
    }
}
