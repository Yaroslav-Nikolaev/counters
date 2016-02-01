package ru.ya.counters;

public interface LastEventMetricsHolder {

    long getQuantityOfEventsInTheLastMinute();

    long getQuantityOfEventsInTheLastHour();

    long getQuantityOfEventsInTheLastDay();
}
