package ru.ya.timetric.metrics;

/**
 * Provide access to accumulated value from time series for the chosen interval of time.
 */
public interface LastMetricsOfTimeSeries<S> {

    S getAccumulatedValueForTheLastMinute();

    S getAccumulatedValueForTheLastHour();

    S getAccumulatedValueForTheLastDay();
}
