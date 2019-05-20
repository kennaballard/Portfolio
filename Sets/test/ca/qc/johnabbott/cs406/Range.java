/*
 * Copyright (c) 2018 Ian Clement.  All rights reserved.
 */

package ca.qc.johnabbott.cs406;

import java.util.Iterator;

/**
 * Represents a range of integers between low (inclusive) and high (exclusive).
 * Iterable, so we can use in foreach loops.
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 * @since 2018-02-03
 */
public class Range implements Iterable<Integer> {

    private int low;
    private int high;

    public Range(int low, int high) {
        this.low = low;
        this.high = high;
    }

    public Range(int high) {
        this(0, high);
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {

            private int current = low;

            @Override
            public boolean hasNext() {
                return current < high;
            }

            @Override
            public Integer next() {
                return current++;
            }
        };
    }
}
