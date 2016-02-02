package ru.ya.timetric.reducers;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public class LongReducer extends Reducer<Long, Long> {

    public static final LongReducer INSTANCE = new LongReducer();
    private static final Long IDENTITY = 0l;
    private static final BinaryOperator<Long> combiner = (sum, element) -> sum + element;
    private static final BiFunction<Long, ? super Long, Long> accumulator = (sum, element) -> sum + element;

    private LongReducer() {
        super(IDENTITY, combiner, accumulator);
    }

    @Override
    public Long getIdentityClone() {
        return 0l;
    }


}
