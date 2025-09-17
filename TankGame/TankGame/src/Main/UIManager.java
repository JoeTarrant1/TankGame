package Main;

import java.awt.*;

public class UIManager {
    GamePanel gp;
    private final int centreX;
    private final int centreY;
    private int pauseCounter = 0;
    private int startCounter = 0;
    private int endCounter = 0;

    public UIManager(GamePanel gamePanel){
        gp = gamePanel;
        centreX = gp.getWindowWidth() / 2;
        centreY = gp.getWindowHeight() / 2;
    }
    public void drawPauseMenu(Graphics2D g2){
        int astrixLocation = 250;

        g2.drawString("Paused", centreX, 200);
        g2.drawString("Resume", centreX, 250);
        g2.drawString("Restart", centreX, 275);
        g2.drawString("Exit", centreX, 300);

        //This decides where the pointer for the buttons is located
        switch(pauseCounter){
            case 0:
                astrixLocation = 250;
                break;
            case 1:
                astrixLocation = 275;
                break;
            case 2:
                astrixLocation = 300;
                break;
        }

        g2.drawString("*", centreX - 15, astrixLocation);

    }
    public void incrementPauseCounter(){
        pauseCounter++;
        if(pauseCounter > 2){
            pauseCounter = 0;
        }
    }
    public void decrementPauseCounter(){
        pauseCounter--;
        if(pauseCounter < 0){
            pauseCounter = 2;
        }
    }
    //This will decide what action needs to be taken depending on what button the user presses
    public void pauseAction(){
        //The pause counter represents the buttons, 0 being resume, 1 being restart, 2 being exit
        if (pauseCounter == 0){
            //This will change the game state from paused to play
            gp.gameStatePaused();
        }
        else if(pauseCounter == 1){
            //This will restart the 2 player game mode
            if (gp.getLastGameState() == gp.getPlayState()) {
                gp.startGame();
            }
            //This will restart the time trial mode
            if (gp.getLastGameState() == gp.getTimeTrialState()){
                gp.startTimeTrial();
            }
        }
        else if(pauseCounter == 2){
            //This will change the game state to start state (start menu)
            gp.exitToMenu();
        }
    }
    //This is the same as the pause menu, but it will draw different buttons
    public void drawStartMenu(Graphics2D g2){
        int astrixLocation = 0;

        g2.drawString("Tank Game", centreX, 200);
        g2.drawString("2 Player Mode", centreX, 250);
        g2.drawString("1 Player Mode", centreX, 275);
        g2.drawString("Time Trial Mode", centreX, 300);

        switch(startCounter){
            case 0:
                astrixLocation = 250;
                break;
            case 1:
                astrixLocation = 275;
                break;
            case 2:
                astrixLocation = 300;
                break;
        }

        g2.drawString("*", centreX - 15, astrixLocation);
    }
    public void incrementStartCounter(){
        startCounter++;
        if(startCounter > 2){
            startCounter = 0;
        }
    }
    public void decrementStartCounter(){
        startCounter--;
        if(startCounter < 0){
            startCounter = 2;
        }
    }
    //This will decide what to do when a button is pressed on the start menu
    public void startAction(){
        //Like the pause menu, the counter corresponds to the buttons
        if (startCounter == 0){
            //This starts the 2 player game mode
            gp.startGame();
        }
        else if (startCounter == 1){

        }
        else if (startCounter == 2){
            //This starts the time trial game mode
            gp.startTimeTrial();
        }
    }
    //This will draw the buttons for the end of game menu
    public void drawEndMenu(Graphics2D g2){
        int astrixLocation = 0;

        if (gp.getLastGameState() == gp.getPlayState()) {
            g2.drawString("Winner: " + gp.getWinner(), centreX, 200);
        }
        if (gp.getLastGameState() == gp.getTimeTrialState()){
            g2.drawString("Targets: " + gp.getTargets(), centreX, 175);
            g2.drawString("Time: " + gp.getPassedTime(), centreX, 200);
        }
        g2.drawString("Replay", centreX, 250);
        g2.drawString("Exit", centreX, 275);

        switch(endCounter){
            case 0:
                astrixLocation = 250;
                break;
            case 1:
                astrixLocation = 275;
                break;
        }

        g2.drawString("*", centreX - 15, astrixLocation);
    }
    public void changeEndCounter(){
        if (endCounter == 0){
            endCounter = 1;
        }
        else{
            endCounter = 0;
        }
    }
    //This will decide the actions that need to be carried out when a button is pressed
    public void endActions(){
        //Each counter position corresponds to a button, 0 being restart and 1 being exit
        if (endCounter == 0){
            //This restarts the 2 player game mode
            if (gp.getLastGameState() == gp.getPlayState()) {
                gp.startGame();
            }
            //This restarts the time trial game mode
            if (gp.getLastGameState() == gp.getTimeTrialState()){
                gp.startTimeTrial();
            }
        }
        else{
            //This returns the player to the start menu
            gp.setGameState(gp.getStartState());
        }
    }
}
