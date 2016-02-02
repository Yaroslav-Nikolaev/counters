package ru.ya.timetric.metrics;

import ru.ya.timetric.reducers.Reducer;

import java.util.concurrent.TimeUnit;

/**
 * Implementation of {@link ru.ya.timetric.metrics.LastMetricsOfTimeSeries}.
 * Reduce cpu usage although spend more memory when {@link }, by caching results;
 * Presents more accurate results then {@link HolderOfInexactLastMetrics}
 * @param <S> type of accumulated value
 */
public class HolderOfExactLastMetrics<S> extends HolderOfInexactLastMetrics<S> {
    public HolderOfExactLastMetrics(TimeSeriesProperties timeSeriesProperties, SlicedValue<S> slicedValue, Reducer<S, S> reducer) {
        super(timeSeriesProperties, slicedValue, reducer);
    }

    @Override
    S getQuantityOfEventsInTheLastInterval(TimeUnit unit, int interval) {
        S result = super.getQuantityOfEventsInTheLastInterval(unit, interval);
        S currentValue = getCurrentSliceValue();
        return getReducer().getCombiner().apply(result, currentValue);
    }
}
