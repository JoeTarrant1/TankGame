package Entity;

import Main.GamePanel;
import Main.KeyHandler;
import Main.MapReader;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class WASDPlayer extends Player{

    public WASDPlayer(GamePanel gp, KeyHandler kh, MapReader mp){
        super(gp, kh, mp);
    }

    int counter = 30;
    public void update(){

        if(!bulletCollision) {
            //This checks for collision between the wall and player
            collisionChecker.checkCollision(this);
            //This checks for collision between the player and any powerups
            collisionChecker.checkCollision(this, gp.getPowerUp());

            executePowerups();

            //This will update the players location when they press the "w" key
            if (keyH.iswPressed()) {
                direction = "forward";
                if (!wallCollision) {
                    moveForward();
                }
            }
            //This will update the players location when the "s" key is pressed
            if (keyH.issPressed()) {
                direction = "backwards";
                if (!wallCollision) {
                    moveBackwards();
                }
            }
            //This will update the player angle when the "a" key is pressed
            if (keyH.isaPressed()) {
                turnLeft();
            }
            //This will update the player angle when the "d" key is pressed
            if (keyH.isdPressed()) {
                turnRight();
            }
            //This will spawn a bullet when the player presses the "1" key
            if (keyH.isOnePressed()) {

                //The counter helps delay the bullet so it can only spawn twice a second or without delay if the rapid fire powerup is active
                if (counter >= 30 || rapidFire) {
                    shoot();
                    counter = 0;
                }
            }
        }
        //This will remove any bullets that need to despawn
        for (int i = 0; i < bullets.size(); i++) {
            if (bullets.get(i).getDespawn()) {
                Bullet bullet = bullets.get(i);
                bullets.remove(bullet);
                totalBullets.remove(bullet);
            }
        }
        if (gp.getGameState() == gp.getPlayState()) {
            //This will increment the score of the other player if this player has been shot
            if (bulletCollision && !gp.isRoundChanged()) {
                //This moves the players collision area off of the map so that it cannot be interacted with  once the player is dead
                getCollisionArea().x = -1;
                getCollisionArea().y = -1;
                //The score counter is just used to ensure this code only happens once
                if (gp.getPlayer2().scoreCounter < 1) {
                    gp.getPlayer2().incrementScore();
                    gp.setTime();
                }
                gp.nextRound();
            }
        }
        if (gp.getGameState() == gp.getTimeTrialState()){
            if (bulletCollision){
                bulletCollision = false;
                bullets.clear();
                totalBullets.clear();
                gp.setGameState(gp.getEndOfGameState());
            }
        }
        //This calls each of the bullets update methods so they can move
        for (int x = 0; x < bullets.size(); x++){
            bullets.get(x).update();
        }
        //This acts as a simple delay that limits how quickly a player can fire
        counter++;
    }
    public void paintComponent(Graphics2D g2){

        g2.setColor(Color.GREEN);
        //If the player has collided with a bullet then they should not be drawn
        if (!bulletCollision) {
            Rectangle rect = new Rectangle(playerX, playerY, playerWidth, playerHeight);

            //The transformation code means that the players can move separately and not be affected by the others inputs
            //STACK OVERFLOW
            AffineTransform old = g2.getTransform();
            //STACK OVERFLOW

            g2.rotate(Math.toRadians(playerAngle), playerX + playerWidth / 2, playerY + playerHeight / 2);
            g2.draw(rect);
            g2.fill(rect);

            //STACK OVERFLOW
            g2.setTransform(old);
            //STACK OVERFLOW

            //g2.setColor(Color.BLUE);
            //g2.fill(getCollisionArea());

            //g2.fillRect(collisionChecker.getTopLeft().x, collisionChecker.getTopLeft().y, 5, 5);
            //g2.fillRect(collisionChecker.getTopRight().x, collisionChecker.getTopRight().y, 5, 5);
            //g2.fillRect(collisionChecker.getBottomLeft().x, collisionChecker.getBottomLeft().y, 5, 5);
            //g2.fillRect(collisionChecker.getBottomRight().x, collisionChecker.getBottomRight().y, 5, 5);
        }
        g2.drawString("Player 1 Ammo: " + ammo, gp.getTileSize()*5, gp.getTileSize()*32);

        //This draws the players score to the screen for the 2 player game mode
        if (gp.getGameState() == gp.getPlayState()) {
            g2.drawString("Player 1 Score: " + score, gp.getTileSize() * 5, gp.getTileSize() * 33);
        }
        //This draws the number of hit targets to the screen for the time trial game mode
        if (gp.getGameState() == gp.getTimeTrialState()){
            g2.drawString("Targets: " + targets + "/10", gp.getTileSize() * 5, gp.getTileSize() * 33);
        }
        //This calls each of the bullets paint methods so they can be drawn to the screen
        for (int y = 0; y < bullets.size(); y++){
            bullets.get(y).paintComponent(g2);
        }
    }

}
