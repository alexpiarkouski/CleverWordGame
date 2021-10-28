package ui;

import model.Game;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// Clever Word Game app
public class WordGame {

    // Some fields modified from WorkRoomApp
    private static final String JSON_STORE = "./data/game.json";
    private Scanner input;
    private Game game;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // Modified from WorkRoomApp
    // EFFECTS: runs the Word Game app
    public WordGame() throws FileNotFoundException {
        runWordGame();
    }

    // modified from TellerApp
    // MODIFIES: this
    // EFFECTS: processes user input
    private void runWordGame() {
        boolean keepGoing = true;
        String command = null;

        init();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("quit_game")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }

    // modified from TellerApp and WorkRoomApp
    // MODIFIES: this
    // EFFECTS: processes user command. If player wishes to play constructs a new game and proceeds to play
    private void processCommand(String command) {
        if (command.equals("play")) {
            game = new Game();
            playGame();
            endGameMessage();
        } else if (command.equals("score")) {
            lastScore();
        } else if (command.equals("last_game")) {
            lastGame();
        } else if (command.equals("save")) {
            saveGame();
        } else if (command.equals("load")) {
            loadGame();
        } else {
            System.out.println("Selection not valid...");
        }
    }


    // Modified from WorkRoomApp
    // EFFECTS: saves game to file
    private void saveGame() {
        try {
            jsonWriter.open();
            jsonWriter.write(game);
            jsonWriter.close();
            System.out.println("Saved game with score " + game.getScore() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // Modified from WorkRoomApp
    // MODIFIES: this
    // EFFECTS: loads game from file
    private void loadGame() {
        try {
            game = jsonReader.read();
            System.out.println("Loaded game with score " + game.getScore() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: displays word entries from the previous game
    private void lastGame() {
        System.out.println("Last valid game set is: ");
        getWordEntries();
    }

    // EFFECTS: displays point score from the previous game
    private void lastScore() {
        System.out.println("Last final score is: " + game.getScore());
    }

    // REQUIRES: attempts >=1
    // EFFECTS: executes and prints the gameplay interface. Prints number of attempts left,
    // and whether input word is valid or not
    private void playGame() {
        System.out.println("You have " + game.getAttempts() + " attempts left");
        System.out.println("Enter a " + game.getLetterNum() + "-letter word");
        String word = input.next();
        if (!(game.checkIfWordValid(word))) {
            game.enterInvalidWord();
            System.out.println("Invalid Word. Your score is " + game.getScore());
        } else {
            game.enterValidWord(word);
            System.out.println("Valid Word. Your score is " + game.getScore());
        }
        if (game.getAttempts() > 0) {
            playGame();
        }
    }

    // EFFECTS: displays end game message with final score and word entries (game set)
    private void endGameMessage() {
        System.out.println("Game Over. Your final score is " + game.getScore());
        System.out.println("Your valid game set is ");
        getWordEntries();
    }

    // modified from TellerApp and WorkRoomApp
    // MODIFIES: this
    // EFFECTS: initializes Scanner and Json Reader and Writer
    private void init() {
        input = new Scanner(System.in);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // modified from TellerApp
    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tplay -> play game");
        System.out.println("\tscore -> view last score");
        System.out.println("\tlast_game -> see last game");
        System.out.println("\tsave -> save last game to file");
        System.out.println("\tload -> load last game from file");
        System.out.println("\tquit_game -> quit game");
    }

    // EFFECTS: displays recorded word entries
    private void getWordEntries() {
        if (game.getWordEntryList().size() == 0) {
            System.out.println("Empty game");
        } else {
            for (int i = 0; i < game.getWordEntryList().size(); i++) {
                System.out.println(game.getWordEntryList().get(i).getWord() + ", "
                        + game.getWordEntryList().get(i).getWordValue() + "; ");
            }
        }
    }


}
