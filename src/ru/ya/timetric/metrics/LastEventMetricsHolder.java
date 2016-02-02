package ru.ya.timetric.metrics;

public interface LastEventMetricsHolder {

    long getQuantityOfEventsInTheLastMinute();

    long getQuantityOfEventsInTheLastHour();

    long getQuantityOfEventsInTheLastDay();
}
