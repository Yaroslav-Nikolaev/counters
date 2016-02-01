package ru.ya.counters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.ya.counters.concurrent.WeakEventCounterOverAdder;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;

@RunWith(Parameterized.class)
public class CounterOverAdderTest {
    private final static CountedEvent sample = new CountedEvent() {
    };

    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private long quantity;


    public CounterOverAdderTest(long quantity) {
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
        WeakEventCounterOverAdder<CountedEvent> eventCounter = new WeakEventCounterOverAdder<>(scheduledExecutorService);
        runTesters(eventCounter);
        TimeUnit.SECONDS.sleep(1);
        assert eventCounter.getQuantityOfEventsInTheLastMinute() == quantity;
        assert eventCounter.getQuantityOfEventsInTheLastHour() == quantity;
        TimeUnit.MINUTES.sleep(1);
        assert eventCounter.getQuantityOfEventsInTheLastMinute() != quantity;
        assert eventCounter.getQuantityOfEventsInTheLastHour() == quantity;
    }

    @Test
    public void testOfChangingHourQuantityOfEvents() throws InterruptedException {
        WeakEventCounterOverAdder<CountedEvent> eventCounter = new WeakEventCounterOverAdder<>(scheduledExecutorService);
        runTesters(eventCounter);
        assert eventCounter.getQuantityOfEventsInTheLastMinute() == quantity;
        assert eventCounter.getQuantityOfEventsInTheLastHour() == quantity;
        TimeUnit.MINUTES.sleep(1);
        runTesters(eventCounter);
        assert eventCounter.getQuantityOfEventsInTheLastMinute() == quantity;
        assert eventCounter.getQuantityOfEventsInTheLastHour() == quantity * 2;
    }

    private void runTesters(final EventCounter<CountedEvent> counter) {
        LongStream.range(0, quantity).parallel().forEach(i -> counter.countEvent(sample));
    }
}
