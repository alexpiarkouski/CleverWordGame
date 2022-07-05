package ui;

import model.Event;
import model.EventLog;
import model.Game;
import model.WordEntry;
import org.json.JSONObject;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

// Clever Word Game app
public class WordGame extends JPanel implements ActionListener {

    // Some fields modified from WorkRoomApp and Traffic Light problem
    private static final String JSON_STORE = "./data/game.json";
    private static final String JSON_STORE_HIGH_SCORE = "./data/highscore.json";
    private static final File VALID_WORDS_LIST_STORE = new File("./data/words_alpha_sorted.txt");
    private static final ImageIcon iconBad = new ImageIcon("./data/icon-bad.gif");
    private static final ImageIcon iconGood = new ImageIcon("./data/icon-good.gif");
    private static final ImageIcon iconGreat = new ImageIcon("./data/icon-great-still.gif");
    private static final int GOOD_SCORE_CUTOFF = 10;
    private static final int GREAT_SCORE_CUTOFF = 40;
    //private static final int WIDTH = 550;
    //private static final int HEIGHT = 225;

    private Game game;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JsonWriter jsonWriterHighScore;
    private JsonReader jsonReaderHighScore;

    private JFrame frame;
    private JPanel topLevelPanel;

    private DefaultListModel<String> resultsListModel;
    private JTextField textField;

    private JButton enterButton;
    private JButton resetButton;
    private JButton lastSetButton;
    private JButton saveButton;
    private JButton loadButton;

    private JLabel scoreLabel;
    private JLabel attemptsLabel;
    private JLabel statusLabel;
    private JLabel resultsTitleLabel;


    // Modified from WorkRoomApp
    // EFFECTS: runs the Word Game app
    // throws FileNotFoundException
    public WordGame() throws FileNotFoundException {
        game = new Game();
        new TxtToListWorker().execute();
        javax.swing.SwingUtilities.invokeLater(this::runWordGame);
    }

    // Partially modified from DrawingEditor
    // MODIFIES: this
    // EFFECTS: Creates game window frame, calls methods to initialize app panels, adds panels to frame
    public void runWordGame() {
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

        topLevelPanel = new JPanel();
        topLevelPanel.setLayout(new BoxLayout(topLevelPanel, BoxLayout.LINE_AXIS));
        //topLevelPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        initJson();
        initMenuButtonPanel();
        initGamePanel();

        frame.add(topLevelPanel);

        loadHighScore();

        //frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.LINE_AXIS));
        frame.setLayout(new FlowLayout(FlowLayout.CENTER));
        frame.addWindowListener(windowAdapter);

        //frame.setSize(WIDTH, HEIGHT);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        textField.requestFocusInWindow();
    }

    // MODIFIES: this
    // EFFECTS: Initializes Menu Button Panel
    private void initMenuButtonPanel() {
        JPanel menuButtonPanel = new JPanel();
        menuButtonPanel.setLayout(new BoxLayout(menuButtonPanel, BoxLayout.PAGE_AXIS));

        makeResetButton();
        //makeScoreButton();
        makeLastSetButton();
        makeSaveButton();
        makeLoadButton();

        menuButtonPanel.add(resetButton);
        menuButtonPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        //menuButtonPanel.add(scoreButton);
        //menuButtonPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        menuButtonPanel.add(lastSetButton);
        menuButtonPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        menuButtonPanel.add(saveButton);
        menuButtonPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        menuButtonPanel.add(loadButton);

        topLevelPanel.add(menuButtonPanel);
        topLevelPanel.add(Box.createRigidArea(new Dimension(10, 0)));
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
    // EFFECTS: initializes Game Panel - Score and Enter Buttons
    private void initGamePanel() {
        JPanel gameStatusPanel = new JPanel();
        JPanel gamePanel = new JPanel();

        statusLabel = new JLabel("<html>" + "New game started. " + "<br/>"
                + "Enter a " + game.getLetterNum() + "-letter word");
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.LINE_AXIS));
        gamePanel.add(initTextFieldPanel());
        gamePanel.add(Box.createRigidArea(new Dimension(10, 0)));
        gamePanel.add(initResultsPanel());
        gamePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        gamePanel.add(Box.createHorizontalGlue());

        gameStatusPanel.setLayout(new BoxLayout(gameStatusPanel, BoxLayout.PAGE_AXIS));
        gameStatusPanel.add(gamePanel);
        gameStatusPanel.add(statusLabel);

