package ui;

import model.Game;
import model.WordEntry;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.util.Scanner;

// Clever Word Game app
public class WordGame extends JPanel implements ActionListener {

    // Some fields modified from WorkRoomApp
    private static final String JSON_STORE = "./data/game.json";
    //private static final ImageIcon icon = new ImageIcon("./data/icon.gif");
    private static final ImageIcon iconBad = new ImageIcon("./data/icon-bad.gif");
    private static final ImageIcon iconGood = new ImageIcon("./data/icon-good.gif");
    private static final ImageIcon iconGreat = new ImageIcon("./data/icon-great.gif");
    private static final int GOOD_SCORE_CUTOFF = 1;
    private static final int GREAT_SCORE_CUTOFF = 30;
    // private Scanner input;
    private Game game;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 250;

    private JFrame frame;
    private JPanel menuButtonPanel;
    private JPanel gamePanel;
    private JPanel resultsPanel;
    private JList<String> list;
    private DefaultListModel<String> listModel;
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
    private JList<String> resultsList;

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
        game = new Game();

        frame = new JFrame("Clever Word Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

    private void initMenuButtonPanel() {
        menuButtonPanel = new JPanel();
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

    }

    // modified from TellerApp and WorkRoomApp
    // MODIFIES: this
    // EFFECTS: initializes Json Reader and Writer
    private void initJson() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // MODIFIES: this
    // EFFECTS: initializes Game Panel
    private void initGamePanel() {
        gamePanel = new JPanel();
        makeEnterButton();

        listModel = new DefaultListModel<>();
        listModel.addElement("<no words>");
        list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);

        textField = new JTextField(10);
        textField.addActionListener(this);

        scoreLabel = new JLabel("Current Score: " + game.getScore());


        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.LINE_AXIS));
        gamePanel.add(listScrollPane);
        gamePanel.add(Box.createHorizontalGlue());
        gamePanel.add(textField);
        gamePanel.add(Box.createRigidArea(new Dimension(10, 0)));
        gamePanel.add(enterButton);
        gamePanel.add(scoreLabel);
        System.out.println("You have " + game.getAttempts() + " attempts left");
        System.out.println("Enter a " + game.getLetterNum() + "-letter word");
    }

    // MODIFIES: this
    // EFFECTS: initializes Results Panel
    private void initResultsPanel() {
        resultsPanel = new JPanel();

        resultsTitleLabel = new JLabel("Results Panel");
        statusLabel = new JLabel("New game started. " + game.getAttempts() + " attempts left. "
                + "Enter a " + game.getLetterNum() + "-letter word");
        //statusLabel.setText("<html>" + statusLabel.getText() + "<br/>" + game.getAttempts() + " attempts left. "
        //                + "<br/>" + "Enter a " + game.getLetterNum() + "-letter word" + "<html>");

        resultsListModel = new DefaultListModel<>();
        resultsListModel.addElement("<no data>");
        resultsList = new JList<>(resultsListModel);
        resultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultsList.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(resultsList);

        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.PAGE_AXIS));
        resultsPanel.add(resultsTitleLabel);
        resultsPanel.add(listScrollPane);
        resultsPanel.add(statusLabel);
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
            refreshScore();
            enterButton.setEnabled(false);
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
            game.enterValidWord(word);
            listModel.addElement(word);
            refreshScore();
            textField.setText("");
            textField.requestFocusInWindow();
            statusLabel.setText("Status: " + "Valid Word");
        }
        //statusLabel.setText("<html>" + statusLabel.getText() + "<br/>" + game.getAttempts() + " attempts left. "
        //                + "<br/>" + "Enter a " + game.getLetterNum() + "-letter word" + "<html>");
        statusLabel.setText(statusLabel.getText() + ". " + game.getAttempts() + " attempts left. "
                + "Enter a " + game.getLetterNum() + "-letter word");
        if (game.getAttempts() == 0) {
            endGameMessage();
            enterButton.setEnabled(false);
            accessMenuButtonsInGame(true);
        }
    }

    // EFFECTS: displays end game message with final score and word entries (game set)
    private void endGameMessage() {
        //statusLabel.setText("Status: " + "Game Over. Your final score is " + game.getScore());
        System.out.println("Game Over. Your final score is " + game.getScore());
        resultsTitleLabel.setText("Your valid game set is ");
        System.out.println("Your valid game set is ");
        getWordEntries();
        if (game.getScore() < GOOD_SCORE_CUTOFF) {
            endGameDialogue(iconBad);
        } else if (game.getScore() < GREAT_SCORE_CUTOFF) {
            endGameDialogue(iconGood);
        } else {
            endGameDialogue(iconGreat);
        }
    }

    private void endGameDialogue(ImageIcon icon) {
        JOptionPane.showMessageDialog(frame,
                "Game over. Your final score is " + game.getScore(),
                "Game Over!",
                JOptionPane.INFORMATION_MESSAGE,
                icon);
    }

    // EFFECTS: displays recorded word entries
    private void getWordEntries() {
        resultsListModel.removeAllElements();
        if (game.getWordEntryList().size() == 0) {
            resultsTitleLabel.setText("Your valid game set is: empty game");
            resultsListModel.addElement("<no data>");
            System.out.println("Empty game");
        } else {
            for (WordEntry wordEntry : game.getWordEntryList()) {
                resultsListModel.addElement(wordEntry.getWord() + ", "
                        + wordEntry.getWordValue() + "; ");
            }
        }
    }

    private void accessMenuButtonsInGame(boolean onOffStatus) {
        scoreButton.setEnabled(onOffStatus);
        lastSetButton.setEnabled(onOffStatus);
        saveButton.setEnabled(onOffStatus);
        loadButton.setEnabled(onOffStatus);
    }

    private void makeResetButton() {
        resetButton = new JButton("Reset");
        resetButton.setActionCommand("reset");
        resetButton.addActionListener(e -> {
            game = new Game();
            enterButton.setEnabled(true);
            listModel.removeAllElements();
            listModel.addElement("<no words>");
            resultsListModel.removeAllElements();
            resultsListModel.addElement("<no data>");
            statusLabel.setText("No status");
            resultsTitleLabel.setText("Results");
            refreshScore();
            System.out.println("You have " + game.getAttempts() + " attempts left");
            System.out.println("Enter a " + game.getLetterNum() + "-letter word");
        });
    }

    private void makeScoreButton() {
        scoreButton = new JButton("Score");
        scoreButton.setActionCommand("score");
        scoreButton.addActionListener(e -> lastScore());
    }

    private void makeLastSetButton() {
        lastSetButton = new JButton("Last Game Set");
        lastSetButton.setActionCommand("last_set");
        lastSetButton.addActionListener(e -> lastSet());
    }

    private void makeSaveButton() {
        saveButton = new JButton("Save");
        saveButton.setActionCommand("save");
        saveButton.addActionListener(e -> saveGame());
    }

    private void makeLoadButton() {
        loadButton = new JButton("Load");
        loadButton.setActionCommand("load");
        loadButton.addActionListener(e -> loadGame());
    }

    private void makeEnterButton() {
        enterButton = new JButton("Enter Word");
        enterButton.setEnabled(true);
        enterButton.setActionCommand("advance");
        enterButton.addActionListener(e -> playGame());

    }

    private void refreshScore() {
        scoreLabel.setText("Current Score: " + game.getScore());
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
