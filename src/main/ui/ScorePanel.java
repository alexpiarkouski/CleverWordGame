package ui;

import model.Game;

import javax.swing.*;
import java.awt.*;

/*
 * Represents the panel in which the scoreboard is displayed.
 */
public class ScorePanel extends JPanel {
    private static final String SCORE_TXT = "Current Score: ";
    private static final String ATTEMPTS_TXT = "Attempts remaining: ";
    private static final int LBL_WIDTH = 200;
    private static final int LBL_HEIGHT = 30;
    private final Game game;
    private final JLabel scoreLabel;
    private final JLabel attemptsLabel;

    // Constructs a score panel
    // effects: sets the background colour and draws the initial labels;
    //          updates this with the game whose score is to be displayed
    public ScorePanel(Game g) {
        game = g;
        setBackground(new Color(180, 180, 180));
        scoreLabel = new JLabel(SCORE_TXT + game.getScore());
        scoreLabel.setPreferredSize(new Dimension(LBL_WIDTH, LBL_HEIGHT));
        attemptsLabel = new JLabel(ATTEMPTS_TXT + game.getAttempts());
        attemptsLabel.setPreferredSize(new Dimension(LBL_WIDTH, LBL_HEIGHT));
        add(scoreLabel);
        add(Box.createHorizontalStrut(10));
        add(attemptsLabel);
    }

    // Updates the score panel
    // modifies: this
    // effects:  updates number of invaders shot and number of missiles
    //           remaining to reflect current state of game
    public void update() {
        scoreLabel.setText(SCORE_TXT + game.getScore());
        attemptsLabel.setText(ATTEMPTS_TXT + game.getAttempts());
        repaint();
    }
}
