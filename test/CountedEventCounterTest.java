import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.ya.counters.EventCounter;
import ru.ya.counters.concurrent.EventCounterOverAdder;
import ru.ya.counters.concurrent.EventCounterOverAtomic;
import ru.ya.counters.CountedEvent;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;

@RunWith(Parameterized.class)
public class CountedEventCounterTest {
    private final static CountedEvent sample = new CountedEvent() {
    };

    private ExecutorService testers;

    private long quantity;
    private int poolSize;


    public CountedEventCounterTest(long quantity, int poolSize) {
        this.quantity = quantity;
        this.poolSize = poolSize;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> params() {
        Object[][] data = new Object[][]
                {
                        {1000000, Runtime.getRuntime().availableProcessors()},
                        {2000000, Runtime.getRuntime().availableProcessors() * 2},
                        {3000000, Runtime.getRuntime().availableProcessors() * 3}
                };
        return Arrays.asList(data);
    }

    @Before
    public void init() {
        testers = Executors.newFixedThreadPool(poolSize);
    }

    @After
    public void shutdown() {
        testers.shutdown();
    }

    @Test
    public void testCountingAdder() throws InterruptedException {
        long start = System.nanoTime();
        EventCounter<CountedEvent> eventCounter = new EventCounterOverAdder();
        runTesters(eventCounter);
        TimeUnit.SECONDS.sleep(1);
        assert eventCounter.getQuantityOfEventsInTheLastMinute() == quantity;
        System.out.println("Adder Test time:" + (System.nanoTime() - start));
    }

    @Test
    public void testCountingOverTimeAdder() throws InterruptedException {
        long start = System.nanoTime();
        EventCounter<CountedEvent> eventCounter = new EventCounterOverAdder();
        runTesters(eventCounter);
        assert eventCounter.getQuantityOfEventsInTheLastMinute() == quantity;
        TimeUnit.MINUTES.sleep(1);
        runTesters(eventCounter);
        assert eventCounter.getQuantityOfEventsInTheLastMinute() == quantity;
        assert eventCounter.getQuantityOfEventsInTheLastHour() == quantity * 2;
        System.out.println("Adder Test time:" + (System.nanoTime() - start));
    }

    @Test
    public void testCountingAtomic() throws InterruptedException {
        long start = System.nanoTime();
        EventCounter<CountedEvent> eventCounter = new EventCounterOverAtomic();
        runTesters(eventCounter);
        TimeUnit.SECONDS.sleep(1);
        assert eventCounter.getQuantityOfEventsInTheLastMinute() == quantity;
        System.out.println("Atomic Test time:" + (System.nanoTime() - start));
    }

    @Test
    public void testCountingOverTimeAtomic() throws InterruptedException {
        long start = System.nanoTime();
        EventCounter<CountedEvent> eventCounter = new EventCounterOverAtomic();
        runTesters(eventCounter);
        assert eventCounter.getQuantityOfEventsInTheLastMinute() == quantity;
        TimeUnit.MINUTES.sleep(1);
        runTesters(eventCounter);
        assert eventCounter.getQuantityOfEventsInTheLastMinute() == quantity;
        assert eventCounter.getQuantityOfEventsInTheLastHour() == quantity * 2;
        System.out.println("Atomic Test time:" + (System.nanoTime() - start));
    }

    private void runTesters(final EventCounter<CountedEvent> counter) {
        LongStream.range(0, quantity).forEach(i -> testers.submit(() -> counter.count(sample)));
    }

}
