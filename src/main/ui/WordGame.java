package ui;

import model.Game;

import java.util.Scanner;

// Clever Word Game app
public class WordGame {

    private Scanner input;
    private Game game;

    // EFFECTS: runs the Word Game app
    public WordGame() {
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

    // modified from TellerApp
    // MODIFIES: this
    // EFFECTS: processes user command. If player has 0 attempts and wishes to play again constructs a new game
    private void processCommand(String command) {
        if (command.equals("play")) {
            if (game.getAttempts() == 0) {
                System.out.println("Scores and attempts reset succesfully. You can start a new game now.");
                game = new Game();
            } else {
                playGame();
                endGameMessage();
            }
        } else if (command.equals("reset")) {
            System.out.println("Scores and attempts reset succesfully");
            game = new Game();
        } else if (command.equals("score")) {
            lastScore();
        } else if (command.equals("last_game")) {
            lastGame();
        } else {
            System.out.println("Selection not valid...");
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

    // modified from TellerApp
    // MODIFIES: this
    // EFFECTS: initializes accounts
    private void init() {
        game = new Game();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    // modified from TellerApp
    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tplay -> play game");
        System.out.println("\treset -> restart game");
        System.out.println("\tscore -> view last score");
        System.out.println("\tlast_game -> see last game");
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
