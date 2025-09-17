package Main;

import Entity.Player;

import java.awt.*;

public class FireThroughWalls extends PowerUp{

    private long duration = 5000;
    private long time;
    public FireThroughWalls(GamePanel gp, MapReader mr, Point spawn){
        super(gp, mr);
        x = spawn.x;
        y = spawn.y;
        powerUpArea = new Rectangle(x, y, width, height);
    }

    @Override
    public void draw(Graphics2D g2){
        g2.setColor(Color.RED);
        g2.fill(powerUpArea);
        g2.setColor(Color.BLACK);

        Font old = g2.getFont();

        g2.setFont(new Font("TimesRoman", Font.PLAIN, 7));
        g2.drawString("W", (int)powerUpArea.getCenterX(), (int)powerUpArea.getCenterY());

        g2.setFont(old);
    }

    @Override
    public void action(Player player){
        //This sets the fireThroughWalls variable in the player to true in order to indicate that this powerup is active
        player.setFireThroughWalls(true);

        //This forms a simple timer that will be true when the powerup has lasted its duration
        long currentTime = System.currentTimeMillis();
        if ((currentTime - time) >= duration){
            //Now the powerup is done the changes need to be reverted and it can be despawned
            player.setFireThroughWalls(false);
            despawn = true;
        }
    }
    @Override
    public void setTime(long num){
        time = num;
    }
}
