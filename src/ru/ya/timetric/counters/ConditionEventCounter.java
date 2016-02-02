package ru.ya.timetric.counters;

import java.util.function.Predicate;

public class ConditionEventCounter<T extends CountedEvent> implements EventCounter<T> {
    private final EventCounter<T> delegate;
    private final Predicate<T> condition;

    public ConditionEventCounter(EventCounter<T> delegate, Predicate<T> condition) {
        this.delegate = delegate;
        this.condition = condition;
    }

    @Override
    public void countEvent(T event) {
        if (condition.test(event)) {
            delegate.countEvent(event);
        }
    }
}
