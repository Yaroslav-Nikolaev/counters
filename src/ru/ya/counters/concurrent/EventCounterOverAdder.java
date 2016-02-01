package ru.ya.counters.concurrent;

import ru.ya.counters.CountedEvent;

import java.util.concurrent.ScheduledExecutorService;

/**
 * More accurate implementation of {@link ru.ya.counters.concurrent.WeakEventCounterOverAdder}
 * which decrease an error in results.
 * @param <T>
 */
public class EventCounterOverAdder<T extends CountedEvent> extends WeakEventCounterOverAdder<T> {
    public EventCounterOverAdder(ScheduledExecutorService executorService) {
        super(executorService);
    }

    @Override
    public long getQuantityOfEventsInTheLastMinute() {
        return super.getQuantityOfEventsInTheLastMinute() + getQuantityInThisMoment();
    }

    @Override
    public long getQuantityOfEventsInTheLastHour() {
        return super.getQuantityOfEventsInTheLastHour() + getQuantityInThisMoment();
    }

    @Override
    public long getQuantityOfEventsInTheLastDay() {
        return super.getQuantityOfEventsInTheLastDay() + getQuantityInThisMoment();
    }
}
