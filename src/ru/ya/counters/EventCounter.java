package ru.ya.counters;

public interface EventCounter<T extends CountedEvent> {

    void count(T event);

    long getQuantityOfEventsInTheLastMinute();

    long getQuantityOfEventsInTheLastHour();

    long getQuantityOfEventsInTheLastDay();


}
