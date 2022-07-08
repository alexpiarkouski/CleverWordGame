package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardEntry implements Writable {
    private int score;
    private String name;
    private List<WordEntry> wordEntries; //points value of all characters in a word

    public LeaderboardEntry() {
        //super(attempts, letterNum);
        score = 0;
        name = "---";
        wordEntries = new ArrayList<>();
    }

    // EFFECTS: word is set to thisWord, points value is set to thisWordValue
    public LeaderboardEntry(//int attempts, int letterNum,
                             int score, String name
            //, List<WordEntry> wordEntries
                            ) {
        //super(attempts, letterNum);
        this.score = score;
        this.name = name;
        //this.wordEntries = wordEntries;
    }


    //EFFECTS: Returns total point value of characters in the word in a wordEntry
    public int getScore() {
        return score;
    }

    //EFFECTS: Returns the word string in a wordEntry
    public String getName() {
        return name;
    }

    public List<WordEntry> getWordEntries() {
        return wordEntries;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("score", score);
        json.put("name", name);
        //json.put("word entries", wordEntriesToJson());
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
}
