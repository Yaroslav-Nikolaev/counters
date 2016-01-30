package ru.ya.counters;

public interface EventCounter<T extends CountedEvent> {

    void countEvent(T event);

    long getQuantityOfEventsInTheLastMinute();

    long getQuantityOfEventsInTheLastHour();

    long getQuantityOfEventsInTheLastDay();


}
