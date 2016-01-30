package ru.ya.counters.concurrent;

import ru.ya.counters.EventCounter;
import ru.ya.counters.CountedEvent;

import java.util.Deque;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 *
 * @param <T>
 */
public abstract class SeriesEventCounter<T extends CountedEvent> implements EventCounter<T> {
    //in real application the executor won't be here.
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final Deque<Long> countSeries = new LinkedBlockingDeque<>((int) TimeUnit.DAYS.toSeconds(1));

    public SeriesEventCounter() {
        executorService.scheduleAtFixedRate(new Refresher(), 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public long getQuantityOfEventsInTheLastMinute() {
        return getQuantityOfEventsInInterval(TimeUnit.MINUTES, 1);
    }

    @Override
    public long getQuantityOfEventsInTheLastHour() {
        return getQuantityOfEventsInInterval(TimeUnit.HOURS, 1);
    }

    @Override
    public long getQuantityOfEventsInTheLastDay() {
        return getQuantityOfEventsInInterval(TimeUnit.DAYS, 1);
    }

    private long getQuantityOfEventsInInterval(TimeUnit unit, int interval) {
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


    private class Refresher implements Runnable {

        @Override
        public void run() {
            Long quantityOfEventsInInterval = getQuantityInPeriod();
            addToSeries(quantityOfEventsInInterval);
        }

    }

}
