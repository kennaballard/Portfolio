package com.company;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * DESCRIPTION HERE
 *
 * @author Kennedy Ballard
 */
public class StemmerBuilder {

    private MyStemmer stemmer;

    public StemmerBuilder() {
        stemmer = new MyStemmer();
    }

    /**
     *  Begin building a new chain.
     */
    public void startChain() {
        // ADD A NEW CHAIN
        stemmer.chains.add(new ArrayList<>());
    }

    /**
     *  Add a rule to the current chain being built.
     */
    public void addRule(String prefixTest, String suffix, String replacement) throws ParseException {
        // ADD NEW RULE TO MOST RECENT CHAIN (last index of chains storage --> add rule to chain)
        stemmer.chains.get(stemmer.chains.size() - 1).add(new Rule(prefixTest, suffix, replacement));
    }

    /**
     *  Using the chains that have been added to the builder, construct and return a Stemmer implementation.
     *  @return the built stemmer. 
     */
    public Stemmer create() {
        return stemmer;
    }

    /**
     * Inner stemmer class that is being created by this builder. 
     */
    public class MyStemmer implements Stemmer {

        ArrayList<ArrayList<Rule>> chains;
        /**
         *  Construct a stemmer.
         */
        public MyStemmer() {
            chains = new ArrayList<>();
        }

        /**
         * Stem the current word
         * @return the stemmed word
         */
        public String stem(String word) {
            // GO THROUGH CHAINS
            for(ArrayList<Rule> chain : chains){
                // GO THROUGH RULES IN CHAIN
                for(Rule rule : chain){
                    // WHEN RULE FITS THEN REPLACE AND MOVE TO NEXT CHAIN
                    if(rule.matches(word)){
                        word = rule.replace(word);
                        break;
                    }
                }
            }
            return word;
        }
    }

}
