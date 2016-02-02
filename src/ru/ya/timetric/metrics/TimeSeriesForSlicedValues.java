package ru.ya.timetric.metrics;

import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

//todo create new one abstraction which will hide time series implementation!
//todo try implementations with EvictingQueue (guava) and  CircularFifoBuffer(apache)
class TimeSeriesForSlicedValues<S> {
    private final Deque<S> timeSeries;
    private final SlicedValue<S> slicedValue;
    private final TimeSeriesProperties timeSeriesProperties;
    private final AtomicBoolean isStarted = new AtomicBoolean(false);

    public TimeSeriesForSlicedValues(TimeSeriesProperties timeSeriesProperties, SlicedValue<S> slicedValue) {
        assert timeSeriesProperties != null;
        assert slicedValue != null;
        this.slicedValue = slicedValue;
        this.timeSeriesProperties = timeSeriesProperties;
        this.timeSeries = new LinkedBlockingDeque<>(timeSeriesProperties.getLengthOfSeries());
    }

    public void start(ScheduledExecutorService executorService) {
        if (isStarted.compareAndSet(false, true)) {
            Slicer slicer = new Slicer<>(this);
            executorService.scheduleAtFixedRate(slicer, 0, timeSeriesProperties.getTimeInterval(), timeSeriesProperties.getIntervalUnit());
        } else {
            throw new RuntimeException("Time Series can be started more when one time");
        }
    }

    protected void addToSeries(S quantityOfEventsInInterval) {
        boolean isAdded = timeSeries.offerFirst(quantityOfEventsInInterval);
        while (!isAdded) {
            timeSeries.removeLast();
            isAdded = timeSeries.offerFirst(quantityOfEventsInInterval);
        }
    }

    public S getCurrentSliceValue() {
        return slicedValue.getCurrentSlice();
    }

    Deque<S> getTimeSeries() {
        return timeSeries;
    }

    private class Slicer<T> implements Runnable {
        private final TimeSeriesForSlicedValues<T> timeSeriesForSlicedValues;

        public Slicer(TimeSeriesForSlicedValues<T> timeSeriesForSlicedValues) {
            this.timeSeriesForSlicedValues = timeSeriesForSlicedValues;
        }

        @Override
        public void run() {
            T quantityOfEventsInInterval = timeSeriesForSlicedValues.slicedValue.getCurrentSliceAndReset();
            timeSeriesForSlicedValues.addToSeries(quantityOfEventsInInterval);
        }
    }
}
