package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.HashMap;



public class Main {

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

        // GET COMMON ENGLISH WORDS
        Scanner scan = new Scanner(new FileReader("common-english-words.txt"));
        ArrayList<String> commonWords = new ArrayList<>();

        while(scan.hasNext()){
            // STORE COMMON WORDS IN ARRAY LIST
            commonWords.add(scan.next());
        }
        scan.close();

	    // READ IN URLS
        Scanner scanner = new Scanner(new FileReader("manifest.txt"));

        while(scanner.hasNextLine()){
            String url = scanner.nextLine();

            Document doc = Jsoup.connect(url).get();
            String text = doc.select("p").text();

            Scanner textScan = new Scanner(text);
            textScan.useDelimiter("[\\p{javaWhitespace}\u00A0]+");

            // INDEX TO STORE NUM OF OCCURRENCES
            HashMap<String, Integer> index = new HashMap<>();

            // FILE TO STORE INFO SETUP
            digest.reset();
            digest.update(url.getBytes());
            String hash = String.format("%032x", new BigInteger(1, digest.digest()));
            PrintWriter write = new PrintWriter((hash + ".txt"));

            while(textScan.hasNext()){
                // CONVERT TO LOWER CASE
                String next = textScan.next().toLowerCase();

                // REMOVE PREFIX AND SUFFIX NON-ALPHABETICAL CHARACTERS
                if(!Character.isLetter(next.charAt(0)))
                    next = next.substring(1, next.length());
                if(next.length() > 0 && !Character.isLetter(next.charAt(next.length()-1)))
                    next = next.substring(0, next.length()-1);

                // WORD IS NOT COMMON
                if(!commonWords.contains(next) && next.length() > 0){
                    // WORD HAS ALREADY OCCURRED
                    if(index.containsKey(next))
                        index.put(next, (index.get(next))+1);
                    // FIRST OCCURRENCE OF WORD
                    else
                        index.put(next, 1);
                }
            }

            textScan.close();

            // PUT ALL INDEX KEYS AND VALUES INTO FILE
            for (Map.Entry<String, Integer> entry: index.entrySet()) {
                write.println(entry.getKey() + " => " + entry.getValue());
            }

            write.close();
        }

        scanner.close();
    }
}
