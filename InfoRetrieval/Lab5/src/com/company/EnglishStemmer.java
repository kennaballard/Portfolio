/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.util.Scanner;

/**
 * Singleton class that contains an implementation of Porter's stemming algorithm with
 * the original rules for English.
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 * @since 2018-03-17
 */
public class EnglishStemmer {

    private final static String RULES_LOCATION = "rules/";
    private final static String LANGUAGE_CODE = "en";
    private final static String CHAIN_LIST_FILE = RULES_LOCATION + LANGUAGE_CODE + "/chains.txt";

    // store the stemmer instance
    private static Stemmer instance;

    /**
     * Get the stemmer for English.
     * @return the stemmer.
     */
    public static Stemmer getInstance() {

        // Lazy initialization: only load the stemmer on the first request.
        if (instance == null) {

            // create the builder
            StemmerBuilder builder = new StemmerBuilder();

            try {

                // open the chain list file and figure out its path in the file system
                // in order to open the relative paths inside.
                Scanner chainListScanner = new Scanner(new FileReader(CHAIN_LIST_FILE));
                String path = new File(CHAIN_LIST_FILE).getParentFile().getPath();

                while (chainListScanner.hasNext()) {

                    // read in the chain's rules from it's file.
                    String chain = chainListScanner.nextLine();
                    Scanner chainRuleScanner = new Scanner(new FileReader(path + "/" + chain));

                    builder.startChain();

                    while(chainRuleScanner.hasNext()) {

                        // each rule has format: NONE sses -> ss
                        // but the replacement can be empty
                        String[] values = chainRuleScanner.nextLine().split(" ");
                        if(!(values.length == 3 || values.length == 4))
                            throw new ParseException("Rule must be in format: prefixTest suffix -> replacement", -1);

                        // add rule
                        builder.addRule(values[0], values[1], values.length == 3 ? "" : values[3]);
                    }

                    chainRuleScanner.close();

                }
                chainListScanner.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            instance = builder.create();
        }

        return instance;
    }

    /* The constructor is made private since this class is not intended to make
     * instances. */
    private EnglishStemmer() {}
}
