/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package com.company;

import java.text.ParseException;

/**
 * A rule in Porter's stemming algorithm.
 */
class Rule {

    private String prefixTestString;
    private PrefixTest prefixTest;
    private String suffix;
    private String replacement;

    /**
     * Create a rule.
     * @param prefixTest the test applied to the prefix when matching.
     * @param suffix the suffix that need to match the input for the rule to apply.
     * @param replacement the replacement that will be applied to an input that matches the rule.
     * @throws ParseException
     */
    public Rule(String prefixTest, String suffix, String replacement) throws ParseException {
        this.prefixTestString = prefixTest;
        this.prefixTest = PrefixTestFactory.parse(prefixTest);
        this.suffix = suffix;
        this.replacement = replacement;
    }

    /**
     * Test that the input matches the rule, i.e.: the input ends with the suffix
     * and the prefix passes the test.
     * @param input the input string to test.
     * @return whether the rule applies to this input.
     */
    public boolean matches(String input) {

        // eliminate impossible inputs.
        if(input.length() < suffix.length())
            return false;

        // find the position in the input that splits the string into prefix-suffix
        int suffixPos = input.lastIndexOf(suffix);

        // test that the input actually has the suffix, and if so, that the prefix passes the test.
        return (suffixPos + suffix.length() == input.length()) && prefixTest.test(input, suffixPos-1);
    }

    /**
     * Replace the suffix with the replacement text.
     * @param input the input string to replace.
     * @return the input with replaced suffix.
     */
    public String replace(String input) {
        int suffixPos = input.lastIndexOf(suffix);
        return input.substring(0, suffixPos) + (replacement == null ? "" : replacement);
    }

    @Override
    public String toString() {
        return prefixTestString + " " + suffix + " => " + replacement;
    }
}


