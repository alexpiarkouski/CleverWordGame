package persistence;

import model.Game;
import model.WordEntry;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// Test class for JsonWriter
// All tests modified from WorkRoomApp

class JsonWriterTest extends JsonTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    @Test
    void testWriterInvalidFile() {
        try {
            Game game = new Game();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyGame() {
        try {
            Game game = new Game();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyGame.json");
            writer.open();
            writer.write(game);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyGame.json");
            game = reader.read();
            assertEquals(0, game.getScore());
            assertEquals(0, game.getWordEntryList().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralGame() {
        try {
            Game game = new Game();
            game.addWordEntry(new WordEntry("cats", 6));
            game.addWordEntry(new WordEntry("aaaa", 4));
            game.updateScore(4+6);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralGame.json");
            writer.open();
            writer.write(game);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralGame.json");
            game = reader.read();
            assertEquals(4+6, game.getScore());
            List<WordEntry> wordEntries = game.getWordEntryList();
            assertEquals(2, wordEntries.size());
            checkWordEntry("cats", 6, wordEntries.get(0));
            checkWordEntry("aaaa", 4, wordEntries.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterHighScore() {
        try {
            Game game = new Game();
            JsonWriter writer = new JsonWriter("./data/testWriterHighScore.json");
            JsonReader reader = new JsonReader("./data/testReaderHighScore.json");
            writer.open();
            writer.writeHighScore(reader.readJson(), 42);
            writer.close();

            //JsonReader reader = new JsonReader("./data/testWriterHighScore.json");
            reader.readHighScore(game);
            assertEquals(60, game.getHighScore());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}