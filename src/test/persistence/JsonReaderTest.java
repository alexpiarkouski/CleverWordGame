package persistence;

import model.Game;
import model.WordEntry;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Test class for JsonReader
// All tests modified from WorkRoomApp

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Game game = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyGame() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyGame.json");
        try {
            Game game = reader.read();
            assertEquals(0, game.getScore());
            assertEquals(0, game.getWordEntryList().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralGame() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralGame.json");
        try {
            Game game = reader.read();
            assertEquals(10, game.getScore());
            List<WordEntry> wordEntries = game.getWordEntryList();
            assertEquals(2, wordEntries.size());
            checkWordEntry("aaaa", 4, wordEntries.get(0));
            checkWordEntry("cats", 6, wordEntries.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}