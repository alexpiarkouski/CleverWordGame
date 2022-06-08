package ui;

import model.Event;
import model.EventLog;
import model.Game;
import model.WordEntry;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

// Clever Word Game app
public class WordGame extends JPanel implements ActionListener {

    // Some fields modified from WorkRoomApp and Traffic Light problem
    private static final String JSON_STORE = "./data/game.json";
    private static final String JSON_STORE_HIGH_SCORE = "./data/highscore.json";
    private static final ImageIcon iconBad = new ImageIcon("./data/icon-bad.gif");
    private static final ImageIcon iconGood = new ImageIcon("./data/icon-good.gif");
    private static final ImageIcon iconGreat = new ImageIcon("./data/icon-great.gif");
    private static final int GOOD_SCORE_CUTOFF = 1;
    private static final int GREAT_SCORE_CUTOFF = 30;
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 250;

    private Game game;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JsonWriter jsonWriterHighScore;
    private JsonReader jsonReaderHighScore;

    private JFrame frame;

    private DefaultListModel<String> entryListModel;
    private DefaultListModel<String> resultsListModel;
    private JTextField textField;

    private JButton enterButton;
    private JButton resetButton;
    private JButton scoreButton;
    private JButton lastSetButton;
    private JButton saveButton;
    private JButton loadButton;

    private JLabel scoreLabel;
    private JLabel statusLabel;
    private JLabel resultsTitleLabel;


    // Modified from WorkRoomApp
    // EFFECTS: runs the Word Game app
    // throws FileNotFoundException
    public WordGame() throws FileNotFoundException {
        runWordGame();
    }

