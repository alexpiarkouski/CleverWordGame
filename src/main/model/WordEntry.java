package model;

import java.util.ArrayList;

// Represents a word entry consisting of a word and the points it is worth
public class WordEntry {
    private String word; //word entered
    private int wordValue; //points value of all characters in a word

    // EFFECTS: word is set to thisWord, points value is set to thisWordValue
    public WordEntry(String thisWord, int thisWordValue) {
        word = thisWord;
        wordValue = thisWordValue;

    }

    //EFFECTS: Returns total point value of characters in the word in a wordEntry
    public int getWordValue() {
        return wordValue;
    }

    //EFFECTS: Returns the word string in a wordEntry
    public String getWord() {
        return word;
    }




}
