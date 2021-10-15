package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
    }

    @Test
    void testEnterValidWord() {
        int prevAttempts = game.getAttempts();
        game.enterValidWord("cats");
        assertEquals(1, game.getWordEntryList().size());
        assertEquals(prevAttempts - 1, game.getAttempts());
        assertEquals("cats", game.getWordEntryList().get(0).getWord());
        assertEquals(6, game.getWordEntryList().get(0).getWordValue());
        game.enterValidWord("dogs");
        game.enterValidWord("park");
        assertEquals(3, game.getWordEntryList().size());
        assertEquals(prevAttempts - 3, game.getAttempts());
        assertEquals("park", game.getWordEntryList().get(2).getWord());
        assertEquals(10, game.getWordEntryList().get(2).getWordValue());
    }

    @Test
    void testEnterInvalidWord() {
        int prevAttempts = game.getAttempts();
        game.enterInvalidWord();
        assertEquals(prevAttempts - 1, game.getAttempts());
        assertEquals(0, game.getWordEntryList().size());
        game.enterInvalidWord();
        game.enterInvalidWord();
        assertEquals(prevAttempts - 3, game.getAttempts());
        assertEquals(0, game.getWordEntryList().size());
    }

    @Test
    void testAssignLetterPoints() {
        assertEquals(1, game.assignLetterPoints('a'));
        assertEquals(4, game.assignLetterPoints('w'));
        assertEquals(8, game.assignLetterPoints('j'));
        assertEquals(10, game.assignLetterPoints('z'));
        assertEquals(0, game.assignLetterPoints('1'));
        assertEquals(0, game.assignLetterPoints(' '));
    }

    @Test
    void testUpdateScore() {
        game.updateScore(15);
        assertEquals(15, game.getScore());
        game.updateScore(0);
        assertEquals(15, game.getScore());
        game.updateScore(200);
        assertEquals(215, game.getScore());
    }

    @Test
    void testCheckIfWordValid() {
        assertEquals(game.checkIfWordInList("cats") && game.checkLetterNum("cats"),
                game.checkIfWordValid("cats"));
        assertEquals(game.checkIfWordInList("cat") && game.checkLetterNum("cat"),
                game.checkIfWordValid("cat"));
        assertEquals(false, game.checkIfWordValid("monkey"));
        assertEquals(false, game.checkIfWordValid("abcdefghijklmnopqrstuvwxyz"));
        assertEquals(game.checkIfWordInList("9999") && game.checkLetterNum("9999"),
                game.checkIfWordValid("9999"));
    }

    @Test
    // Work in progress - method returns true by default for now except false for "9999" - testing purposes
    void testCheckIfWordInList() {
        assertTrue(game.checkIfWordInList(""));
        assertTrue(game.checkIfWordInList("abc"));
        assertTrue(game.checkIfWordInList("123"));
        assertFalse(game.checkIfWordInList("9999"));
    }

    @Test
    void testCheckLetterNum() {
        assertFalse(game.checkLetterNum("cat"));
        assertTrue(game.checkLetterNum("cats"));
        assertFalse(game.checkLetterNum(""));
        assertFalse(game.checkLetterNum("monkey"));
        assertTrue(game.checkLetterNum("    "));
    }

    @Test
    void testGetLetterNum() {
        assertEquals(4, game.getLetterNum());
        Game newGame = new Game();
        assertEquals(4, newGame.getLetterNum());
    }
}