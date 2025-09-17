package Entity;

import Main.CollisionChecker;
import Main.GamePanel;
import Main.KeyHandler;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Bullet {
    private int bulletX;
    private int bulletY;
    private int bulletAngle;
    private int bulletWidth;
    private int bulletHeight;
    private GamePanel gp;
    private KeyHandler keyH;
    private double cosMovement;
    private double sinMovement;
    private long spawnTime = System.currentTimeMillis();
    private long lifespan = 5000;
    private boolean wallCollision = false;
    private boolean playerCollision = false;
    private boolean despawn = false;
    private boolean disableWallCollisions = false;
    private Rectangle collisionArea;
    private CollisionChecker collisionChecker;
    private boolean bounced = false;
    public Bullet(int x, int y, int angle, GamePanel gamePanel, KeyHandler keyHandler){
        bulletX = x;
        bulletY = y;
        bulletWidth = 7;
        bulletHeight = 7;
        bulletAngle = angle;
        gp = gamePanel;
        keyH = keyHandler;
        collisionArea = new Rectangle(bulletX, bulletY, bulletWidth, bulletHeight);
        collisionChecker = new CollisionChecker(gp, gp.getMapReader());
    }

    //This will calculate the variables that determine which way a bullet will move
    public void setMovement(int angle){
        //I am multiplying by 5 to make the bullets go faster
        cosMovement = Math.abs(Math.cos(Math.toRadians(angle)) * 5);
        sinMovement = Math.abs(Math.sin(Math.toRadians(angle)) * 5);
    }
    //This will return true when a bullet should no longer be in the game
    public void despawn(){
        //This will trigger the bullet to despawn if it touches the edge of the screen
        if (bulletX <= 0 || bulletX >= gp.getMapWidth()-8 || bulletY <= 0 || bulletY >= gp.getMapHeight()-8){
            despawn = true;
        }
        //If the bullet is touching a player it will despawn
        if (playerCollision){
            despawn = true;
        }
        //This will trigger the bullet to despawn after 5 seconds
        long currentTime = System.currentTimeMillis();
        if ((currentTime - spawnTime) >= lifespan){
            despawn = true;
        }
    }
    //This will decide whether to add or subtract from the x and y variables to make the bullet move in the right direction
    public void update(){

        //This will not check for collisions if the disableWallCollisions variable is true, allowing the fire through walls powerup to function
        if (!disableWallCollisions){
            collisionChecker.checkCollision(this);
        }

        despawn();

        //The following will use the bullet angle to determine how much the x and y coordinates should increase by
        if (bulletAngle >= 0 && bulletAngle <= 90){
            setMovement(90 - bulletAngle);
            bulletX += cosMovement;
            checkVerticalCollision();
            bulletY -= sinMovement;
            checkHorizontalCollision();
        }
        else if (bulletAngle <= 180){
            setMovement(180 - bulletAngle);
            bulletX += sinMovement;
            checkVerticalCollision();
            bulletY += cosMovement;
            checkHorizontalCollision();
        }
        else if (bulletAngle <= 270){
            setMovement(270 - bulletAngle);
            bulletX -= cosMovement;
            checkVerticalCollision();
            bulletY += sinMovement;
            checkHorizontalCollision();
        }
        else if (bulletAngle <= 360){
            setMovement(360 - bulletAngle);
            bulletX -= sinMovement;
            checkVerticalCollision();
            bulletY -= cosMovement;
            checkHorizontalCollision();
        }
    }
    //This method is called after updating the y coordinate because if there is a wall collision after changing the y, it is a horizontal wall
    private void checkHorizontalCollision(){
        collisionArea.y = bulletY;
        
        //If wall collisions are disabled none of this code should run
        if(!disableWallCollisions) {
            collisionChecker.checkCollision(this);

            //If there is a wall collision and the bullet has not already bounced then the bullet angle can be changed
            //The bounced variable ensures the bullet angle will not be changed again after it has already bounced until it leaves the wall
            if (wallCollision && !bounced){
                //The calculations for changing angles are slightly different depending on bullet angle
                if (bulletAngle >= 0 && bulletAngle <= 180){
                    bulletAngle = 180 - bulletAngle;
                }
                else{
                    bulletAngle = 540 - bulletAngle;
                }
                //Once the bullet angle has been changed the bullet has bounced and the bounced variable can be set to true
                bounced = true;
            }

            //If the bullet is no longer colliding with the wall the bounced variable can be returned to false
            if (!wallCollision){
                bounced = false;
            }
        }
    }
    //This method is called after updating the x coordinate because if there is a wall collision after changing the x, it is a vertical wall
    private void checkVerticalCollision(){
        collisionArea.x = bulletX;

        //If wall collisions are disabled none of this code should run
        if(!disableWallCollisions) {
            collisionChecker.checkCollision(this);

            //If there is a wall collision and the bullet has not already bounced then the bullet angle can be changed
            //The bounced variable ensures the bullet angle will not be changed again after it has already bounced until it leaves the wall
            if (wallCollision && !bounced) {
                //This calculates the bullet angle after rebounding off the wall
                bulletAngle = 360 - bulletAngle;

                //Once the bullet angle has been changed the bullet has bounced and the bounced variable can be set to true
                bounced = true;
            }

            //If the bullet is no longer colliding with the wall the bounced variable can be returned to false
            if (!wallCollision){
                bounced = false;
            }
        }
    }
    //This will draw the bullet to the screen
    public void paintComponent(Graphics2D g2){
        //STACK OVERFLOW
        AffineTransform old = g2.getTransform();
        //STACK OVERFLOW

        g2.setColor(Color.BLACK);
        g2.rotate(bulletAngle, bulletX + 5, bulletY + 5);
        g2.fillRect(bulletX, bulletY, 7, 7);

        //STACK OVERFLOW
        g2.setTransform(old);
        //STACK OVERFLOW
    }
    public Rectangle getCollisionArea(){
        return collisionArea;
    }
    public int getBulletAngle(){
        return bulletAngle;
    }


    public void setWallCollision(boolean wallCollision) {
        this.wallCollision = wallCollision;
    }

    public void setPlayerCollision(boolean playerCollision) {
        this.playerCollision = playerCollision;
    }
    public boolean getDespawn(){
        return despawn;
    }
    public void setDisableWallCollisions(boolean val){
        disableWallCollisions = val;
    }
}
