package ru.ya.timetric;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.ya.timetric.metrics.ExactLastMetricsHolder;
import ru.ya.timetric.counters.SlicedEventCounterOverAtomic;
import ru.ya.timetric.metrics.TimeSeriesProperties;
import ru.ya.timetric.counters.CountedEvent;
import ru.ya.timetric.counters.EventCounter;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;

@RunWith(Parameterized.class)
public class CounterOverAtomicTest {
    private final static CountedEvent sample = new CountedEvent() {
    };

    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private TimeSeriesProperties timeSeriesProperties = new TimeSeriesProperties((int) TimeUnit.DAYS.toSeconds(1), 1, TimeUnit.SECONDS);

    private long quantity;


    public CounterOverAtomicTest(long quantity) {
        this.quantity = quantity;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> params() {
        Object[][] data = new Object[][]
                {
                        {10000}, {20000}, {30000}
                };
        return Arrays.asList(data);
    }

    @Test
    public void testOfChangingMinutesQuantityOfEvents() throws InterruptedException {
        SlicedEventCounterOverAtomic<CountedEvent> eventCounter = new SlicedEventCounterOverAtomic<>();
        ExactLastMetricsHolder metricsHolder = new ExactLastMetricsHolder(timeSeriesProperties, eventCounter);
        metricsHolder.start(scheduledExecutorService);
        runTesters(eventCounter);
        TimeUnit.SECONDS.sleep(1);
        assert metricsHolder.getQuantityOfEventsInTheLastMinute() == quantity;
        assert metricsHolder.getQuantityOfEventsInTheLastHour() == quantity;
        TimeUnit.MINUTES.sleep(1);
        assert metricsHolder.getQuantityOfEventsInTheLastMinute() != quantity;
        assert metricsHolder.getQuantityOfEventsInTheLastHour() == quantity;
    }

    @Test
    public void testOfChangingHourQuantityOfEvents() throws InterruptedException {
        SlicedEventCounterOverAtomic<CountedEvent> eventCounter = new SlicedEventCounterOverAtomic<>();
        ExactLastMetricsHolder metricsHolder = new ExactLastMetricsHolder(timeSeriesProperties, eventCounter);
        metricsHolder.start(scheduledExecutorService);
        runTesters(eventCounter);
        assert metricsHolder.getQuantityOfEventsInTheLastMinute() == quantity;
        assert metricsHolder.getQuantityOfEventsInTheLastHour() == quantity;
        TimeUnit.MINUTES.sleep(1);
        runTesters(eventCounter);
        assert metricsHolder.getQuantityOfEventsInTheLastMinute() == quantity;
        assert metricsHolder.getQuantityOfEventsInTheLastHour() == quantity * 2;
    }

    private void runTesters(final EventCounter<CountedEvent> counter) {
        LongStream.range(0, quantity).parallel().forEach(i -> counter.countEvent(sample));
    }

}
