package ru.ya.timetric.metrics;

import ru.ya.timetric.reducers.Reducer;

import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

/**
 * Implementation of {@link ru.ya.timetric.metrics.LastMetricsOfTimeSeries}.
 * Reduce cpu usage although spend more memory when {@link }, by caching results;
 * There is an error which allow reduce reads hence increase work speed.
 * If you want to have more accurate implementation, use {@link HolderOfExactLastMetrics}
 * @param <S> type of accumulated value
 */
public class HolderOfInexactLastMetrics<S> extends TimeSeriesForSlicedValues<S> implements LastMetricsOfTimeSeries<S> {
    private final static int SINGLE_TIME_UNIT = 1;

    private final Reducer<S> reducer;

    private volatile S quantityOfEventsInTheLastMinute;
    private volatile S quantityOfEventsInTheLastHour;
    private volatile S quantityOfEventsInTheLastDay;

    //todo implement flexible time series properties
    private final static TimeSeriesProperties DEFAULT_TIME_SERIES_PROPERTIES = new TimeSeriesProperties((int) TimeUnit.DAYS.toSeconds(1), 1, TimeUnit.SECONDS);

    public HolderOfInexactLastMetrics(SlicedValue<S> slicedValue, Reducer<S> reducer) {
        super(DEFAULT_TIME_SERIES_PROPERTIES, slicedValue);
        this.reducer = reducer;
    }

    private HolderOfInexactLastMetrics(TimeSeriesProperties timeSeriesProperties, SlicedValue<S> slicedValue, Reducer<S> reducer) {
        super(timeSeriesProperties, slicedValue);
        this.reducer = reducer;
    }

    @Override
    protected void addToSeries(S quantityOfEventsInInterval) {
        super.addToSeries(quantityOfEventsInInterval);
        quantityOfEventsInTheLastMinute = getAccumulatedValueForTheLastInterval(TimeUnit.MINUTES, SINGLE_TIME_UNIT);
        quantityOfEventsInTheLastHour = getAccumulatedValueForTheLastInterval(TimeUnit.HOURS, SINGLE_TIME_UNIT);
        quantityOfEventsInTheLastDay = getAccumulatedValueForTheLastInterval(TimeUnit.DAYS, SINGLE_TIME_UNIT);
    }

    //todo write one more abstaction which will hide an wrapper functions over streams.
    S getAccumulatedValueForTheLastInterval(TimeUnit unit, int interval) {
        //todo rewrite when will be rewriten DEFAULT_TIME_SERIES_PROPERTIES -> do it in TimeSeriesForSlicedValues!
        long historyLength = unit.toSeconds(interval);
        S identity = reducer.getIdentityClone();
        BinaryOperator<S> combiner = reducer.getCombiner();
        BiFunction<S, ? super S, S> accumulator = reducer.getAccumulator();
        return getTimeSeries().stream().limit(historyLength).reduce(identity, accumulator, combiner);
    }

    @Override
    public S getAccumulatedValueForTheLastMinute() {
        return quantityOfEventsInTheLastMinute;
    }

    @Override
    public S getAccumulatedValueForTheLastHour() {
        return quantityOfEventsInTheLastHour;
    }

    @Override
    public S getAccumulatedValueForTheLastDay() {
        return quantityOfEventsInTheLastDay;
    }

    protected Reducer<S> getReducer() {
        return reducer;
    }
}
