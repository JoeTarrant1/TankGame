package Entity;

import Main.GamePanel;
import Main.KeyHandler;
import Main.MapReader;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class ArrowsPlayer extends Player{

    public ArrowsPlayer(GamePanel gp, KeyHandler kh, MapReader mp){
        super(gp, kh, mp);
    }

    int counter = 30;
    public void update(){

        if (!bulletCollision) {
            collisionChecker.checkCollision(this);
            collisionChecker.checkCollision(this, gp.getPowerUp());
            executePowerups();

            //This updates the players location when the up arrow is pressed
            if (keyH.isUpPressed()) {
                direction = "forward";
                if (!wallCollision) {
                    moveForward();
                }
            }
            //This updates the players location when the down arrow is pressed
            if (keyH.isDownPressed()) {
                direction = "backwards";
                if (!wallCollision) {
                    moveBackwards();
                }
            }
            //This updates the player angle when the left arrow is pressed
            if (keyH.isLeftPressed()) {
                turnLeft();
            }
            //This updates the player angle when the right arrow is pressed
            if (keyH.isRightPressed()) {
                turnRight();
            }
            //This spawns a bullet when the "m" key is pressed
            if (keyH.ismPressed()) {
                //The delay will only allow a bullet to be fired every half second or without delay if the rapid fire powerup is active
                if (counter >= 30 || rapidFire) {
                    shoot();
                    counter = 0;
                }
            }
        }
        //This will remove any bullets that need to be despawned
        for (int i = 0; i < bullets.size(); i++) {
            if (bullets.get(i).getDespawn()) {
                Bullet bullet = bullets.get(i);
                bullets.remove(bullet);
                totalBullets.remove(bullet);
            }
        }
        //This will increment the score of the other player if this player has been shot
        if (bulletCollision && !gp.isRoundChanged()){
            getCollisionArea().x = -1;
            getCollisionArea().y = -1;
            if(gp.getPlayer1().scoreCounter < 1){
                gp.getPlayer1().incrementScore();
                gp.setTime();
            }
            gp.nextRound();
        }
        //This will call the update method for each bullet so that they can move
        for (int x = 0; x < bullets.size(); x++) {
            bullets.get(x).update();
        }
        counter++;
    }
    public void paintComponent(Graphics2D g2){

        g2.setColor(Color.RED);
        if (!bulletCollision) {
            Rectangle rect = new Rectangle(playerX, playerY, playerWidth, playerHeight);

            //STACK OVERFLOW
            AffineTransform old = g2.getTransform();
            //STACK OVERFLOW

            //This will draw the rectangle which represents the player
            g2.rotate(Math.toRadians(playerAngle), playerX + playerWidth / 2, playerY + playerHeight / 2);
            g2.draw(rect);
            g2.fill(rect);

            //g2.setColor(Color.BLUE);
            //g2.fill(getCollisionArea());

            //STACK OVERFLOW
            g2.setTransform(old);
            //STACK OVERFLOW
        }
        g2.drawString("Player 2 Ammo: " + ammo, gp.getTileSize()*36, gp.getTileSize()*32);
        g2.drawString("Player 2 Score: " + score, gp.getTileSize()*36, gp.getTileSize()*33);

        //This will call the paint methods of each bullet so that they can be displayed to the screen
        for (int y = 0; y < bullets.size(); y++){
            bullets.get(y).paintComponent(g2);
        }
    }
}
