package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

// Test class for Game
class GameTest {

    private Game game;
    static File VALID_WORDS_LIST_STORE = new File("./data/words_alpha_sorted.txt");

    @BeforeAll
    static void setValidWords() throws FileNotFoundException {
        Game.txtToList(VALID_WORDS_LIST_STORE);
    }


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
        game.enterValidWord("upyx");
        assertEquals("upyx", game.getWordEntryList().get(3).getWord());
        assertEquals(16, game.getWordEntryList().get(3).getWordValue());
        game.enterValidWord("jxqz");
        assertEquals("jxqz", game.getWordEntryList().get(4).getWord());
        assertEquals(36, game.getWordEntryList().get(4).getWordValue());

        Game newGame = new Game();
        newGame.enterValidWord("fhvw");
        assertEquals("fhvw", newGame.getWordEntryList().get(0).getWord());
        assertEquals(16, newGame.getWordEntryList().get(0).getWordValue());
        newGame.enterValidWord("eibm");
        assertEquals("eibm", newGame.getWordEntryList().get(1).getWord());
        assertEquals(8, newGame.getWordEntryList().get(1).getWordValue());
        newGame.enterValidWord("ontl");
        assertEquals("ontl", newGame.getWordEntryList().get(2).getWord());
        assertEquals(4, newGame.getWordEntryList().get(2).getWordValue());
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
        assertEquals(1, game.assignLetterPoints('u'));
        assertEquals(4, game.assignLetterPoints('y'));
        assertEquals(8, game.assignLetterPoints('x'));
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
        assertEquals(Game.checkIfWordInList("cats")
                        && game.checkLetterNum("cats"),
                game.checkIfWordValid("cats"));

        assertEquals(Game.checkIfWordInList("cat")
                        && game.checkLetterNum("cat"),
                game.checkIfWordValid("cat"));

        assertFalse(game.checkIfWordValid("monkey"));
        assertFalse(game.checkIfWordValid("abcdefghijklmnopqrstuvwxyz"));

        assertEquals(Game.checkIfWordInList("9999")
                        && game.checkLetterNum("9999"),
                game.checkIfWordValid("9999"));

        assertEquals(Game.checkIfWordInList("999")
                        && game.checkLetterNum("999"),
                game.checkIfWordValid("999"));

        assertEquals(Game.checkIfWordInList("")
                && game.checkLetterNum(""),
                game.checkIfWordValid(""));

        game.enterValidWord("dogs");

        assertEquals(game.checkIfWordUnique("dogs")
                && Game.checkIfWordInList("dogs")
                        && game.checkLetterNum("dogs"),
                game.checkIfWordValid("dogs"));

        assertEquals(game.checkIfWordUnique("cats")
                        && Game.checkIfWordInList("cats")
                        && game.checkLetterNum("cats"),
                game.checkIfWordValid("cats"));
    }

    @Test

    void testCheckIfWordInList() {
        assertFalse(Game.checkIfWordInList(""));
        assertTrue(Game.checkIfWordInList("cat"));
        assertTrue(Game.checkIfWordInList("cats"));
        assertFalse(Game.checkIfWordInList("zzz"));
        assertFalse(Game.checkIfWordInList("2"));
        assertFalse(Game.checkIfWordInList("9999"));
        assertFalse(Game.checkIfWordInList("catz"));
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
    void testCheckDuplicates() {
        assertTrue(game.checkIfWordUnique("cats"));
        game.enterValidWord("cats");
        assertFalse(game.checkIfWordUnique("cats"));
        assertTrue(game.checkIfWordUnique("dogs"));
        game.enterValidWord("dogs");
        assertFalse(game.checkIfWordUnique("dogs"));

    }

    @Test
    void testGetLetterNum() {
        assertEquals(4, game.getLetterNum());
        Game newGame = new Game();
        assertEquals(4, newGame.getLetterNum());
    }

    @Test
    void logGameReset() {
        // stub
    }

    @Test
    void logGameLoad() {
        // stub
    }

    @Test
    void setHighScore() {
        Game.setHighScore(20);
        assertEquals(20, Game.getHighScore());
        Game.setHighScore(30);
        assertEquals(30, Game.getHighScore());
    }

    @Test
    void getHighScore() {
        Game.setHighScore(20);
        int highscore = Game.getHighScore();
        assertEquals(20, highscore);
    }
}