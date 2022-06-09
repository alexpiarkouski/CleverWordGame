package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

// Represents a Clever Word Game round
public class Game implements Writable {

    private static final File VALID_WORDS_LIST_STORE = new File("./data/words_alpha_sorted.txt");
    private int score;
    private int highScore;
    private int attempts;
    private int letterNum;
    private List<WordEntry> wordEntries;
    private List<String> validWords;

    // EFFECTS: new game is created. Score set to 0, empty word entries list.
    public Game() {
        wordEntries = new ArrayList<>();
        score = 0;
        attempts = 5;
        letterNum = 4;
        try {
            txtToList(VALID_WORDS_LIST_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Status: " + "Unable to find file: " + VALID_WORDS_LIST_STORE);
        }
    }

    public void txtToList(File file) throws FileNotFoundException {
        validWords = new ArrayList<>();
        Scanner scan = new Scanner(file);
        String validWord;
        while (scan.hasNext()) {
            validWord = scan.nextLine().toLowerCase();
            validWords.add(validWord);
        }
        scan.close();
//        Collections.sort(validWords);
//        String outputFile = "words_alpha_sorted.txt";
//        FileWriter fileWriter = null;
//        try {
//            fileWriter = new FileWriter(outputFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        PrintWriter out = new PrintWriter(fileWriter);
//        for (String outputLine : validWords) {
//            out.println(outputLine);
//        }
//        out.flush();
//        out.close();
//        try {
//            fileWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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

    // EFFECTS: Returns corresponding point value of each lowercase english letter character
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
    public boolean checkIfWordValidLinear(String thisWord) {
        boolean isWordValid = false;
        try {
            isWordValid = (checkIfWordInListLinear(thisWord) && checkLetterNum(thisWord));
        } catch (FileNotFoundException e) {
            System.out.println("Status: " + "Unable to find file: " + VALID_WORDS_LIST_STORE);
        }
        return isWordValid;
    }

    // EFFECTS: returns true if both the valid english word and letter number conditions of the word are satisfied,
    // else false
    public boolean checkIfWordValid(String thisWord) {
        return checkIfWordInList(thisWord) && checkLetterNum(thisWord) && checkDuplicates(thisWord);
    }

    // EFFECTS: returns true if wordEntries does not contain a word entry with word value that equals thisWord
    private boolean checkDuplicates(String thisWord) {
        if (wordEntries.isEmpty()) {
            return true;
        }
        for (WordEntry wordEntry : wordEntries) {
            if (wordEntry.getWord().equals(thisWord)) {
                return false;
            }
        }
        return true;
    }

    // EFFECTS: returns true if given word is found in the eligible word list. Else false
    public boolean checkIfWordInListLinear(String thisWord) throws FileNotFoundException {
        boolean isWordInList = false;
        Scanner scan = new Scanner(VALID_WORDS_LIST_STORE);
        while (scan.hasNext()) {
            String validWord = scan.nextLine().toLowerCase();
            if (validWord.equals(thisWord)) {
                isWordInList = true;
            }
        }
        return isWordInList;
    }

    // EFFECTS: returns true if given word is found in the eligible word list. Else false. Binary search
    public boolean checkIfWordInList(String thisWord) {
        int result = Collections.binarySearch(validWords, thisWord);
        System.out.println(result);
        return result > -1;
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

    // EFFECTS: returns true if given word is a non-empty string of correct character length
    public boolean checkLetterNum(String thisWord) {
        if (!thisWord.equals("")) {
            return thisWord.length() == letterNum;
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: adds wordEntry into the list of wordEntries
    public void addWordEntry(WordEntry wordEntry) {
        wordEntries.add(wordEntry);
        EventLog.getInstance().logEvent(new Event("Valid word entry " + wordEntry.getWord() + ", "
                + wordEntry.getWordValue() + " added to list of valid entries"));
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

    // EFFECTS: sets high score to highScoreValue
    public void setHighScore(int highScoreValue) {
        highScore = highScoreValue;
    }

    // EFFECTS: returns high score
    public int getHighScore() {
        return highScore;
    }

    // MODIFIES: EventLog
    // EFFECTS: logs new event when game is reset
    public void logGameReset() {
        EventLog.getInstance().logEvent(new Event("Game reset. List of valid entries reset"));
    }

    // MODIFIES: EventLog
    // EFFECTS: logs new event when game is loaded from file
    public void logGameLoad() {
        EventLog.getInstance().logEvent(new Event("Game loaded from file. List of valid entries reset"));
    }
}
