/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package com.company;

import java.text.ParseException;
import java.util.Arrays;

/**
 * This factory class will create the prefix tests needed for Porter's stemming
 * algorithm (http://tartarus.org/martin/PorterStemmer/def.txt).
 *
 * Usage:
 *
 *   PrefixTest prefixTest = PrefixTestFactory.parse("ENDS_WITH(l)");
 *   if(prefixTest.test("hello", 3))
 *       ...
 *   if(prefixTest.test("world", 2))
 *       ...
 *
 *   Supported test "predicates" include:
 *
 *   1.  NONE: always true.
 *   2.  ENDS_WITH(c): the prefix ends with character 'c'
 *   3.  HAS_VOWEL: the prefix contains at least one vowel.
 *   4.  MEASURE([op]i): the prefix contains a measure that is compared to i using [op]
 *                   [op] is either '<', '>' or '='
 *                   the measure is define as `m` in the following pattern
 *
 *                      [C]{CV}^m[V]
 *
 *                   where C is a sequence of one or more consonants
 *                         V is a sequence of one or more vowels
 *
 *   5.  AND(e1,e2): the conjunction of two sub-tests.
 *   6.  OR(e1,e2): the disjunction of two sub-tests.
 *   7.  NOT(e): the negation of a sub-test.
 *   8.  ENDS_CVC: the prefix ends in with [consonant][vowel][consonant]
 *
 *  @author Ian Clement
 *  @version 2
 */
class PrefixTestFactory {

    /**
     * Parse the expresion as return a prefix test.
     * @param expr the expression to parse.
     * @return The prefix test corresponding to the expression.
     * @throws ParseException
     */
    public static PrefixTest parse(String expr) throws ParseException {

        // get the position of the outermost parentheses.
        int lParenPos = expr.indexOf('(');
        int rParenPos = expr.lastIndexOf(')');

        // extract the predicate and the the argument list (no validation)
        String predicate = lParenPos >= 0 ? expr.substring(0, lParenPos) : expr;
        String arguments = rParenPos >= 0 ? expr.substring(lParenPos + 1, rParenPos) : "";

        // create the appropriate test based on the predicate.
        switch (predicate) {

            case "NONE":
                if(arguments.length() != 0)
                    throw new ParseException("NONE expects no arguments.", -1);
                return new NoneTest();

            case "HAS_VOWEL":
                if(arguments.length() != 0)
                    throw new ParseException("HAS_VOWEL expects no arguments.", -1);
                return new HasVowelTest();

            case "MEASURE":
                return new MeasureTest(arguments);

            case "ENDS_WITH":
                return new EndsWithTest(arguments);

            case "AND":
                return new AndTest(arguments);

            case "OR":
                return new OrTest(arguments);

            case "NOT":
                return new NotTest(arguments);

            case "DOUBLE_CONSONANT":
                return new DoubleConsonantTest();

            case "ENDS_CVC":
                return new EndsCVCTest();
        }

        throw new ParseException("Unrecognized predicate: " + predicate, -1);
    }

    /**
     * Check that a char is a consonant.
     * @param c
     * @return
     */
    private static boolean isConsonant(char c) {
        return Character.isAlphabetic(c) && !isVowel(c);
    }

    /**
     * Check that a char is a vowel
     * @param c
     * @return
     */
    private static boolean isVowel(char c) {
        c = Character.toLowerCase(c);
        return Character.isAlphabetic(c) && (
                c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u'
        );
    }


    /**
     * A test that always returns true.
     */
    private static class NoneTest implements PrefixTest {
        @Override
        public boolean test(String input, int prefixEnds) {
            return true;
        }

        @Override
        public String toString() {
            return "NONE";
        }
    }

    /**
     * A test that checks the measure of the prefix (measure described above).
     */
    private static class MeasureTest implements PrefixTest {

        private enum Comparaison {
            LT, EQ, GT;

            @Override
            public String toString() {
                switch (this) {
                    case LT:
                        return "<";
                    case GT:
                        return ">";
                    case EQ:
                        return "=";
                }
                return "";
            }
        }

        private Comparaison comparaison;
        private int measure;

        public MeasureTest(String arg) throws ParseException {

            if(arg.length()<2)
                throw new ParseException("MEASURE requires an argument of the form {op}{number}.",-1);

            // check the first character to determine test
            switch(arg.charAt(0)) {
                case '<':
                    comparaison = Comparaison.LT;
                    break;
                case '>':
                    comparaison = Comparaison.GT;
                    break;
                case '=':
                    comparaison = Comparaison.EQ;
                    break;
                default:
                    throw new ParseException("MEASURE: unknown operator " + arg.charAt(0), -1);
            }

            // advance past the operator
            arg = arg.substring(1);
            try {
                measure = Integer.parseInt(arg);
            } catch (Exception e) {
                throw new ParseException("MEASURE requires an integer argument.", -1);
            }
        }

        @Override
        public boolean test(String input, int prefixEnds) {

            // count will store the actual measure of the input.
            int count = 0;

            // use inConsonant to track the changes between vowel sequences and consonant sequences.
            // each change from vowel sequence to consonant will increase the measure by 1
            boolean inConsonant = true;

            // if the prefix has length >1
            if(prefixEnds > 0) {
                // iterate over the char array
                char[] cs = Arrays.copyOfRange(input.toCharArray(), 0, prefixEnds + 1);
                for (char c : cs) {

                    if (isVowel(c))
                        inConsonant = false;
                    if (isConsonant(c)) {
                        if (!inConsonant)
                            count++;
                        inConsonant = true;
                    }
                }
            }

            // the measure test verifies that the actual measure is <, > or = to the required measure
            switch (comparaison) {
                case LT:
                    return count < measure;
                case EQ:
                    return count == measure;
                case GT:
                    return count > measure;
                default:
                    return false;
            }

        }

