/*
 * Copyright (c) 2018 Ian Clement.  All rights reserved.
 */

package ca.qc.johnabbott.cs406.generator;

import java.util.Random;

/**
 * Interface for random value generator.
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 * @since 2018-02-13
 */
public interface Generator<T> {

    /**
     * Generate a random value.
     * @param random the random number generator.
     * @return a random value.
     */
    T generate(Random random);
}
