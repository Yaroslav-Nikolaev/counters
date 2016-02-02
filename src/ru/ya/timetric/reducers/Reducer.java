package ru.ya.timetric.reducers;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public abstract class Reducer<U> {
    private final U identity;
    private final BinaryOperator<U> combiner;
    private final BiFunction<U, ? super U, U> accumulator;

    public Reducer(U identity, BinaryOperator<U> combiner, BiFunction<U, ? super U, U> accumulator) {
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

    public BiFunction<U, ? super U, U> getAccumulator() {
        return accumulator;
    }
}
