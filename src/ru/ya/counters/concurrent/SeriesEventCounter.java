package ru.ya.counters.concurrent;

import ru.ya.counters.CountedEvent;
import ru.ya.counters.EventCounter;
import ru.ya.counters.LastEventMetricsHolder;

import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * The implementation of {@link EventCounter}, which use series of counting results splitted by time.
 * This implementation suggests granularity of data, hence error in an accurate result.
 *
 * @param <T>
 */
public abstract class SeriesEventCounter<T extends CountedEvent> implements EventCounter<T>, LastEventMetricsHolder {
    private final static int SINGLE_TIME_UNIT = 1;
    private final static TimeUnit DEFAULT_GRANULATION = TimeUnit.SECONDS;
    private final static int QUANTITY_OF_GRANULE = (int) TimeUnit.DAYS.toSeconds(1);

    private final Deque<Long> countSeries;

    //deliberately data race!
    private long quantityOfEventsInTheLastMinute = 0;
    private long quantityOfEventsInTheLastHour = 0;
    private long quantityOfEventsInTheLastDay = 0;


    public SeriesEventCounter(ScheduledExecutorService executorService) {
        this(executorService, DEFAULT_GRANULATION, SINGLE_TIME_UNIT, QUANTITY_OF_GRANULE);
    }

    //todo realize ability to change granularity in future(during task in doesn't need)
    private SeriesEventCounter(ScheduledExecutorService executorService, TimeUnit unit, int interval, int quantityOfGranule) {
        countSeries = new LinkedBlockingDeque<>(quantityOfGranule);
        CountSlicer countSlicer = new CountSlicer(this);
        executorService.scheduleAtFixedRate(countSlicer, 0, interval, unit);
    }

    @Override
    public long getQuantityOfEventsInTheLastMinute() {
        return quantityOfEventsInTheLastMinute;
    }

    @Override
    public long getQuantityOfEventsInTheLastHour() {
        return quantityOfEventsInTheLastHour;
    }

    @Override
    public long getQuantityOfEventsInTheLastDay() {
        return quantityOfEventsInTheLastDay;
    }

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
        quantityOfEventsInTheLastMinute = getQuantityOfEventsInTheLastInterval(TimeUnit.MINUTES, SINGLE_TIME_UNIT);
        quantityOfEventsInTheLastHour = getQuantityOfEventsInTheLastInterval(TimeUnit.HOURS, SINGLE_TIME_UNIT);
        quantityOfEventsInTheLastDay = getQuantityOfEventsInTheLastInterval(TimeUnit.DAYS, SINGLE_TIME_UNIT);
    }

    protected abstract Long getQuantityInPeriodAndReset();


    private class CountSlicer implements Runnable {
        private final SeriesEventCounter eventCounter;

        public CountSlicer(SeriesEventCounter eventCounter) {
            this.eventCounter = eventCounter;
        }

        @Override
        public void run() {
            Long quantityOfEventsInInterval = eventCounter.getQuantityInPeriodAndReset();
            eventCounter.addToSeries(quantityOfEventsInInterval);
        }

    }

}
