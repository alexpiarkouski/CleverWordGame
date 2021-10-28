package persistence;

import model.WordEntry;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Modified from WorkRoomApp
public class JsonTest {
    protected void checkWordEntry(String word, int wordValue, WordEntry wordEntry) {
        assertEquals(word, wordEntry.getWord());
        assertEquals(wordValue, wordEntry.getWordValue());
    }
}
