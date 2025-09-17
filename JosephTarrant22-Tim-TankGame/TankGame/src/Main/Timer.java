package Main;

import java.awt.*;

public class Timer {

    private long time = 180000;
    private long lastTime;
    private boolean endOfGame = false;
    private GamePanel gp;
    private long passedTime;

    public Timer(GamePanel gamePanel){
        gp = gamePanel;
    }

    public void resetTimer(){
        //This value of time is equivalent to 180 seconds or 3 minutes
        time = 180000;
        endOfGame = false;
        lastTime = System.currentTimeMillis();
    }

    //The update method takes time away from the time variable to create a suitable timer
    public void update(){
        long currentTime = System.currentTimeMillis();
        time = time - (currentTime - lastTime);
        lastTime = currentTime;
        if (time <= 0){
            endOfGame = true;
        }
        //This will store the time counting up instead of counting down
        passedTime = 180 - time/1000;
    }

    public void paintComponent(Graphics2D g2){
        g2.setColor(Color.BLACK);
        //This will show time counting down for the 2 player game mode
        if (gp.getGameState() == gp.getPlayState()) {
            g2.drawString("Time: " + time / 1000, gp.getTileSize()*21, gp.getTileSize()*32);
        }
        //This will show time counting up for the time trial game mode
        if (gp.getGameState() == gp.getTimeTrialState()){
            g2.drawString("Time: " + passedTime, gp.getTileSize()*21, gp.getTileSize()*32);
        }
    }
    public boolean isEndOfGame(){
        return endOfGame;
    }

    public int getPassedTime() {
        return (int)passedTime;
    }
}
