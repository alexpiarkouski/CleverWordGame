package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WordEntryTest {

    @Test
    void testItemConstructor() {
        WordEntry wordEntry = new WordEntry("quit", 13);
        assertEquals("quit", wordEntry.getWord());
        assertEquals(13, wordEntry.getWordValue());

    }

}