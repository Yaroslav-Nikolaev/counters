package ru.ya.timetric.metrics;


public interface SlicedValue<T> {

    T getCurrentSlice();

    T getCurrentSliceAndReset();
}
