package ru.ya.timetric.metrics;

import java.util.concurrent.TimeUnit;

public class ExactLastMetricsHolder extends InexactLastMetricsHolder {
    public ExactLastMetricsHolder(TimeSeriesProperties timeSeriesProperties, SlicedValue<Long> slicedValue) {
        super(timeSeriesProperties, slicedValue);
    }

    @Override
    long getQuantityOfEventsInTheLastInterval(TimeUnit unit, int interval) {
        long result = super.getQuantityOfEventsInTheLastInterval(unit, interval);
        long currentValue = getCurrentSliceValue();
        return result + currentValue;
    }
}
