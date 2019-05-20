/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package com.company;

/**
 * Outlines the structure of a word stemmer.
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 * @since 2018-03-16
 */
public interface Stemmer {

    /**
     * Stem the input word if possible.
     * @param input the input to stem.
     * @return the word stem or the word itself if already stemmed.
     */
    String stem(String input);
}
