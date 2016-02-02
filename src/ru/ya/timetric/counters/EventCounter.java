package ru.ya.timetric.counters;

public interface EventCounter<T extends CountedEvent> {

    void countEvent(T event);

}