        @Override
        public String toString() {
            return "MEASURE(" + comparaison.toString() + measure + ")";
        }
    }

    /**
     * A test that checks if a prefix contains at least one vowel.
     */
    private static class HasVowelTest implements PrefixTest {
        @Override
        public boolean test(String input, int prefixEnds) {
            char[] vowels = { 'a', 'e', 'i', 'o', 'u' };
            for(char vowel : vowels) {
                int vowelPos = input.indexOf(vowel);
                if(vowelPos >= 0 && vowelPos <= prefixEnds)
                    return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return "HAS_VOWEL";
        }
    }

    /**
     * A test that verifies that a prefix ends with specific character
     */
    private static class EndsWithTest implements PrefixTest {

        private char endsWith;

        public EndsWithTest(String arg) throws ParseException {
            if(arg.length() != 1)
                throw new ParseException("ENDS_WITH not given argument.", -1);
            endsWith = arg.charAt(0);
        }

        @Override
        public boolean test(String input, int prefixEnds) {
            return input.charAt(prefixEnds) == endsWith;
        }

        @Override
        public String toString() {
            return "ENDS_WITH(" + endsWith + ")";
        }
    }

    /**
     * A test that combined two sub-test using conjunction.
     */
    private static class AndTest implements PrefixTest {

        private PrefixTest expr1;
        private PrefixTest expr2;

        public AndTest(String arg) throws ParseException {

            // detect the argument separator (,) without catching those of the nested expressions
            int nestedParens = 0;  // only when this is == 0 is the (,) separating the arguments.
            int argumentSeperator = -1;
            for(int i=0; i<arg.length(); i++) {
                switch (arg.charAt(i)) {
                    case '(':
                        nestedParens++;
                        break;
                    case ')':
                        nestedParens--;
                        break;
                    case ',':
                        if(nestedParens == 0)
                            argumentSeperator = i;
                }
            }

            // error if the parentheses are unbalanced or there is no arguement separator
            if(nestedParens != 0 || argumentSeperator < 0)
                throw new ParseException(arg,-1);

            // extract and parse sub-expressions.
            expr1 = PrefixTestFactory.parse( arg.substring(0, argumentSeperator) );
            expr2 = PrefixTestFactory.parse( arg.substring(argumentSeperator + 1) );
        }

        @Override
        public boolean test(String input, int prefixEnds) {
            return expr1.test(input, prefixEnds) && expr2.test(input, prefixEnds);
        }

        @Override
        public String toString() {
            return "AND(" + expr1.toString() + "," + expr2.toString() + ")";
        }
    }

    /**
     * A test that combined two sub-test using disjunction.
     */
    private static class OrTest implements PrefixTest {

        private PrefixTest expr1;
        private PrefixTest expr2;

        public OrTest(String arg) throws ParseException {

            // detect the argument separator (,) without catching those of the nested expressions
            int nestedParens = 0;  // only when this is == 0 is the (,) separating the arguments.
            int argumentSeperator = -1;
            for(int i=0; i<arg.length(); i++) {
                switch (arg.charAt(i)) {
                    case '(':
                        nestedParens++;
                        break;
                    case ')':
                        nestedParens--;
                        break;
                    case ',':
                        if(nestedParens == 0)
                            argumentSeperator = i;
                }
            }

            // error if the parentheses are unbalanced or there is no argument separator
            if(nestedParens != 0 || argumentSeperator < 0)
                throw new ParseException(arg,-1);

            // extract and parse sub-expressions.
            expr1 = PrefixTestFactory.parse( arg.substring(0, argumentSeperator) );
            expr2 = PrefixTestFactory.parse( arg.substring(argumentSeperator + 1) );
        }

        @Override
        public boolean test(String input, int prefixEnds) {
            return expr1.test(input, prefixEnds) || expr2.test(input, prefixEnds);
        }

        @Override
        public String toString() {
            return "OR(" + expr1.toString() + "," + expr2.toString() + ")";
        }
    }

    /**
     * A test that negates a sub-test.
     */
    private static class NotTest implements PrefixTest {
        private PrefixTest expr;

        public NotTest(String arg) throws ParseException {

            // extract and parse sub-expressions.
            expr = PrefixTestFactory.parse( arg );
        }

        @Override
        public boolean test(String input, int prefixEnds) {
            return !expr.test(input, prefixEnds);
        }

        @Override
        public String toString() {
            return "NOT(" + expr.toString() + ")";
        }
    }

    /**
     * Test that the prefix ends in a double consonant.
     */
    private static class DoubleConsonantTest implements PrefixTest {
        @Override
        public boolean test(String input, int prefixEnds) {
            if(prefixEnds < 1)
                return false;
            return (isConsonant(input.charAt(prefixEnds)) && input.charAt(prefixEnds) == input.charAt(prefixEnds-1));
        }

        @Override
        public String toString() {
            return "DOUBLE_CONSONANT";
        }
    }

    /**
     * Test that the prefix ends with a [consonant][vowel][consonent]
     */
    private static class EndsCVCTest implements PrefixTest {

        @Override
        public boolean test(String input, int prefixEnds) {
            if(prefixEnds < 2)
                return false;
            return (isConsonant(input.charAt(prefixEnds-2))
                    && isVowel(input.charAt(prefixEnds-1))
                    && isConsonant(input.charAt(prefixEnds))
            );
        }

        @Override
        public String toString() {
            return "ENDS_CVC";
        }
    }
}