        topLevelPanel.add(gameStatusPanel);
    }

    // MODIFIES: this
    // EFFECTS: Initializes Text Field Panel
    private JPanel initTextFieldPanel() {
        JPanel textFieldPanel = new JPanel();
        JPanel textFieldEnterPanel = new JPanel();

        textField = new JTextField(10);
        textField.addActionListener(this);
        textField.setMinimumSize(new Dimension(150, 25));
        textField.setPreferredSize(new Dimension(150, 25));
        textField.setMaximumSize(new Dimension(150, 25));

        JLabel textFieldLabel = new JLabel("Text Entry Area");
        scoreLabel = new JLabel("Current Score: " + game.getScore());
        attemptsLabel = new JLabel("Attempts: " + game.getAttempts());
        JLabel statusTitleLabel = new JLabel("Status:");
        makeEnterButton();

        textFieldLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        textFieldEnterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        scoreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        attemptsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textFieldEnterPanel.setLayout(new BoxLayout(textFieldEnterPanel, BoxLayout.LINE_AXIS));
        textFieldEnterPanel.add(textField);
        textFieldEnterPanel.add(enterButton);

        textFieldPanel.setLayout(new BoxLayout(textFieldPanel, BoxLayout.PAGE_AXIS));
        textFieldPanel.add(textFieldLabel);
        textFieldPanel.add(textFieldEnterPanel);
        textFieldPanel.add(scoreLabel);
        textFieldPanel.add(attemptsLabel);
        textFieldPanel.add(Box.createVerticalGlue());
        textFieldPanel.add(statusTitleLabel);

        textFieldPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        return textFieldPanel;
        //topLevelPanel.add(textFieldPanel);
    }

    // MODIFIES: this
    // EFFECTS: initializes Results Panel and the status bar it contains
    private JPanel initResultsPanel() {
        JPanel resultsPanel = new JPanel();

        resultsTitleLabel = new JLabel("Valid Entries");
        //statusLabel = new JLabel("<html>" + "New game started. " + "<br/>"
        //        + game.getAttempts() + " attempts left. " + "<br/>"
        //        + "Enter a " + game.getLetterNum() + "-letter word");

        resultsListModel = new DefaultListModel<>();
        resultsListModel.addElement("<no data>");
        JList<String> resultsList = new JList<>(resultsListModel);
        resultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultsList.setVisibleRowCount(game.getAttempts());
        JScrollPane listScrollPane = new JScrollPane(resultsList);
        listScrollPane.setMinimumSize(new Dimension(100, 105));
        listScrollPane.setPreferredSize(new Dimension(100, 105));
        listScrollPane.setMaximumSize(new Dimension(100, 150));

        resultsTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        listScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.PAGE_AXIS));
        //resultsPanel.add(Box.createVerticalGlue());
        resultsPanel.add(resultsTitleLabel);
        resultsPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        resultsPanel.add(listScrollPane);
        resultsPanel.add(Box.createVerticalGlue());
        //resultsPanel.add(statusLabel);
        //resultsPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        //topLevelPanel.add(resultsPanel);
        //topLevelPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        resultsPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        return resultsPanel;
    }

    // REQUIRES: attempts >=0
    // EFFECTS: executes and prints the gameplay interface. Prints number of attempts left,
    // and whether input word is valid or not
    private void playGame() {
        accessMenuButtonsInGame(false);
        String word = textField.getText();
        if (!(game.checkIfWordValid(word))) {
            game.enterInvalidWord();
            Toolkit.getDefaultToolkit().beep();
            textField.selectAll();
            statusLabel.setText("Invalid Word");
        } else {
            if (game.getWordEntryList().isEmpty()) {
                resultsListModel.removeAllElements();
            }
            game.enterValidWord(word);
            resultsListModel.addElement(word);
            refreshScore();
            textField.setText("");
            statusLabel.setText("Valid Word");
        }
        textField.requestFocusInWindow();
        attemptsLabel.setText("Attempts: " + game.getAttempts());
        statusLabel.setText("<html>" + statusLabel.getText()
                + "<br/>" + "Enter a " + game.getLetterNum() + "-letter word" + "<html>");
        if (game.getAttempts() == 0) {
            endGameRoutine();
        }
    }

    // EFFECTS: calls game dialogue with varying images depending on score value and shows final game set
    private void endGameRoutine() {
        resultsTitleLabel.setText("Results");
        statusLabel.setText("<html>" + "Game over!" + "<br/>" + "Reset to play again");
        getWordEntries();
        int leaderboardPosIndex = game.findLeaderboardPositionIndex();
        //textField.setFocusable(false);
        resetButton.requestFocusInWindow();
        enterButton.setEnabled(false);
        accessMenuButtonsInGame(true);
        int score = game.getScore();
        if (leaderboardPosIndex != -1) {        // score too low to make it on the leaderboard
            saveLeaderboardEntry(leaderboardPosIndex);
            leaderboardRoutine(score);
        } else if (score < GOOD_SCORE_CUTOFF) {
            endGameDialogue("Game over. Your final score is ", iconBad);
        } else if (score < GREAT_SCORE_CUTOFF) {
            endGameDialogue("Good job! Your final score is ", iconGood);
        } else {
            endGameDialogue("Great job. Your final score is ", iconGreat);
        }
    }

    private void leaderboardRoutine(int score) {
        if (score > game.getHighScore()) {
            saveHighScore(score);
            endGameInputDialogue("<html>" + "Wow! New high score! Will save it at " + JSON_STORE_HIGH_SCORE
                    + "<br/>" + " Your final score is ", iconGreat);
        } else {
            endGameInputDialogue("<html>" + "Wow! Made it on the leaderboard. Entry will save at "
                    + JSON_STORE_HIGH_SCORE + "<br/>" + " Your final score is ", iconGreat);
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
    }

    // MODIFIES: this
    // EFFECTS: creates end game dialogue window with score, text and image
    private void endGameInputDialogue(String message, ImageIcon icon) {
        String playerName = (String) JOptionPane.showInputDialog(frame, message + game.getScore() + "<html>" + "<br/>"
                        + "Enter player name:", "Leaderboard Name Selection",
                JOptionPane.QUESTION_MESSAGE, icon, null, game.getLastPlayerName());
        game.setLastPlayerName(playerName);
    }

    // Modified from WorkRoomApp
    // MODIFIES: JSON_SCORE
    // EFFECTS: saves game to file
    private void saveGame() {
        try {
            jsonWriter.open();
            jsonWriter.write(game);
            jsonWriter.close();
            statusLabel.setText("Saved game with score " + game.getScore() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            statusLabel.setText("Unable to save to file: " + JSON_STORE);
        }
    }

    // Modified from WorkRoomApp
    // MODIFIES: this
    // EFFECTS: loads game from file
    private void loadGame() {
        try {
            game.logGameLoad();
            game = jsonReader.read();
            statusLabel.setText("Loaded game with score " + game.getScore() + " from " + JSON_STORE);
            //refreshScore();
            resultsListModel.removeAllElements();
            resultsListModel.addElement("<no data>");
            resultsTitleLabel.setText("Valid Entries");
            scoreLabel.setText("Current Score: 0");
            enterButton.setEnabled(false);
        } catch (IOException e) {
            statusLabel.setText("Unable to read from file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads high score from file
    private void loadHighScore() {
        try {
            jsonReaderHighScore.readHighScore(game);
        } catch (IOException e) {
            statusLabel.setText("Unable to read high score from file: " + JSON_STORE_HIGH_SCORE);
        }
    }

    private JSONObject loadLeaderboard() {
        try {
            return jsonReaderHighScore.readJson();
        } catch (IOException e) {
            statusLabel.setText("Unable to read leaderboard game from file: " + JSON_STORE_HIGH_SCORE);
        }
        return null;
    }

    // MODIFIES: game.json high score
    // EFFECTS: saves high score to file
    private void saveHighScore(int highScore) {
        game.setHighScore(highScore);
        JSONObject jsonObject = loadLeaderboard();
        try {
            jsonWriterHighScore.open();
            jsonWriterHighScore.writeHighScore(Objects.requireNonNull(jsonObject), highScore);
            jsonWriterHighScore.close();
        } catch (FileNotFoundException e) {
            statusLabel.setText("Unable to save to file: " + JSON_STORE_HIGH_SCORE);
        }
    }

    private void saveLeaderboardEntry(int index) {
        JSONObject jsonObject = loadLeaderboard();
        try {
            jsonWriterHighScore.open();
            if (jsonObject != null) {
                jsonWriterHighScore.writeLeaderboard(game, jsonObject, index);
            }
            jsonWriterHighScore.close();
        } catch (IOException e) {
            statusLabel.setText("Unable to write to file: " + JSON_STORE_HIGH_SCORE);
        }
    }


    // EFFECTS: displays word entries from the previous game
    private void lastSet() {
        statusLabel.setText("Last final score is: " + game.getScore());
        getWordEntries();
    }

    // EFFECTS: displays recorded word entries
    private void getWordEntries() {
        resultsListModel.removeAllElements();
        resultsTitleLabel.setText("Last Entries");
        if (game.getWordEntryList().size() == 0) {
            statusLabel.setText("Your valid game set is: empty game");
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
            new TxtToListWorker().execute();
            game.logGameReset();
            enterButton.setEnabled(true);
            accessMenuButtonsInGame(true);
            resultsTitleLabel.setText("Valid Entries");
            resultsListModel.removeAllElements();
            resultsListModel.addElement("<no data>");
            textField.setText("");
            //textField.setFocusable(true);
            textField.requestFocusInWindow();
            statusLabel.setText("<html>" + "New game started. " + "<br/>"
                    + "Enter a " + game.getLetterNum() + "-letter word");
            loadHighScore();
            refreshScore();
            attemptsLabel.setText("Attempts: " + game.getAttempts());
        });
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

    // EFFECTS: Creates an enter button and defines associated action - play game by entering a word
    private void makeEnterButton() {
        enterButton = new JButton("↵");
        enterButton.setEnabled(true);
        enterButton.setActionCommand("advance");
        enterButton.addActionListener(e -> playGame());

    }

    // EFFECTS: updates scoreLabel with the latest score
    private void refreshScore() {
        scoreLabel.setText("Current Score: " + game.getScore());
    }

    class TxtToListWorker extends SwingWorker<Void, Void> {
        @Override
        public Void doInBackground() throws FileNotFoundException {
            try {
                game.txtToList(VALID_WORDS_LIST_STORE);
            } catch (FileNotFoundException e) {
                System.out.println("Status: " + "Unable to find file: " + VALID_WORDS_LIST_STORE);
            }
            return null;
        }
    }

    // Required by ActionListener associated with JButton Class
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
