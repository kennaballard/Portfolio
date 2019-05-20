package com.company;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Query {
    private static MessageDigest digest;
    static {
        digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(); // should not occur
        }
    }

    public static void main(String[] args) throws IOException {
        // CREATE STEMMER
        Stemmer stemmer = EnglishStemmer.getInstance();
        HashMap<String, HashMap<String, Integer>> stems = new HashMap<>();

        // STEM SAVED WORDS
        stemIndexes(stems, stemmer);

        // GET QUERY WORD
        System.out.println("Enter query:");

        BufferedReader read = new BufferedReader(new InputStreamReader(System.in));

        // MUST BE STEMMED

        String word = read.readLine();
        word = stemmer.stem(word);

        // DISPLAY RELEVANCE
        checkRelevance(word, stems);
    }

    private static void checkRelevance(String query, HashMap<String, HashMap<String, Integer>> stemmedIndexes) {
        System.out.println("Results for \"" + query + "\":");

        for(Map.Entry<String, HashMap<String, Integer>> entry : stemmedIndexes.entrySet()){
            if(entry.getValue().containsKey(query)){
                System.out.println("*\t" + entry.getKey());
                System.out.println("\tRelevance: " + entry.getValue().get(query));
            }
        }
    }

    /*
        STEMS ALL ARTICLE INDEXES
     */
    public static void stemIndexes(HashMap<String, HashMap<String, Integer>> stemmedIndexes, Stemmer stemmer) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileReader("manifest.txt"));

        // GO THROUGH ARTICLES
        while(scanner.hasNextLine()){
            String url = scanner.nextLine();
            digest.reset();
            digest.update(url.getBytes());
            String hash = String.format("%032x", new BigInteger(1, digest.digest())) + ".txt";

            Scanner scan = new Scanner(new FileReader(hash));
            HashMap<String, Integer> stems = new HashMap<>();
            // GO THROUGH EACH ARTICLE'S INDEX
            while(scan.hasNextLine()){
                String[] values = scan.nextLine().split(" ");
                // save stems
                String key = stemmer.stem(values[0]);

                // CHECK IF KEY ALREADY EXISTS (STEMMED VERSION)
                Integer value = stems.containsKey(key) ? stems.get(key) + Integer.valueOf(values[2]) : Integer.valueOf(values[2]);

                stems.put(key, value);
            }
            scan.close();

            // ADD TO MAIN HASH MAP
            stemmedIndexes.put(url, stems);
        }
        scanner.close();
    }
}
