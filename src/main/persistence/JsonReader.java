package persistence;

import model.Game;
import model.LeaderboardEntry;
import model.WordEntry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.json.*;

// Adapted from WorkRoomApp
// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private final String source;

    // From WorkRoomApp
    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    public JSONObject readJson() throws IOException {
        String jsonData = readFile(source);
        return new JSONObject(jsonData);
    }

    // Modified from WorkRoomApp
    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Game read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseGame(jsonObject);
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public void readHighScore(Game game) throws IOException {
        JSONObject jsonObject = readJson();

        addHighScore(game, jsonObject);
        addLeaderboardEntries(game, jsonObject);
        addLastPlayerName(game, jsonObject);
    }

    //    public JSONObject readLeaderboard(Game game) throws IOException {
//        String jsonData = readFile(source);
//        JSONObject jsonObject = new JSONObject(jsonData);
//        return jsonObject;
//    }

    // From WorkRoomApp
    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    // Modified from WorkRoomApp
    // EFFECTS: parses game from JSON object and returns it
    private Game parseGame(JSONObject jsonObject) {
        Game game = new Game();
        addWordEntries(game, jsonObject);
        addScore(game, jsonObject);
        return game;
    }

//    private JSONObject parseLeaderboard(JSONObject jsonObject, Game game) {
//
//        int highScore = jsonObject.getInt("high score");
//        game.setHighScore(highScore);
//
//        JSONArray jsonArrayGames = jsonObject.getJSONArray("games");
//        return
//    }

    // Modified from WorkRoomApp
    // MODIFIES: game
    // EFFECTS: parses word entries from JSON object and adds them to game
    private void addWordEntries(Game game, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("word entries");
        for (Object json : jsonArray) {
            JSONObject nextWordEntry = (JSONObject) json;
            addWordEntry(game, nextWordEntry);
        }
    }

    private List<WordEntry> wordEntriesToArray(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("word entries");
        List<WordEntry> wordEntries = new ArrayList<>();
        for (Object json : jsonArray) {
            JSONObject nextWordEntry = (JSONObject) json;
            String word = nextWordEntry.getString("word");
            int wordValue = nextWordEntry.getInt("word value");
            WordEntry wordEntry = new WordEntry(word, wordValue);
            wordEntries.add(wordEntry);
        }
        return wordEntries;
    }

    // Modified from WorkRoomApp
    // MODIFIES: game
    // EFFECTS: parses score from JSON object and adds it to game
    private void addScore(Game game, JSONObject jsonObject) {
        int score = jsonObject.getInt("score");
        game.updateScore(score);
    }

    private void addLeaderboardEntries(Game game, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("games");

        for (Object json : jsonArray) {
            JSONObject nextGame = (JSONObject) json;
            LeaderboardEntry entry = new LeaderboardEntry(nextGame.getInt("score"),
                    nextGame.getString("name"), wordEntriesToArray(nextGame));
            game.addLeaderboardEntry(entry);
        }
    }

    private void addHighScore(Game game, JSONObject jsonObject) {
        int highScore = jsonObject.getInt("high score");
        game.setHighScore(highScore);
    }

    private void addLastPlayerName(Game game, JSONObject jsonObject) {
        String lastPlayerName = jsonObject.getString("last player name");
        game.setLastPlayerName(lastPlayerName);
    }

    // Modified from WorkRoomApp
    // MODIFIES: game
    // EFFECTS: parses word entry from JSON object and adds it to game
    private void addWordEntry(Game game, JSONObject jsonObject) {
        String word = jsonObject.getString("word");
        int wordValue = jsonObject.getInt("word value");
        WordEntry wordEntry = new WordEntry(word, wordValue);
        game.addWordEntry(wordEntry);
    }
}
