package Main;

import Entity.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class PowerUp {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Rectangle powerUpArea;
    private GamePanel gp;
    private MapReader mr;
    private long time = System.currentTimeMillis();
    //This is currently a 10 second interval as 10000 milliseconds = 10 seconds
    private long interval = 20000;
    private ArrayList<PowerUp> powerups = new ArrayList<>();
    private int currentPowerUp;
    protected boolean despawn = false;

    public PowerUp(GamePanel gp, MapReader mr){
        this.gp = gp;
        this.mr = mr;

        width = gp.getTileSize() - 3;
        height = gp.getTileSize() - 3;
    }

    public void update(){
        //I have created a simple time delay that will execute the if statement every interval
        long currentTime = System.currentTimeMillis();
        if ((currentTime - time) >= interval){
            Random random = new Random();
            //A random number will be used to decide which powerup to spawn
            currentPowerUp = random.nextInt(3);
            //Before creating the powerup, an empty space is found
            Point spawn = mr.findEmptySpace();

            //This checks the random number and spawns the relevant powerup
            switch(currentPowerUp){
                case 0:
                    powerups.add(new ExtraAmmo(gp, mr, spawn));
                    break;
                case 1:
                    powerups.add(new RapidFire(gp, mr, spawn));
                    break;
                case 2:
                    powerups.add(new FireThroughWalls(gp, mr, spawn));
                    break;
            }

            //This resets the time delay
            time = System.currentTimeMillis();
        }
    }
    public void paint(Graphics2D g2){
        //This loops through the powerup list and prints out each rectangle
        for (int i = 0; i < powerups.size(); i++){
            powerups.get(i).draw(g2);
        }
    }
    public void reset(){
        powerups.clear();
    }
    public ArrayList<PowerUp> getPowerups(){
        return powerups;
    }
    //These are blank methods that will be overwritten in the child classes
    public void draw(Graphics2D g2){

    }
    public void action(Player player){

    }
    public void setTime(long num){

    }
    public Rectangle getPowerUpArea(){
        return powerUpArea;
    }
    public boolean isDespawn(){
        return despawn;
    }
}
