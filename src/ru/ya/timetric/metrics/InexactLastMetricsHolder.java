package ru.ya.timetric.metrics;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BinaryOperator;

public class InexactLastMetricsHolder extends TimeSeriesForSlicedValues<Long> implements LastEventMetricsHolder {
    private final static int SINGLE_TIME_UNIT = 1;
    private static final long ZERO = 0;
    private final BinaryOperator<Long> slicedValuesAccumulator = (sum1, sum2) -> sum1 + sum2;

    private volatile long quantityOfEventsInTheLastMinute = ZERO;
    private volatile long quantityOfEventsInTheLastHour = ZERO;
    private volatile long quantityOfEventsInTheLastDay = ZERO;

    public InexactLastMetricsHolder(TimeSeriesProperties timeSeriesProperties, SlicedValue<Long> slicedValue) {
        super(timeSeriesProperties, slicedValue);
    }

    @Override
    protected void addToSeries(Long quantityOfEventsInInterval) {
        super.addToSeries(quantityOfEventsInInterval);
        quantityOfEventsInTheLastMinute = getQuantityOfEventsInTheLastInterval(TimeUnit.MINUTES, SINGLE_TIME_UNIT);
        quantityOfEventsInTheLastHour = getQuantityOfEventsInTheLastInterval(TimeUnit.HOURS, SINGLE_TIME_UNIT);
        quantityOfEventsInTheLastDay = getQuantityOfEventsInTheLastInterval(TimeUnit.DAYS, SINGLE_TIME_UNIT);
    }

    long getQuantityOfEventsInTheLastInterval(TimeUnit unit, int interval) {
        long historyLength = unit.toSeconds(interval);
        Optional<Long> result = getTimeSeries().stream().limit(historyLength).reduce(slicedValuesAccumulator);
        return result.isPresent() ? result.get() : ZERO;
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
}
