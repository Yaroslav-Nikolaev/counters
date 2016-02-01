package ru.ya.counters;

public interface EventCounter<T extends CountedEvent> {

    void countEvent(T event);

}
