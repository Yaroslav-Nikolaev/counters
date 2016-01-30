package ru.ya.counters.concurrent;

import ru.ya.counters.CountedEvent;
import ru.ya.counters.EventCounter;

import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * The implementation of {@link EventCounter}, which use series of counting results splitted by time.
 * This implementation suggests granularity of data, hence error in an accurate result.
 * @param <T>
 */
public abstract class SeriesEventCounter<T extends CountedEvent> implements EventCounter<T> {
    private final static int SINGLE_TIME_UNIT = 1;
    private final static TimeUnit DEFAULT_GRANULATION = TimeUnit.SECONDS;

    private final Deque<Long> countSeries = new LinkedBlockingDeque<>((int) TimeUnit.DAYS.toSeconds(1));
    private final TimeUnit activeTimeUnit;
    private final int activeInterval;


    public SeriesEventCounter(ScheduledExecutorService executorService) {
        this(executorService, DEFAULT_GRANULATION, SINGLE_TIME_UNIT);
    }

    //todo realize ability to change granularity in future(during task in doesn't need)
    private SeriesEventCounter(ScheduledExecutorService executorService, TimeUnit unit, int interval) {
        activeTimeUnit = unit;
        activeInterval = interval;
        executorService.scheduleAtFixedRate(new CounterSlicer(), 0, interval, unit);
    }

    @Override
    public long getQuantityOfEventsInTheLastMinute() {
        return getQuantityOfEventsInTheLastInterval(TimeUnit.MINUTES, SINGLE_TIME_UNIT);
    }

    @Override
    public long getQuantityOfEventsInTheLastHour() {
        return getQuantityOfEventsInTheLastInterval(TimeUnit.HOURS, SINGLE_TIME_UNIT);
    }

    @Override
    public long getQuantityOfEventsInTheLastDay() {
        return getQuantityOfEventsInTheLastInterval(TimeUnit.DAYS, SINGLE_TIME_UNIT);
    }

    //todo range method in future (during task in doesn't need)
    protected long getQuantityOfEventsInTheLastInterval(TimeUnit unit, int interval) {
        long historyLength = unit.toSeconds(interval);
        return countSeries.stream().limit(historyLength).reduce(0l, (sum, element) -> sum + element, (sum1, sum2) -> sum1 + sum2);
    }

    private void addToSeries(Long quantityOfEventsInInterval) {
        boolean isAdded = countSeries.offerFirst(quantityOfEventsInInterval);
        while (!isAdded) {
            countSeries.removeLast();
            isAdded = countSeries.offerFirst(quantityOfEventsInInterval);
        }
    }

    protected abstract Long getQuantityInPeriod();


    private class CounterSlicer implements Runnable {

        @Override
        public void run() {
            Long quantityOfEventsInInterval = getQuantityInPeriod();
            addToSeries(quantityOfEventsInInterval);
        }

    }

}
