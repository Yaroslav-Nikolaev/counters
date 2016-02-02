package ru.ya.timetric.reducers;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public abstract class Reducer<U, T> {
    private final U identity;
    private final BinaryOperator<U> combiner;
    private final BiFunction<U, ? super T, U> accumulator;

    public Reducer(U identity, BinaryOperator<U> combiner, BiFunction<U, ? super T, U> accumulator) {
        this.identity = identity;
        this.combiner = combiner;
        this.accumulator = accumulator;
    }

    public abstract U getIdentityClone();

    protected U getIdentity() {
        return identity;
    }

    public BinaryOperator<U> getCombiner() {
        return combiner;
    }

    public BiFunction<U, ? super T, U> getAccumulator() {
        return accumulator;
    }
}
