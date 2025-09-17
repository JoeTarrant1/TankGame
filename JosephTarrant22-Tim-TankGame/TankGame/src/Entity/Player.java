package Entity;

import Main.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Player {

    Random random = new Random();
    //These are all variables that are common between both sub classes so they are protected so the sub classes can use them
    protected int playerWidth;
    protected int playerHeight;
    protected int playerX;
    protected int playerY;
    protected int playerAngle;
    protected int ammo = 15;
    protected int score = 0;
    protected int targets = 0;
    protected String direction = "forward";
    protected boolean wallCollision = false;
    protected boolean bulletCollision = false;
    protected ArrayList<Bullet> bullets = new ArrayList<>();
    protected static ArrayList<Bullet> totalBullets = new ArrayList<>();
    private Rectangle collisionArea;
    protected CollisionChecker collisionChecker;
    protected GamePanel gp;
    protected KeyHandler keyH;
    protected MapReader mp;
    protected int scoreCounter = 0;
    protected boolean rapidFire = false;
    protected boolean fireThroughWalls = false;
    protected ArrayList<PowerUp> activePowerups = new ArrayList<>();

    public Player(GamePanel gp, KeyHandler kh, MapReader mp) {
        this.gp = gp;
        playerWidth = 28;
        playerHeight = 44;
        keyH = kh;
        this.mp = mp;
        setLocation();
        collisionArea = new Rectangle(playerX + 5, playerY + 5, playerWidth - 10, playerHeight - 10);
        collisionChecker = new CollisionChecker(gp, gp.getMapReader());
    }

    public void setLocation(){
        Point point = mp.findEmptySpace();
        playerX = point.x;
        playerY = point.y;
        playerAngle = random.nextInt(360);
    }

    //This will calculate the cos component of the player's movement
    public double calculateCosMovement(int angle) {
        //I have multiplied by 2.5 to make the number bigger so the players can move faster
        double num = Math.abs(Math.cos(Math.toRadians(angle)) * 2.5);
        return num;
    }
    //This will calculate the sin component of the player's movement
    public double calculateSinMovement(int angle) {
        //I have multiplied by 2.5 to make the number bigger so the players can move faster
        double num = Math.abs(Math.sin(Math.toRadians(angle)) * 2.5);
        return num;
    }
    //The next two methods are responsible for making players move forwards and backwards
    //Players will move in different directions depending on which way they are facing, I have split the screen into 4 imaginary sectors
    //I am using sin and cos to calculate how much the player should move in the x and y direction depending on what angle they are facing
    public void moveForward(){
        if (playerAngle >= 0 && playerAngle <= 90){
            playerX += calculateCosMovement(90 - playerAngle);
            playerY -= calculateSinMovement(90 - playerAngle);
        }
        else if (playerAngle <= 180){
            playerX += calculateSinMovement(180 - playerAngle);
            playerY += calculateCosMovement(180 - playerAngle);
        }
        else if (playerAngle <= 270){
            playerX -= calculateCosMovement(270 - playerAngle);
            playerY += calculateSinMovement(270 - playerAngle);
        }
        else if (playerAngle <= 360){
            playerX -= calculateSinMovement(360 - playerAngle);
            playerY -= calculateCosMovement(360 - playerAngle);
        }
        collisionArea.x = playerX + 5;
        collisionArea.y = playerY + 5;

    }
    public void moveBackwards(){
        if (playerAngle >= 0 && playerAngle <= 90){
            playerX -= calculateCosMovement(90 - playerAngle);
            playerY += calculateSinMovement(90 - playerAngle);
        }
        else if (playerAngle <= 180){
            playerX -= calculateSinMovement(180 - playerAngle);
            playerY -= calculateCosMovement(180 - playerAngle);
        }
        else if (playerAngle <= 270){
            playerX += calculateCosMovement(270 - playerAngle);
            playerY -= calculateSinMovement(270 - playerAngle);
        }
        else if (playerAngle <= 360){
            playerX += calculateSinMovement(360 - playerAngle);
            playerY += calculateCosMovement(360 - playerAngle);
        }
        collisionArea.x = playerX + 5;
        collisionArea.y = playerY + 5;
    }
    //If the player has ammo left and has less than 3 bullets on the map then a new bullet will be added to the players bullet list
    public void shoot(){
        //If the rapid fire powerup is active the boolean variable will override the 3 bullet limit
        if ((bullets.size() < 3 || rapidFire) && ammo > 0) {

            collisionChecker.getPoints(new Rectangle(playerX, playerY, playerWidth, playerHeight), playerAngle);
            Point tl = collisionChecker.getTopLeft();
            //Point tr = collisionChecker.getTopRight();

            //Point midpoint = new Point((tl.x+tr.x)/2, (tl.y+tr.y)/2);

            Bullet bullet = new Bullet(tl.x, tl.y, playerAngle, gp, keyH);

            if (fireThroughWalls){
                bullet.setDisableWallCollisions(true);
            }
            else{
                bullet.setDisableWallCollisions(false);
            }

            bullets.add(bullet);
            totalBullets.add(bullet);

            //This will not decrement the ammo when the rapid fire powerup is active
            if(!rapidFire) {
                decrementAmmo();
            }
        }
    }
    //This subtracts from the player angle to make the player turn left
    public void turnLeft(){
        playerAngle -= 3;

        if (playerAngle < 0){
            playerAngle += 360;
        }
    }
    //This adds to the player angle to make the player turn right
    public void turnRight(){
        playerAngle += 3;

        if (playerAngle > 360){
            playerAngle -= 360;
        }
    }
    public void decrementAmmo(){
        ammo -= 1;
    }

    public void incrementScore(){
        score++;
        scoreCounter++;
    }



    //This resets all the relevant player information so that the game can be restarted
    public void resetPlayer(){
        restartPlayer();
        score = 0;
    }
    //This resets everything but the score, it will be used when starting new rounds
    public void restartPlayer(){
        setLocation();
        ammo = 15;
        collisionArea.x = playerX + 5;
        collisionArea.y = playerY + 5;
        bulletCollision = false;
        scoreCounter = 0;
        bullets.clear();
        totalBullets.clear();
        activePowerups.clear();
        fireThroughWalls = false;
        rapidFire = false;
    }

    //This method will be used to spawn players into the time trial game mode
    public void spawnPlayer(){
        playerX = gp.getTileSize()*10;
        playerY = gp.getTileSize()*2;
        collisionArea.x = playerX + 5;
        collisionArea.y = playerY + 5;
        playerAngle = 270;
    }

    public void executePowerups(){
        //This loops through the list of all the players active powerups
        for (int i = 0; i < activePowerups.size(); i++){
            //If the powerup's despawn variable is true it needs to be removed from the list
            if (activePowerups.get(i).isDespawn()){
                activePowerups.remove(i);
            }
            else{
                //Otherwise the powerups action method is called which gives each powerup their functionality
                activePowerups.get(i).action(this);
            }
        }
    }


    public String getDirection() {
        return direction;
    }
    public Rectangle getCollisionArea() {
        return collisionArea;
    }
    public int getPlayerAngle(){
        return playerAngle;
    }
    public void setWallCollision(boolean wallCollision) {
        this.wallCollision = wallCollision;
    }
    public void setBulletCollision(boolean bulletCollision) {
        this.bulletCollision = bulletCollision;
    }
    public ArrayList<Bullet> getTotalBullets(){
        return totalBullets;
    }
    public int getScore(){
        return score;
    }
    public void setAmmo(int num){
        ammo = num;
    }
    public void setFireThroughWalls(boolean val){
        fireThroughWalls = val;
    }
    public void setRapidFire(boolean val){
        rapidFire = val;
    }
    public void addPowerup(PowerUp powerup){
        activePowerups.add(powerup);
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public void setTargets(int num){
        targets = num;
    }
    public int getTargets(){
        return targets;
    }
    public void incrementTargets(){
        targets += 1;
//        if (targets == 10){
//            gp.setGameState(gp.getEndOfGameState());
//        }
    }
}