    // Partially modified from DrawingEditor
    // MODIFIES: this
    // EFFECTS: Creates game window frame, calls methods to initialize app panels, adds panels to frame
    public void runWordGame() {
        game = new Game();

        frame = new JFrame("Clever Word Game");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        WindowAdapter windowAdapter = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for (Event next : EventLog.getInstance()) {
                    System.out.println(next.toString());
                }
                System.exit(0);
            }
        };

        initJson();
        initMenuButtonPanel();
        initEntryListPanel();
        initTextFieldPanel();
        initGamePanel();
        initResultsPanel();

        loadHighScore();

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.LINE_AXIS));
        frame.addWindowListener(windowAdapter);

        //Display the window.
        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);
        textField.requestFocusInWindow();
    }

    // MODIFIES: this
    // EFFECTS: Initializes Menu Button Panel
    private void initMenuButtonPanel() {
        JPanel menuButtonPanel = new JPanel();
        menuButtonPanel.setLayout(new BoxLayout(menuButtonPanel, BoxLayout.PAGE_AXIS));

        makeResetButton();
        makeScoreButton();
        makeLastSetButton();
        makeSaveButton();
        makeLoadButton();

        menuButtonPanel.add(resetButton);
        menuButtonPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        menuButtonPanel.add(scoreButton);
        menuButtonPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        menuButtonPanel.add(lastSetButton);
        menuButtonPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        menuButtonPanel.add(saveButton);
        menuButtonPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        menuButtonPanel.add(loadButton);

        frame.add(menuButtonPanel);

    }

    // modified from TellerApp and WorkRoomApp
    // MODIFIES: this
    // EFFECTS: initializes Json Readers and Writers
    private void initJson() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriterHighScore = new JsonWriter(JSON_STORE_HIGH_SCORE);
        jsonReaderHighScore = new JsonReader(JSON_STORE_HIGH_SCORE);
    }

    // MODIFIES: this
    // EFFECTS: Initializes List of Entries Panel
    private void initEntryListPanel() {
        JPanel entryListPanel = new JPanel();

        entryListModel = new DefaultListModel<>();
        entryListModel.addElement("<no words>");
        JList<String> list = new JList<>(entryListModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list); // to make list visible at all times event when empty

        JLabel entryListLabel = new JLabel("Current Entries");

        entryListPanel.setLayout(new BoxLayout(entryListPanel, BoxLayout.PAGE_AXIS));
        entryListPanel.add(entryListLabel);
        entryListPanel.add(listScrollPane);

        frame.add(entryListPanel);
    }

    // MODIFIES: this
    // EFFECTS: Initializes Text Field Panel
    private void initTextFieldPanel() {
        JPanel textFieldPanel = new JPanel();

        textField = new JTextField(10);
        textField.addActionListener(this);

        JLabel textFieldLabel = new JLabel("Text Entry Area");

        textFieldPanel.setLayout(new BoxLayout(textFieldPanel, BoxLayout.PAGE_AXIS));
        textFieldPanel.add(textFieldLabel);
        textFieldPanel.add(textField);

        frame.add(textFieldPanel);
    }

    // MODIFIES: this
    // EFFECTS: initializes Game Panel - Score and Enter Buttons
    private void initGamePanel() {
        JPanel gamePanel = new JPanel();
        makeEnterButton();

        scoreLabel = new JLabel("Current Score: " + game.getScore());

        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.LINE_AXIS));
        gamePanel.add(Box.createRigidArea(new Dimension(10, 0)));
        gamePanel.add(enterButton);
        gamePanel.add(scoreLabel);

        frame.add(gamePanel);
    }


    // MODIFIES: this
    // EFFECTS: initializes Results Panel and the status bar it contains
    private void initResultsPanel() {
        JPanel resultsPanel = new JPanel();

        resultsTitleLabel = new JLabel("Results Panel");
        statusLabel = new JLabel("<html>" + "New game started. " + "<br/>"
                + game.getAttempts() + " attempts left. " + "<br/>"
                + "Enter a " + game.getLetterNum() + "-letter word");

        resultsListModel = new DefaultListModel<>();
        resultsListModel.addElement("<no data>");
        JList<String> resultsList = new JList<>(resultsListModel);
        resultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultsList.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(resultsList);

        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.PAGE_AXIS));
        resultsPanel.add(resultsTitleLabel);
        resultsPanel.add(listScrollPane);
        resultsPanel.add(statusLabel);

        frame.add(resultsPanel);
    }

    // Modified from WorkRoomApp
    // MODIFIES: JSON_SCORE
    // EFFECTS: saves game to file
    private void saveGame() {
        try {
            jsonWriter.open();
            jsonWriter.write(game);
            jsonWriter.close();
            statusLabel.setText("Status: " + "Saved game with score " + game.getScore() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            statusLabel.setText("Status: " + "Unable to save to file: " + JSON_STORE);
        }
    }

    // Modified from WorkRoomApp
    // MODIFIES: this
    // EFFECTS: loads game from file
    private void loadGame() {
        try {
            game.logGameLoad();
            game = jsonReader.read();
            statusLabel.setText("Status: " + "Loaded game with score " + game.getScore() + " from " + JSON_STORE);
            refreshScore();
            enterButton.setEnabled(false);
        } catch (IOException e) {
            statusLabel.setText("Status: " + "Unable to read from file: " + JSON_STORE);
        }
    }

    private void loadHighScore() {
        try {
            jsonReaderHighScore.readHighScore(game);
        } catch (IOException e) {
            statusLabel.setText("Status: " + "Unable to read high score from file: " + JSON_STORE_HIGH_SCORE);
        }
    }

    // MODIFIES: game.json high score
    // EFFECTS: saves high score to file
    private void saveHighScore(int highScore) {
        try {
            jsonWriterHighScore.open();
            jsonWriterHighScore.writeHighScore(highScore);
            jsonWriterHighScore.close();
        } catch (FileNotFoundException e) {
            statusLabel.setText("Status: " + "Unable to save to file: " + JSON_STORE_HIGH_SCORE);
        }
    }

    // EFFECTS: displays word entries from the previous game
    private void lastSet() {
        resultsTitleLabel.setText("Last valid game set ");
        getWordEntries();
    }

    // EFFECTS: displays point score from the previous game
    private void lastScore() {
        statusLabel.setText("Status: " + "Last final score is: " + game.getScore());
    }

    // REQUIRES: attempts >=0
    // EFFECTS: executes and prints the gameplay interface. Prints number of attempts left,
    // and whether input word is valid or not
    private void playGame() {
        accessMenuButtonsInGame(false);
        String word = textField.getText();
        if (!(game.checkIfWordValid(word))) {
            game.enterInvalidWord();
            statusLabel.setText("Status: " + "Invalid Word");
            Toolkit.getDefaultToolkit().beep();
            textField.requestFocusInWindow();
            textField.selectAll();
        } else {
            if (game.getWordEntryList().isEmpty()) {
                entryListModel.removeAllElements();
            }
            game.enterValidWord(word);
            entryListModel.addElement(word);
            refreshScore();
            textField.setText("");
            textField.requestFocusInWindow();
            statusLabel.setText("Status: " + "Valid Word");
        }
        statusLabel.setText("<html>" + statusLabel.getText() + "<br/>" + game.getAttempts() + " attempts left. "
                        + "<br/>" + "Enter a " + game.getLetterNum() + "-letter word" + "<html>");
        if (game.getAttempts() == 0) {
            endGameMessage();
            enterButton.setEnabled(false);
            accessMenuButtonsInGame(true);
        }
    }

    // EFFECTS: calls game dialogue with varying images depending on score value and shows final game set
    private void endGameMessage() {
        resultsTitleLabel.setText("Your valid game set is ");
        getWordEntries();
        if (game.getScore() < GOOD_SCORE_CUTOFF) {
            endGameDialogue("Game over. Your final score is ", iconBad);
        } else if (game.getScore() < GREAT_SCORE_CUTOFF) {
            endGameDialogue("Good job! Your final score is ", iconGood);
        } else if (game.getScore() > game.getHighScore()) {
            saveHighScore(game.getScore());
            game.setHighScore(game.getScore());
            endGameDialogue("<html>" + "Wow! New high score! Saved it at " + JSON_STORE_HIGH_SCORE
                            + "<br/>" + " Your final score is ", iconGreat);
        } else {
            endGameDialogue("Great job. Your final score is ", iconGreat);
        }
    }

    // MODIFIES: this
    // EFFECTS: creates end game dialogue window with score, text and image
    private void endGameDialogue(String message, ImageIcon icon) {
        JOptionPane.showMessageDialog(frame,
                message + game.getScore(),
                "Game Over!",
                JOptionPane.INFORMATION_MESSAGE,
                icon);
        statusLabel.setText("<html>" + "Game over!" + "<br/>" + "Reset to play again");
    }

    // EFFECTS: displays recorded word entries
    private void getWordEntries() {
        resultsListModel.removeAllElements();
        if (game.getWordEntryList().size() == 0) {
            resultsTitleLabel.setText("Your valid game set is: empty game");
            resultsListModel.addElement("<no data>");
        } else {
            for (WordEntry wordEntry : game.getWordEntryList()) {
                resultsListModel.addElement(wordEntry.getWord() + ", "
                        + wordEntry.getWordValue() + "; ");
            }
        }
    }

    // EFFECTS: Sets whether menu buttons are enabled equal to onOffStatus (excluding reset button)
    private void accessMenuButtonsInGame(boolean onOffStatus) {
        scoreButton.setEnabled(onOffStatus);
        lastSetButton.setEnabled(onOffStatus);
        saveButton.setEnabled(onOffStatus);
        loadButton.setEnabled(onOffStatus);
    }

    // Modified from Traffic Light Project
    // EFFECTS: Creates a reset button and defines associated action - reset game, score, last game set
    private void makeResetButton() {
        resetButton = new JButton("Reset");
        resetButton.setActionCommand("reset");
        resetButton.addActionListener(e -> {
            game = new Game();
            game.logGameReset();
            enterButton.setEnabled(true);
            entryListModel.removeAllElements();
            entryListModel.addElement("<no words>");
            resultsListModel.removeAllElements();
            resultsListModel.addElement("<no data>");
            textField.setText("");
            textField.requestFocusInWindow();
            statusLabel.setText("<html>" + "New game started. " + "<br/>"
                    + game.getAttempts() + " attempts left. " + "<br/>"
                    + "Enter a " + game.getLetterNum() + "-letter word");
            resultsTitleLabel.setText("Results");
            refreshScore();
        });
    }

    // Modified from Traffic Light Project
    // EFFECTS: Creates a last score button and defines associated action - display last score
    private void makeScoreButton() {
        scoreButton = new JButton("Last Score");
        scoreButton.setActionCommand("score");
        scoreButton.addActionListener(e -> lastScore());
    }

    // Modified from Traffic Light Project
    // EFFECTS: Creates a last game set button and defines associated action - display last game set
    private void makeLastSetButton() {
        lastSetButton = new JButton("Last Game Set");
        lastSetButton.setActionCommand("last_set");
        lastSetButton.addActionListener(e -> lastSet());
    }

    // Modified from Traffic Light Project
    // EFFECTS: Creates a save button and defines associated action - save game
    private void makeSaveButton() {
        saveButton = new JButton("Save");
        saveButton.setActionCommand("save");
        saveButton.addActionListener(e -> saveGame());
    }

    // Modified from Traffic Light Project
    // EFFECTS: Creates a load button and defines associated action - load game
    private void makeLoadButton() {
        loadButton = new JButton("Load");
        loadButton.setActionCommand("load");
        loadButton.addActionListener(e -> loadGame());
    }

    // Modified from Traffic Light Project
    // EFFECTS: Creates an enter button and defines associated action - play game by entering a word
    private void makeEnterButton() {
        enterButton = new JButton("Enter Word");
        enterButton.setEnabled(true);
        enterButton.setActionCommand("advance");
        enterButton.addActionListener(e -> playGame());

    }

    // EFFECTS: updates scoreLabel with latest score
    private void refreshScore() {
        scoreLabel.setText("Current Score: " + game.getScore());
    }

    // Required by ActionListener associated with JButton Class
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
