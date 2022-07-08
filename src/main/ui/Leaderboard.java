package ui;

import model.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Leaderboard extends JPanel implements ActionListener {

    //private static JFrame controllingFrame;
    private final JFrame controllingFrame; //needed for dialogs
    private static final String OK = "ok";
    private static final String RESET = "reset_leaderboard";

    public Leaderboard(JFrame frame, Object[][] data, String[] columnNames, Game game) {
        controllingFrame = frame;

        JTable table = new JTable(data, columnNames);
        //String[] options = new String[]{"Reset ", "OK"};
        JComponent buttonPane = createButtonPanel();

        JLabel highScoreLabel = new JLabel("High score: " + game.getHighScore());
        JPanel textPane = new JPanel();
        textPane.setLayout(new BoxLayout(textPane, BoxLayout.PAGE_AXIS));
        textPane.add(highScoreLabel);
        textPane.add(table.getTableHeader());
        textPane.add(table);
        highScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(textPane);
        add(buttonPane);
    }

    protected JComponent createButtonPanel() {
        JPanel p = new JPanel(new GridLayout(0,1));
        JButton okButton = new JButton("OK");
        JButton resetButton = new JButton("Reset");

        okButton.setActionCommand(OK);
        resetButton.setActionCommand(RESET);
        okButton.addActionListener(this);
        resetButton.addActionListener(this);

        p.add(okButton);
        p.add(resetButton);

        return p;
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private void createAndShowPW() {
        //Create and set up the window.
        JFrame frame = new JFrame("Leaderboard Reset");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Create and set up the content pane.
        final PasswordHandler newContentPane = new PasswordHandler(controllingFrame, frame);
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        //Make sure the focus goes to the right component
        //whenever the frame is initially given the focus.
        frame.addWindowListener(new WindowAdapter() {
            public void windowActivated(WindowEvent e) {
                newContentPane.resetFocus();
            }
        });

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        frame.toFront();
        frame.requestFocus();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (RESET.equals(cmd)) {
            controllingFrame.setVisible(true);
            createAndShowPW();
        } else {
            controllingFrame.dispose();
        }
    }

    //Must be called from the event dispatch thread.
    protected void resetFocus() {
        controllingFrame.requestFocusInWindow();
    }
}
