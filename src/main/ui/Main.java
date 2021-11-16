package ui;

import java.io.FileNotFoundException;

// Modified from WorkRoomApp
public class Main {

    public static void main(String[] args) {
        try {
            new WordGame();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run application: file not found");
        }
    }
}
