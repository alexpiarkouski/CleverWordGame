package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class PasswordHandler extends JPanel implements ActionListener {

    private static final String OK = "ok";
    private static final String HELP = "help";

    private final JFrame controllingFrame; //needed for dialogs
    private final JPasswordField passwordField;

    public PasswordHandler(JFrame f) {
        controllingFrame = f;

        passwordField = new JPasswordField(10);
        passwordField.setActionCommand(OK);
        passwordField.addActionListener(this);

        JLabel label = new JLabel("Enter admin password: ");
        label.setLabelFor(passwordField);

        JComponent buttonPane = createButtonPanel();

        //Lay out everything.
        JPanel textPane = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        textPane.add(label);
        textPane.add(passwordField);

        add(textPane);
        add(buttonPane);
    }

    protected JComponent createButtonPanel() {
        JPanel p = new JPanel(new GridLayout(0,1));
        JButton okButton = new JButton("OK");
        JButton helpButton = new JButton("Help");

        okButton.setActionCommand(OK);
        helpButton.setActionCommand(HELP);
        okButton.addActionListener(this);
        helpButton.addActionListener(this);

        p.add(okButton);
        p.add(helpButton);

        return p;
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (OK.equals(cmd)) { //Process the password.
            char[] input = passwordField.getPassword();
            if (isPasswordCorrect(input)) {
                JOptionPane.showMessageDialog(controllingFrame,
                        "Success! You typed the right password.");
            } else {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(controllingFrame,
                        "Invalid password. Try again.",
                        "Error Message",
                        JOptionPane.ERROR_MESSAGE);
            }

            //Zero out the possible password, for security.
            Arrays.fill(input, '0');

            passwordField.selectAll();
            resetFocus();
        } else { //The user has asked for help.
            JOptionPane.showMessageDialog(controllingFrame,
                    "You can get the password by searching \n"
                            + "the source code for the string \"correctPassword\".");
        }
    }

    /**
     * Checks the passed-in array against the correct password.
     * After this method returns, you should invoke eraseArray
     * on the passed-in array.
     */
    private static boolean isPasswordCorrect(char[] input) {
        boolean isCorrect;
        char[] correctPassword = { '1', '2', '3' };

        if (input.length != correctPassword.length) {
            isCorrect = false;
        } else {
            isCorrect = Arrays.equals(input, correctPassword);
        }

        //Zero out the password.
        Arrays.fill(correctPassword,'0');

        return isCorrect;
    }

    //Must be called from the event dispatch thread.
    protected void resetFocus() {
        passwordField.requestFocusInWindow();
    }
}
