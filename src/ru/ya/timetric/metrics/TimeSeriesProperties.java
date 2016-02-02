package ru.ya.timetric.metrics;

import java.util.concurrent.TimeUnit;

public class TimeSeriesProperties {
    private final int lengthOfSeries;
    private final int timeInterval;
    private final TimeUnit intervalUnit;

    public TimeSeriesProperties(int lengthOfSeries, int timeInterval, TimeUnit intervalUnit) {
        //todo asserts
        this.lengthOfSeries = lengthOfSeries;
        this.timeInterval = timeInterval;
        this.intervalUnit = intervalUnit;
    }

    public int getLengthOfSeries() {
        return lengthOfSeries;
    }

    public int getTimeInterval() {
        return timeInterval;
    }

    public TimeUnit getIntervalUnit() {
        return intervalUnit;
    }
}
