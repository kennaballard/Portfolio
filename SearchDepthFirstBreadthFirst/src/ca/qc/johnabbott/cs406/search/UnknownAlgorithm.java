/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package ca.qc.johnabbott.cs406.search;

/**
 * Thrown when an non-existent algorithm is specified.
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 * @since 2017-02-09
 */
public class UnknownAlgorithm extends Exception {
    public UnknownAlgorithm(String message) {
        super(message);
    }
}
