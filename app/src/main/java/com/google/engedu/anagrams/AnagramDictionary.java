package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 4 ;
    private static final int MAX_WORD_LENGTH = 7;
    private Random generator = new Random();
    private ArrayList<String> wordList;
    private HashSet<String> wordSet;
    private HashMap<String, ArrayList<String>> lettersToWords;
    private HashMap<Integer, ArrayList<String>> sizeToWords;
    private int wordLength = DEFAULT_WORD_LENGTH;

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        wordList = new ArrayList<String>();
        wordSet = new HashSet<String>();
        lettersToWords = new HashMap<String, ArrayList<String>>();
        sizeToWords = new HashMap<Integer, ArrayList<String>>();

        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);

            String sortedWord = sortLetters(word);
            if(!lettersToWords.containsKey(sortedWord)) {
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(word);
                lettersToWords.put(sortedWord,temp);

            }
            else {
                ArrayList<String> words = lettersToWords.get(sortedWord);
                words.add(word);

                lettersToWords.put(sortedWord, words);
                //lettersToWords.put(sortedWord, lettersToWords.get(sortedWord).add(word));
            }

            if(!sizeToWords.containsKey(word.length())) {
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(word);
                sizeToWords.put(word.length(), temp);
            }
            else {
                ArrayList<String> words = sizeToWords.get(word.length());
                words.add(word);

                sizeToWords.put(word.length(), words);
            }
        }

    }

    public boolean isGoodWord(String word, String base) {

        if(!wordSet.contains(word))
            return false;

        if(word.toLowerCase().contains(base.toLowerCase()))
            return false;

        return true;
    }

    public ArrayList<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();

        String sortedTarget = sortLetters(targetWord);

        if(lettersToWords.containsKey(sortedTarget))
            return(lettersToWords.get(sortedTarget));

        return result;

    }

    private String sortLetters(String word) {
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        String temp = new String(chars);

        return temp;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();

        for(char ix = 'a'; ix <= 'z'; ix++) {
            String newWord = word + Character.toString(ix);
            newWord = sortLetters(newWord);

            result.addAll(getAnagrams(newWord));
        }

        return result;
    }

    public ArrayList<String> getAnagramsWithTwoMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();

        for(char ix = 'a'; ix <= 'z'; ix++) {
            for(char jx = ix; jx <= 'z'; jx++) {
                String newWord = word + Character.toString(jx) + Character.toString(ix);
                newWord = sortLetters(newWord);


               result.addAll(getAnagrams(newWord));
            }
        }


        return result;
    }

    public String pickGoodStarterWord() {

        int randomIndex = generator.nextInt(wordList.size());
        int ix = 0;
        int jx = randomIndex;

        while(ix < wordList.size()) {

            String word = wordList.get(jx);
            String sortedWord = sortLetters(word);
            if(lettersToWords.get(sortedWord).size() == MIN_NUM_ANAGRAMS
                    && word.length() == wordLength) {

                wordLength++;
                if(wordLength == MAX_WORD_LENGTH)
                    wordLength = DEFAULT_WORD_LENGTH;

                return sizeToWords.get(wordLength - 1).get(generator.nextInt(sizeToWords.get(wordLength - 1).size()));
            }

            jx++;
            if(jx == wordList.size())
                jx = 0;


            ix++;
        }


        return "ouch";
    }
}
