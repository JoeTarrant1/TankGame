package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    private boolean upPressed, downPressed, leftPressed, rightPressed;
    private boolean wPressed, sPressed, aPressed, dPressed;
    private boolean onePressed, mPressed;
    GamePanel gp;
    UIManager UI;
    public KeyHandler(GamePanel gamePanel, UIManager UI){
        gp = gamePanel;
        this.UI = UI;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
    //This method detects when a user presses down a key
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        //This will read inputs from the user when the game is in play state
        if (gp.getGameState() == gp.getPlayState() || gp.getGameState() == gp.getTimeTrialState()) {
            //All of these if statements will check if a key has been pressed and will perform the appropriate action
            if (code == KeyEvent.VK_W) {
                wPressed = true;
            }
            if (code == KeyEvent.VK_S) {
                sPressed = true;
            }
            if (code == KeyEvent.VK_A) {
                aPressed = true;
            }
            if (code == KeyEvent.VK_D) {
                dPressed = true;
            }

            if (code == KeyEvent.VK_P) {
                gp.gameStatePaused();
            }

            if (code == KeyEvent.VK_UP) {
                upPressed = true;
            }
            if (code == KeyEvent.VK_DOWN) {
                downPressed = true;
            }
            if (code == KeyEvent.VK_LEFT) {
                leftPressed = true;
            }
            if (code == KeyEvent.VK_RIGHT) {
                rightPressed = true;
            }

            if (code == KeyEvent.VK_1){
                onePressed = true;
            }
            if (code == KeyEvent.VK_M){
                mPressed = true;
            }
        }
        //This will read key inputs when the game is in a paused state
        else if (gp.getGameState() == gp.getPauseState()){
            if (code == KeyEvent.VK_W){
                UI.decrementPauseCounter();
            }
            if (code == KeyEvent.VK_S){
                UI.incrementPauseCounter();
            }
            if (code == KeyEvent.VK_UP){
                UI.decrementPauseCounter();
            }
            if (code == KeyEvent.VK_DOWN){
                UI.incrementPauseCounter();
            }
            if (code == KeyEvent.VK_ENTER){
                UI.pauseAction();
            }
            if (code == KeyEvent.VK_P) {
                gp.gameStatePaused();
            }
        }
        //This will read key inputs when the game is in a start menu state
        else if (gp.getGameState() == gp.getStartState()){
            if (code == KeyEvent.VK_W){
                UI.decrementStartCounter();
            }
            if (code == KeyEvent.VK_S){
                UI.incrementStartCounter();
            }
            if (code == KeyEvent.VK_UP){
                UI.decrementStartCounter();
            }
            if (code == KeyEvent.VK_DOWN){
                UI.incrementStartCounter();
            }
            if (code == KeyEvent.VK_ENTER){
                UI.startAction();
            }
        }
        //This will read key inputs when the user is in the end of game menu
        else if (gp.getGameState() == gp.getEndOfGameState()){
            if (code == KeyEvent.VK_W){
                UI.changeEndCounter();
            }
            if (code == KeyEvent.VK_S){
                UI.changeEndCounter();
            }
            if (code == KeyEvent.VK_UP){
                UI.changeEndCounter();
            }
            if (code == KeyEvent.VK_DOWN){
                UI.changeEndCounter();
            }
            if (code == KeyEvent.VK_ENTER){
                UI.endActions();
            }
        }
    }
    //This method detects when a user has released a key
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if(gp.getGameState() == gp.getPlayState() || gp.getGameState() == gp.getTimeTrialState()) {
            if (code == KeyEvent.VK_W) {
                wPressed = false;
            }
            if (code == KeyEvent.VK_S) {
                sPressed = false;
            }
            if (code == KeyEvent.VK_A) {
                aPressed = false;
            }
            if (code == KeyEvent.VK_D) {
                dPressed = false;
            }

            if (code == KeyEvent.VK_UP) {
                upPressed = false;
            }
            if (code == KeyEvent.VK_DOWN) {
                downPressed = false;
            }
            if (code == KeyEvent.VK_LEFT) {
                leftPressed = false;
            }
            if (code == KeyEvent.VK_RIGHT) {
                rightPressed = false;
            }

            if (code == KeyEvent.VK_1){
                onePressed = false;
            }
            if (code == KeyEvent.VK_M){
                mPressed = false;
            }
        }
    }
    public boolean isUpPressed() {
        return upPressed;
    }
    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }
    public boolean iswPressed() {
        return wPressed;
    }
    public boolean issPressed() {
        return sPressed;
    }
    public boolean isaPressed() {
        return aPressed;
    }
    public boolean isdPressed() {
        return dPressed;
    }
    public boolean isOnePressed(){
        return onePressed;
    }
    public boolean ismPressed(){
        return mPressed;
    }
}
