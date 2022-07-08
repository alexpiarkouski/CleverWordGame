package persistence;

import model.Game;
import model.LeaderboardEntry;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.*;
import java.util.ArrayList;

// Adapted from WorkRoomApp
// Represents a writer that writes JSON representation of workroom to file
public class JsonWriter {
    private static final int LOWEST_LETTER_NUM_RECORD = 3;
    private static final int TAB = 4;
    private PrintWriter writer;
    private final String destination;

    // From WorkRoomApp
    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // From WorkRoomApp
    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(destination);
    }

    // Modified from WorkRoomApp
    // MODIFIES: this
    // EFFECTS: writes JSON representation of workroom to file
    public void write(Game game) {
        JSONObject json = game.toJson();
        saveToFile(json.toString(TAB));
    }

    public void writeHighScore(Game game, JSONObject jsonObject, int highScore) {
        JSONArray leaderboardArray = jsonObject.getJSONArray("leaderboards");
        int outerIndex = game.getLetterNum() - LOWEST_LETTER_NUM_RECORD;
        JSONObject jsonLeaderboard = (JSONObject) leaderboardArray.get(outerIndex);
        jsonLeaderboard.put("high score", highScore);
        saveToFile(jsonObject.toString(TAB));
    }

    public void writeLeaderboardList(Game game, JSONObject jsonObject, int index) {
        jsonObject.put("last player name", Game.getLastPlayerName());
        JSONArray leaderboardArray = jsonObject.getJSONArray("leaderboards");
        int outerIndex = game.getLetterNum() - LOWEST_LETTER_NUM_RECORD;
        JSONObject jsonLeaderboard = (JSONObject) leaderboardArray.get(outerIndex);
        jsonLeaderboard = writeLeaderboard(game, jsonLeaderboard, index);
        //putAtIndex(index, newJsonLeaderboard, leaderboardArray);
        leaderboardArray.put(outerIndex, jsonLeaderboard);
        saveToFile(jsonObject.toString(TAB));
    }

    public JSONObject writeLeaderboard(Game game, JSONObject jsonObject, int index) {

        JSONArray jsonArrayGames = jsonObject.getJSONArray("games");
        putAtIndex(index, game.toJson(), jsonArrayGames);
        if (jsonArrayGames.length() > 5) {
            for (int i = jsonArrayGames.length(); i > 5; i--) {
                jsonArrayGames.remove(jsonArrayGames.length() - 1);
            }
        }
        return jsonObject;

    }

//    public void writeLeaderboardEntry(LeaderboardEntry entry) {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("last player name", entry.getName());
//
//        entry.toJson();
//    }

    private void putAtIndex(int index, JSONObject jsonObject, JSONArray jsonArray) {
        for (int i = jsonArray.length(); i > index; i--) {
            jsonArray.put(i, jsonArray.get(i - 1));
        }
        jsonArray.put(index, jsonObject);
    }

    // From WorkRoomApp
    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // From WorkRoomApp
    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }


    public void writeResetLeaderboard(Game game, JSONObject jsonObject, LeaderboardEntry entry, int length) {
        jsonObject.put("last player name", entry.getName());
        JSONArray leaderboardArray = jsonObject.getJSONArray("leaderboards");
        int outerIndex = game.getLetterNum() - LOWEST_LETTER_NUM_RECORD;
        JSONObject jsonLeaderboard = (JSONObject) leaderboardArray.get(outerIndex);
        jsonLeaderboard.put("high score", entry.getScore());
        JSONArray entries = new JSONArray();
        for (int i = 0; i < length; i++) {
            entries.put(entry.toJson());
        }
        jsonLeaderboard.put("games", entries);
        saveToFile(jsonObject.toString(TAB));
    }
}
