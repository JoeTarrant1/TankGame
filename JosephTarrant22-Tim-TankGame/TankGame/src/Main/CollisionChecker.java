package Main;

import Entity.Bullet;
import Entity.Player;
import Entity.TargetManager;

import java.awt.*;

public class CollisionChecker {
    GamePanel gp;
    MapReader mp;
    Point topLeft = new Point();
    Point topRight = new Point();
    Point bottomLeft = new Point();
    Point bottomRight = new Point();

    public CollisionChecker(GamePanel gamePanel, MapReader mapReader){
        gp = gamePanel;
        mp = mapReader;
    }

    public void checkCollision(Player player){

        int angle = player.getPlayerAngle();
        getPoints(player.getCollisionArea(), angle);

        //These variables correspond to the players position on the imaginary grid that makes up the map
        int leftCol;
        int rightCol;
        int topRow;
        int bottomRow;

        //The players row and colum value will be different depending on their orientation
        //This if statement will decide whether the player is facing up, down, left, or right
        if (angle >= 315 || angle < 45){
            leftCol = topLeft.x / gp.getTileSize();
            rightCol = topRight.x / gp.getTileSize();
            topRow = topLeft.y / gp.getTileSize();
            bottomRow = bottomLeft.y / gp.getTileSize();
        }
        else if (angle < 135){
            leftCol = bottomLeft.x / gp.getTileSize();
            rightCol = topLeft.x / gp.getTileSize();
            topRow = bottomLeft.y / gp.getTileSize();
            bottomRow = bottomRight.y / gp.getTileSize();
        }
        else if (angle < 225){
            leftCol = topRight.x / gp.getTileSize();
            rightCol = topLeft.x / gp.getTileSize();
            topRow = bottomLeft.y / gp.getTileSize();
            bottomRow = topLeft.y / gp.getTileSize();
        }
        else{
            leftCol = topLeft.x / gp.getTileSize();
            rightCol = bottomLeft.x / gp.getTileSize();
            topRow = topRight.y / gp.getTileSize();
            bottomRow = topLeft.y / gp.getTileSize();
        }

        //These represent the two possible tiles that the player could be colliding with
        String tile1;
        String tile2;

        //The tiles will be different depending on whether the player is moving forwards or backwards
        if (player.getDirection().equals("forward")){
            //This statement checks what direction the player is facing and calculates the corresponding tiles that need to be checked
            if (angle >= 315 || angle < 45){
                tile1 = mp.getMapArray(leftCol, topRow);
                tile2 = mp.getMapArray(rightCol, topRow);
            }
            else if (angle < 135){
                tile1 = mp.getMapArray(rightCol, topRow);
                tile2 = mp.getMapArray(rightCol, bottomRow);
            }
            else if (angle < 225){
                tile1 = mp.getMapArray(leftCol, bottomRow);
                tile2 = mp.getMapArray(rightCol, bottomRow);
            }
            else{
                tile1 = mp.getMapArray(leftCol, bottomRow);
                tile2 = mp.getMapArray(leftCol, topRow);
            }
        }
        else{
            if (angle >= 315 || angle < 45){
                tile1 = mp.getMapArray(leftCol,  bottomRow);
                tile2 = mp.getMapArray(rightCol, bottomRow);
            }
            else if (angle < 135){
                tile1 = mp.getMapArray(leftCol, topRow);
                tile2 = mp.getMapArray(leftCol, bottomRow);
            }
            else if (angle < 225){
                tile1 = mp.getMapArray(leftCol, topRow);
                tile2 = mp.getMapArray(rightCol, topRow);
            }
            else{
                tile1 = mp.getMapArray(rightCol, bottomRow);
                tile2 = mp.getMapArray(rightCol, topRow);
            }
        }

        //This checks if either one of the tiles is a wall tile and changes the players collision variable to reflect this
        if (tile1 != null && tile2 != null) {
            if ((tile1.equals("1")) || tile2.equals("1")) {
                player.setWallCollision(true);
            } else {
                player.setWallCollision(false);
            }
        }
        //This checks for collisions between bullets and players
        for (int i = 0; i < player.getTotalBullets().size(); i++){
            int x = (int)player.getTotalBullets().get(i).getCollisionArea().getCenterX();
            int y = (int)player.getTotalBullets().get(i).getCollisionArea().getCenterY();
            if (player.getCollisionArea().contains(new Point(x, y))){
                player.setBulletCollision(true);
                player.getTotalBullets().get(i).setPlayerCollision(true);
            }
        }
    }
    //This method checks for collisions between the player and any powerups
    public void checkCollision(Player player, PowerUp powerUp){
        //This loops through the list of powerups
        for (int i = 0; i < powerUp.getPowerups().size(); i++){
            //This finds the centre coordinates of the powerup
            int centreX = (int) powerUp.getPowerups().get(i).getPowerUpArea().getCenterX();
            int centreY = (int) powerUp.getPowerups().get(i).getPowerUpArea().getCenterY();
            //This checks if the player is colliding with the powerup
            if (player.getCollisionArea().contains(new Point(centreX, centreY))){
                //This adds the powerup that the player is colliding with to the players list of active powerups
                player.addPowerup(powerUp.getPowerups().get(i));
                //This starts the timer for the powerup if it has one
                powerUp.getPowerups().get(i).setTime(System.currentTimeMillis());
                //This removes it from the list of uncollected powerups so that it will not be drawn or collected again
                powerUp.getPowerups().remove(i);
            }
        }
    }
    //This checks for collisions between bullets and walls
    public void checkCollision(Bullet bullet){

        String tile1;

        //This calculates which row and column the bullet's centre coordinates are in
        int x = (int)bullet.getCollisionArea().getCenterX()/gp.getTileSize();
        int y = (int)bullet.getCollisionArea().getCenterY()/gp.getTileSize();

        //This finds the tile at the bullets location and stores the value it holds in tile1
        tile1 = mp.getMapArray(x,y);
        //This checks if the tile is a wall tile and changes the bullets collision variable to reflect this
        if (tile1 != null) {
            if ((tile1.equals("1"))) {
                bullet.setWallCollision(true);
            } else {
                bullet.setWallCollision(false);
            }
        }
    }

