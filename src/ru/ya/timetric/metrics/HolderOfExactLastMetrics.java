package ru.ya.timetric.metrics;

import ru.ya.timetric.reducers.Reducer;

import java.util.concurrent.TimeUnit;

/**
 * Implementation of {@link ru.ya.timetric.metrics.LastMetricsOfTimeSeries}.
 * Reduce cpu usage although spend more memory when {@link }, by caching results;
 * Presents more accurate results then {@link HolderOfInexactLastMetrics}
 *
 * @param <S> type of accumulated value
 */
public class HolderOfExactLastMetrics<S> extends HolderOfInexactLastMetrics<S> {

    public HolderOfExactLastMetrics(SlicedValue<S> slicedValue, Reducer<S> reducer) {
        super(slicedValue, reducer);
    }

    @Override
    S getAccumulatedValueForTheLastInterval(TimeUnit unit, int interval) {
        S result = super.getAccumulatedValueForTheLastInterval(unit, interval);
        S currentValue = getCurrentSliceValue();
        return getReducer().getCombiner().apply(result, currentValue);
    }
}
