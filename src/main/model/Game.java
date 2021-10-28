package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Represents a Clever Word Game round
public class Game implements Writable {
    private int score;
    private int attempts;
    private int letterNum;
    private List<WordEntry> wordEntries;

    // EFFECTS: new game is created. Score set to 0, empty word entries list.
    public Game() {
        wordEntries = new ArrayList<>();
        score = 0;
        attempts = 5;
        letterNum = 4;

    }

    // REQUIRES: attempts >= 1
    // MODIFIES: this
    // New word entry is created, word value is assigned to the word in word entry,
    // word entry added to list of word entries, score is updated, an attempt is subtracted
    public void enterValidWord(String word) {
        WordEntry wordEntry = new WordEntry(word, assignWordValue(word));
        addWordEntry(wordEntry);
        updateScore(wordEntry.getWordValue());
        attempts--;
    }

    // REQUIRES: attempts >= 1
    // MODIFIES: this
    // EFFECTS: An attempt is subtracted
    public void enterInvalidWord() {
        attempts--;
    }


    // MODIFIES: this
    // EFFECTS: word point value is updated for every character's point value in the word and returned
    private int assignWordValue(String word) {
        int wordValue = 0;
        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            wordValue = wordValue + assignLetterPoints(letter);
        }
        return wordValue;
    }

    // EFFECTS: Returns corresponsing point value of each lowercase english letter character
    public int assignLetterPoints(char letter) {
        if (letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'n'
                || letter == 'r' || letter == 't' || letter == 'l' || letter == 's' || letter == 'u') {
            return 1;
        } else if (letter == 'd' || letter == 'g') {
            return 2;
        } else if (letter == 'b' || letter == 'c' || letter == 'm' || letter == 'p') {
            return 3;
        } else if (letter == 'f' || letter == 'h' || letter == 'v' || letter == 'w' || letter == 'y') {
            return 4;
        } else if (letter == 'k') {
            return 5;
        } else if (letter == 'j' || letter == 'x') {
            return 8;
        } else if (letter == 'q' || letter == 'z') {
            return 10;
        } else {
            return 0;
        }
    }

    // MODIFIES: this
    // EFFECTS: adds wordValue to score
    public void updateScore(int wordValue) {
        score = score + wordValue;
    }

    // EFFECTS: returns true if both the valid english word and letter number conditions of the word are satisfied,
    // else false
    public boolean checkIfWordValid(String thisWord) {
        return (checkIfWordInList(thisWord) && checkLetterNum(thisWord));
    }

    // EFFECTS: returns true if given word is found in the eligible word list
    // Note: stub, work in progress
    public boolean checkIfWordInList(String thisWord) {
        if (thisWord.equals("9999")) { //testing stub
            return false;
        } else {
            return true;
        }
    }

    // Modified from WorkRoomApp
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("score", score);
        json.put("word entries", wordEntriesToJson());
        return json;
    }

    // Modified from WorkRoomApp
    // EFFECTS: returns wordEntries in this game as a JSON array
    private JSONArray wordEntriesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (WordEntry wordEntry : wordEntries) {
            jsonArray.put(wordEntry.toJson());
        }

        return jsonArray;
    }


    // EFFECTS: returns true if given word is of correct character length
    public boolean checkLetterNum(String thisWord) {
        return thisWord.length() == letterNum;
    }

    // MODIFIES: this
    // EFFECTS: adds wordEntry into the list of wordEntries
    public void addWordEntry(WordEntry wordEntry) {
        wordEntries.add(wordEntry);
    }

    // REQUIRES: score is a nonnegative integer
    // EFFECTS: returns point score
    public int getScore() {
        return score;
    }

    // REQUIRES: attempts is a nonnegative integer
    // EFFECTS: returns number of attempts left
    public int getAttempts() {
        return attempts;
    }

    // REQUIRES: letterNum is a nonnegative integer
    // EFFECTS: returns number of letters required for a word
    public int getLetterNum() {
        return letterNum;
    }

    // EFFECTS: returns list of word entries
    public List<WordEntry> getWordEntryList() {
        return wordEntries;
    }

}