    //This checks for collisions between targets and bullets
    public void checkCollision(TargetManager tm, Player player){
        //This loop checks each target to see if any of the bullets are colliding with it
        for (int i = 0; i < tm.getTargets().size(); i++){
            for (int j = 0; j < player.getBullets().size(); j++){
                //This finds the centre coordinates of the bullet
                int x = (int)player.getBullets().get(j).getCollisionArea().getCenterX();
                int y = (int)player.getBullets().get(j).getCollisionArea().getCenterY();
                //This checks if the target collides with the bullet
                if (!tm.getTargets().get(i).isBulletCollision() && tm.getTargets().get(i).getTargetArea().contains(new Point(x, y))){
                    tm.getTargets().get(i).setBulletCollision(true);
                    player.getBullets().get(j).setPlayerCollision(true);
                    player.incrementTargets();
                }
            }
        }
    }


    //I used STACK OVERFLOW to help with the maths in this method
    public void getPoints(Rectangle rect, int angle){
        int width = rect.width;
        int height = rect.height;
        double centreX = rect.getCenterX();
        double centreY = rect.getCenterY();

        //These variables make up components of the later calculations
        double widthCos = (width/2) * Math.cos(Math.toRadians(angle));
        double widthSin = (width/2) * Math.sin(Math.toRadians(angle));
        double heightCos = (height/2) * Math.cos(Math.toRadians(angle));
        double heightSin = (height/2) * Math.sin(Math.toRadians(angle));


        //These calculations will find all four corners of the given rectangle using the centre coordinates, the rectangle dimensions, and the angle it is facing
        //I used STACK OVERFLOW to help with these calculations
        topLeft.x = (int) (centreX - (widthCos - heightSin));
        topLeft.y = (int) (centreY - (widthSin + heightCos));

        topRight.x = (int) (centreX + (widthCos - heightSin));
        topRight.y = (int) (centreY + (widthSin + heightCos));

        bottomLeft.x = (int) (centreX - (widthCos + heightSin));
        bottomLeft.y = (int) (centreY - (widthSin - heightCos));

        bottomRight.x = (int) (centreX + (widthCos + heightSin));
        bottomRight.y = (int) (centreY + (widthSin - heightCos));
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getTopRight() {
        return topRight;
    }

    public Point getBottomLeft() {
        return bottomLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }
}
