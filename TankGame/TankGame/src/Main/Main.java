package Main;

import javax.swing.*;

public class Main{
    public static void main(String[] args){

        JFrame frame = new JFrame();
        frame.setTitle("Tank Game");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.pack();

        gamePanel.startGameThread();

        frame.setVisible(true);

    }
}
