/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package com.company;

/**
 * This interface describes the prefix tests needed for Porter's stemming algorithm
 * (http://tartarus.org/martin/PorterStemmer/def.txt).
 *
 * All tests are created by PrefixTestFactory
 *
 * @author Ian Clement
 */
interface PrefixTest {
    /**
     * Run the test on the provided prefix.
     * @param input the string containing the prefix to test.
     * @param prefixEnds the index where the prefix ends (inclusive).
     * @return the result of the test.
     */
    boolean test(String input, int prefixEnds);
}
