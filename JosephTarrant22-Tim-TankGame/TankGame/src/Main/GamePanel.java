package Main;

import Entity.ArrowsPlayer;
import Entity.TargetManager;
import Entity.WASDPlayer;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{

    private final int tileSize = 16;
    private final int screenRows = 60;
    private final int screenColumns = 40;
    private final int windowWidth = tileSize * screenRows;
    private final int windowHeight = tileSize * screenColumns;
    private final int mapWidth = windowWidth;
    private final int mapHeight = tileSize * 30;
    private final int fps = 60;
    //The game states will dictate what need to be drawn to the screen at what time
    private final int startState = 0;
    private final int playState = 1;
    private final int pauseState = 2;
    private final int endOfGameState = 3;
    private final int timeTrialState = 4;
    private int lastGameState;
    private int gameState = startState;
    private UIManager UI = new UIManager(this);
    private KeyHandler keyH = new KeyHandler(this, UI);
    private Thread gameThread;
    private MapReader mapReader = new MapReader(this);
    private PowerUp powerUp;
    private WASDPlayer player1;
    private ArrowsPlayer player2;
    private Timer timer = new Timer(this);
    private CollisionChecker collisionChecker = new CollisionChecker(this, mapReader);
    private TargetManager tm;
    private String winner = "null";
    private int round = 1;
    private long roundPause = 3000;
    private long time;
    private boolean roundChanged = false;

    public GamePanel(){
        this.setPreferredSize(new Dimension(windowWidth, windowHeight));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);

        this.setFocusable(true);

        mapReader.fillMapArray();
        player1 = new WASDPlayer(this, keyH, mapReader);
        player2 = new ArrowsPlayer(this, keyH, mapReader);
        powerUp = new PowerUp(this, mapReader);

        tm = new TargetManager(this, player1, collisionChecker);
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    //This method will change the game state between play and pause
    public void gameStatePaused(){
        if (gameState == pauseState){
            gameState = lastGameState;
        }
        else{
            lastGameState = gameState;
            gameState = pauseState;
        }
    }

    //This resets the relevant game information so new games can be played
    public void reset(){
        timer.resetTimer();
        player1.resetPlayer();
        player2.resetPlayer();
        powerUp.reset();
        round = 1;
        roundChanged = false;
    }
    //The restart method resets everything needed for a new round to begin
    public void restart(){
        timer.resetTimer();
        powerUp.reset();
        player1.restartPlayer();
        player2.restartPlayer();
    }
    //This increments the round number and resets relevant variables
    public void nextRound(){
        //This if statement acts as a timer that delays the changing of rounds after a player has died
        long currentTime = System.currentTimeMillis();
        if (((currentTime - time) >= roundPause) && !roundChanged) {
            round++;
            restart();
            roundChanged = true;
            //This takes the player to the end of game menu when the round number exceeds 5
            if (round > 5) {
                calculateWinner();
                lastGameState = gameState;
                gameState = endOfGameState;
            }
        }
    }
    public void setTime(){
        time = System.currentTimeMillis();
    }
    //This calculates who has won based off of the player scores
    public void calculateWinner(){
        if (player1.getScore() < player2.getScore()){
            winner = "Red";
        }
        else if(player1.getScore() == player2.getScore()){
            winner = "Draw";
        }
        else{
            winner = "Green";
        }
    }

    //The update method updates all the game information like player location
    //The update method will do different things depending on what state the game is in
    public void update(){
        //This will update all the relevant information for the 2 player game mode
        if (gameState == playState) {

            player1.update();
            player2.update();
            powerUp.update();
            timer.update();

            if (timer.isEndOfGame()){
                roundChanged = false;
                nextRound();
            }
        }
        //This will update the relevant information for the time trial mode
        if (gameState == timeTrialState){
            player1.update();
            tm.update();
            timer.update();

            if (timer.isEndOfGame() || player1.getTargets() == 10){
                lastGameState = gameState;
                gameState = endOfGameState;
            }
        }
    }
    //This method is what draws everything to the screen
    //Like the update method this will draw different things depending on what state the game is in
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        //This draws the information for the 2 player game mode
        if (gameState == playState) {
            mapReader.drawMap(g2);
            player1.paintComponent(g2);
            player2.paintComponent(g2);
            powerUp.paint(g2);
            timer.paintComponent(g2);

            g2.drawString("Round: " + round, 450, 550);
        }

        //This draws the components of the time trial game mode
        if (gameState == timeTrialState){
            mapReader.drawMap(g2);
            tm.paint(g2);
            player1.paintComponent(g2);
            timer.paintComponent(g2);
        }
        //This draws the pause menu
        if (gameState == pauseState){
            UI.drawPauseMenu(g2);
        }
        //This draws the start menu
        if (gameState == startState){
            UI.drawStartMenu(g2);
        }
        //This draws the end of game menu
        if (gameState == endOfGameState){
            UI.drawEndMenu(g2);
        }
    }
    //This is the method that contains the game loop and enables the game to run
    @Override
    public void run() {

        //These variables make up a simple delay that limits the loop to only running 60 times a second
        double drawInterval = 1000000000/fps;
        long currentTime;
        long lastTime = System.nanoTime();
        double delta = 0;

        while (gameThread != null){
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1){
                //This is where the update and paint component methods are called so they run repeatedly
                update();
                repaint();
                delta--;
            }
        }
    }
    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public int getGameState() {
        return gameState;
    }
    public int getStartState() {
        return startState;
    }
    public int getPlayState() {
        return playState;
    }

    public int getPauseState() {
        return pauseState;
    }
    public int getTileSize(){
        return tileSize;
    }
    public int getMapWidth(){
        return mapWidth;
    }
    public int getMapHeight(){
        return mapHeight;
    }
    public void exitToMenu(){
        lastGameState = gameState;
        gameState = startState;
    }
    public void startGame(){
        reset();
        lastGameState = gameState;
        gameState = playState;
    }
    public MapReader getMapReader(){
        return mapReader;
    }
    public PowerUp getPowerUp(){
        return powerUp;
    }
    public String getWinner(){
        return winner;
    }
    public void setGameState(int num){
        lastGameState = gameState;
        gameState = num;
    }
    public int getEndOfGameState(){
        return endOfGameState;
    }

    public WASDPlayer getPlayer1() {
        return player1;
    }

    public ArrowsPlayer getPlayer2() {
        return player2;
    }
    public boolean isRoundChanged(){
        if (roundChanged) {
            roundChanged = false;
            return true;
        }
        return false;
    }
    //This contains all the information needed to start the time trial game mode
    public void startTimeTrial(){
        lastGameState = gameState;
        gameState = timeTrialState;
        player1.spawnPlayer();
        player1.setTargets(0);
        player1.setAmmo(15);
        tm.resetTargets();
        timer.resetTimer();
    }
    public int getTimeTrialState(){
        return timeTrialState;
    }
    public int getLastGameState(){
        return lastGameState;
    }
    public int getPassedTime(){
        return timer.getPassedTime();
    }
    public int getTargets(){
        return player1.getTargets();
    }
}
