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

    private int score;
    private static int highScore;
    private int attempts;
    private int letterNum;
    private static String lastPlayerName;
    private List<WordEntry> wordEntries;
    private static List<LeaderboardEntry> leaderboardEntries;
    private static List<String> validWords;

    // EFFECTS: new game is created. Score set to 0, empty word entries list.
    public Game(Integer attempts, Integer letterNum) {
        score = 0;
        this.attempts = attempts;
        this.letterNum = letterNum;
        //lastPlayerName = "";
        wordEntries = new ArrayList<>();
        //leaderboardEntries = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: Fills ArrayList validWords with Strings of individual words from file
    public static void txtToList(File file) throws FileNotFoundException {
        validWords = new ArrayList<>();
        Scanner scan = new Scanner(file);
        String validWord;
        while (scan.hasNext()) {
            validWord = scan.nextLine().toLowerCase();
            validWords.add(validWord);
        }
        scan.close();
    }

    public static void setLeaderboardEntries(List<LeaderboardEntry> leaderboardEntries) {
        Game.leaderboardEntries = leaderboardEntries;
    }

//    public static int getValidWordsSize() {
//        return validWords.size();
//    }

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
            wordValue += assignLetterPoints(letter);
        }
        return wordValue;
    }

    // MODIFIES: this
    // EFFECTS: adds wordValue to score
    public void updateScore(int wordValue) {
        score += wordValue;
    }

    // EFFECTS: returns true if both the valid english word and letter number conditions of the word are satisfied,
    // else false
//    public boolean checkIfWordValidLinear(String thisWord) {
//        boolean isWordValid = false;
//        try {
//            isWordValid = (checkIfWordInListLinear(thisWord) && checkLetterNum(thisWord));
//        } catch (FileNotFoundException e) {
//            System.out.println("Status: " + "Unable to find file: " + VALID_WORDS_LIST_STORE);
//        }
//        return isWordValid;
//    }

    // EFFECTS: returns true if both the valid english word and letter number conditions of the word are satisfied,
    // else false
    public boolean checkIfWordValid(String thisWord) {
        return checkIfWordInList(thisWord) && checkLetterNum(thisWord) && checkIfWordUnique(thisWord);
    }

    // EFFECTS: returns true if wordEntries does not contain a word entry with word value that equals thisWord
    public boolean checkIfWordUnique(String thisWord) {
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
//    public boolean checkIfWordInListLinear(String thisWord) throws FileNotFoundException {
//        boolean isWordInList = false;
//        Scanner scan = new Scanner(VALID_WORDS_LIST_STORE);
//        while (scan.hasNext()) {
//            String validWord = scan.nextLine().toLowerCase();
//            if (validWord.equals(thisWord)) {
//                isWordInList = true;
//            }
//        }
//        return isWordInList;
//    }

    // EFFECTS: returns true if given word is found in the eligible word list. Else false. Binary search
    public static boolean checkIfWordInList(String thisWord) {
        int result = Collections.binarySearch(validWords, thisWord);
        //System.out.println(result);
        return result > -1;
    }

    // EFFECTS: returns true if given word is a non-empty string of correct character length
    public boolean checkLetterNum(String thisWord) {
        if (!thisWord.equals("")) {
            return thisWord.length() == letterNum;
        }
        return false;
    }


    // EFFECTS: returns position index of this game on the leaderboard by score comparison.
    // If there are no leaderboard entries return top place (index of 0). If score < the lowest entry return -1
    public int findLeaderboardPositionIndex() {
        if (leaderboardEntries.isEmpty()) {
            return 0;
        }
        if (score > leaderboardEntries.get(leaderboardEntries.size() - 1).getScore()) {
            for (int i = leaderboardEntries.size() - 1; i > 0; i--) {
                if (score <= leaderboardEntries.get(i - 1).getScore()) {
                    return i;
                }
            }
            return 0;
        }
        return -1;
    }

    // Modified from WorkRoomApp
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("score", score);
        json.put("name", lastPlayerName);
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

    // MODIFIES: this
    // EFFECTS: adds wordEntry into the list of wordEntries
    public void addWordEntry(WordEntry wordEntry) {
        wordEntries.add(wordEntry);
        EventLog.getInstance().logEvent(new Event("Valid word entry " + wordEntry.getWord() + ", "
                + wordEntry.getWordValue() + " added to list of valid entries"));
    }

    // MODIFIES: this
    // EFFECTS: adds entry into the list of leaderboardEntries
    public static void addLeaderboardEntry(LeaderboardEntry entry) {
        leaderboardEntries.add(entry);
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

    // EFFECTS: returns high score
    public List<LeaderboardEntry> getLeaderboardEntryList() {
        return leaderboardEntries;
    }

    public void setLeaderboardEntryList(List<LeaderboardEntry> newList) {
        leaderboardEntries = newList;
    }

    // EFFECTS: sets high score to highScoreValue
    public static void setHighScore(int highScoreValue) {
        highScore = highScoreValue;
    }

    // EFFECTS: returns high score
    public static int getHighScore() {
        return highScore;
    }

    public static void setLastPlayerName(String name) {
        lastPlayerName = name;
    }

    // REQUIRES: lastPlayerName is an initalized string
    // EFFECTS: returns name of last player
    public static String getLastPlayerName() {
        return lastPlayerName;
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

    // MODIFIES: EventLog
    // EFFECTS: logs new event when game is saved to file
    public void logGameSave() {
        EventLog.getInstance().logEvent(new Event("Game saved to file"));
    }
}
