package ui;

import model.Game;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// Clever Word Game app
public class WordGame extends JPanel implements ActionListener {

    // Some fields modified from WorkRoomApp
    private static final String JSON_STORE = "./data/game.json";
    private Scanner input;
    private Game game;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 250;

    private JFrame frame;
    private JPanel menuButtonPanel;
    private JPanel gamePanel;
    private JPanel resultsPanel;
    private JList list;
    private DefaultListModel listModel;
    private DefaultListModel resultsListModel;
    private JTextField textField;

    private JLabel scoreLabel;
    private JLabel statusLabel;
    private JLabel resultsTitleLabel;
    private JList resultsList;

    // Modified from WorkRoomApp
    // EFFECTS: runs the Word Game app
    // throws FileNotFoundException
    public WordGame() throws FileNotFoundException {
        runWordGame();
    }

    // modified from TellerApp
    // MODIFIES: this
    // EFFECTS: processes user input
    public void runWordGame() {

        frame = new JFrame("Clever Word Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        //JComponent menuContentPane = initMenu();
        //menuContentPane.setOpaque(true); //content panes must be opaque
        //frame.setContentPane(menuContentPane);

        initJson();
        initMenuButtonPanel();
        initGamePanel();
        initResultsPanel();

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.LINE_AXIS));
        frame.add(menuButtonPanel);
        frame.add(gamePanel);
        frame.add(resultsPanel);

        //Display the window.
        //frame.pack();
        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);
    }

//    private JComponent initMenu() {
//        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
//        return this;
//    }

    private void initMenuButtonPanel() {
        menuButtonPanel = new JPanel();
        menuButtonPanel.setLayout(new BoxLayout(menuButtonPanel, BoxLayout.PAGE_AXIS));

        menuButtonPanel.add(makePlayButton());
        menuButtonPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        menuButtonPanel.add(makeScoreButton());
        menuButtonPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        menuButtonPanel.add(makeLastSetButton());
        menuButtonPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        menuButtonPanel.add(makeSaveButton());
        menuButtonPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        menuButtonPanel.add(makeloadButton());

    }

    // modified from TellerApp and WorkRoomApp
    // MODIFIES: this
    // EFFECTS: initializes Scanner and Json Reader and Writer
    private void initJson() {
        input = new Scanner(System.in);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // MODIFIES: this
    // EFFECTS: initializes Game Panel
    private void initGamePanel() {
        gamePanel = new JPanel();

        listModel = new DefaultListModel();
        listModel.addElement("<no words>");
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setVisibleRowCount(5);

        textField = new JTextField(10);
        textField.addActionListener(this);

        scoreLabel = new JLabel("Current Score: " + "0");

        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.LINE_AXIS));
        gamePanel.add(list);
        gamePanel.add(Box.createHorizontalGlue());
        gamePanel.add(textField);
        gamePanel.add(Box.createRigidArea(new Dimension(10, 0)));
        gamePanel.add(makeEnterButton());
        gamePanel.add(scoreLabel);
    }

    private void initResultsPanel() {
        resultsPanel = new JPanel();

        resultsTitleLabel = new JLabel("Results?");
        statusLabel = new JLabel("No status");

        resultsListModel = new DefaultListModel();
        resultsListModel.addElement("<no data>");
        resultsList = new JList(resultsListModel);
        resultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultsList.setVisibleRowCount(5);

        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.PAGE_AXIS));
        resultsPanel.add(resultsTitleLabel);
        resultsPanel.add(resultsList);
        resultsPanel.add(statusLabel);
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
            lastSet();
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
            statusLabel.setText("Status: " + "Saved game with score " + game.getScore() + " to " + JSON_STORE);
            System.out.println("Saved game with score " + game.getScore() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            statusLabel.setText("Status: " + "Saved game with score " + game.getScore() + " to " + JSON_STORE);
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // Modified from WorkRoomApp
    // MODIFIES: this
    // EFFECTS: loads game from file
    private void loadGame() {
        try {
            game = jsonReader.read();
            statusLabel.setText("Status: " + "Loaded game with score " + game.getScore() + " from " + JSON_STORE);
            System.out.println("Loaded game with score " + game.getScore() + " from " + JSON_STORE);
        } catch (IOException e) {
            statusLabel.setText("Status: " + "Unable to read from file: " + JSON_STORE);
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: displays word entries from the previous game
    private void lastSet() {
        resultsTitleLabel.setText("Last valid game set ");
        System.out.println("Last valid game set is: ");
        getWordEntries();
    }

    // EFFECTS: displays point score from the previous game
    private void lastScore() {
        statusLabel.setText("Status: " + "Last final score is: " + game.getScore());
        System.out.println("Last final score is: " + game.getScore());
    }

    // REQUIRES: attempts >=1
    // EFFECTS: executes and prints the gameplay interface. Prints number of attempts left,
    // and whether input word is valid or not
    private void playGame() {
        System.out.println("You have " + game.getAttempts() + " attempts left");
        System.out.println("Enter a " + game.getLetterNum() + "-letter word");
        String word = textField.getText();
        if (!(game.checkIfWordValid(word))) {
            game.enterInvalidWord();
            System.out.println("Invalid Word. Your score is " + game.getScore());
            Toolkit.getDefaultToolkit().beep();
            textField.requestFocusInWindow();
            textField.selectAll();
        } else {
            game.enterValidWord(word);
            listModel.addElement(word);
            refreshScore();
            System.out.println("Valid Word. Your score is " + game.getScore());
        }
        if (game.getAttempts() > 0) {
            playGame();
        }
    }

    // EFFECTS: displays end game message with final score and word entries (game set)
    private void endGameMessage() {
        statusLabel.setText("Status: " + "Game Over. Your final score is " + game.getScore());
        System.out.println("Game Over. Your final score is " + game.getScore());
        resultsTitleLabel.setText("Your valid game set is ");
        System.out.println("Your valid game set is ");
        getWordEntries();
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

    private JButton makePlayButton() {
        JButton playButton = new JButton("Play");
        playButton.setActionCommand("play");
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game = new Game();
                String name = textField.getText();
                playGame();
                endGameMessage();
            }
        });

        return playButton;
    }

    private JButton makeScoreButton() {
        JButton scoreButton = new JButton("Score");
        scoreButton.setActionCommand("score");
        scoreButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lastScore();
            }
        });

        return scoreButton;
    }

    private JButton makeLastSetButton() {
        JButton lastSetButton = new JButton("Last Game Set");
        lastSetButton.setActionCommand("last_set");
        lastSetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lastSet();
            }
        });

        return lastSetButton;
    }

    private JButton makeSaveButton() {
        JButton saveButton = new JButton("Save");
        saveButton.setActionCommand("save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveGame();
            }
        });

        return saveButton;
    }

    private JButton makeloadButton() {
        JButton loadButton = new JButton("Load");
        loadButton.setActionCommand("load");
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadGame();
            }
        });

        return loadButton;
    }

    private JButton makeEnterButton() {
        JButton enterButton = new JButton("Enter Word");
        enterButton.setActionCommand("advance");
        enterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = textField.getText();
                game = new Game();
                playGame();
                endGameMessage();
            }
        });

        return enterButton;
    }

    private void refreshScore() {
        scoreLabel.setText("Current Score: " + game.getScore());
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }


//    @Override
//    public void actionPerformed(ActionEvent e) {
//        String word = textField.getText();
//        game.enterValidWord(word);
//        playGame();
//
//    }
}
