package Main;

import Entity.Player;

import java.awt.*;

public class RapidFire extends PowerUp{

    private long duration = 5000;
    private long time;

    public RapidFire(GamePanel gp, MapReader mr, Point spawn){
        super(gp, mr);
        x = spawn.x;
        y = spawn.y;
        powerUpArea = new Rectangle(x, y, width, height);
    }

    @Override
    public void draw(Graphics2D g2){
        g2.setColor(Color.cyan);
        g2.fill(powerUpArea);
        g2.setColor(Color.BLACK);

        Font old = g2.getFont();

        g2.setFont(new Font("TimesRoman", Font.PLAIN, 7));
        g2.drawString("R", (int)powerUpArea.getCenterX(), (int)powerUpArea.getCenterY());

        g2.setFont(old);
    }

    @Override
    public void action(Player player){
        //This sets the rapid fire variable in teh player class to true
        player.setRapidFire(true);

        //This is a simple timer for 5 seconds that represents the duration for the powerup
        long currentTime = System.currentTimeMillis();
        if((currentTime - time) >= duration){
            //This reverts the rapid fire variable back to false
            player.setRapidFire(false);
            despawn = true;
        }
    }

    @Override
    public void setTime(long num){
        time = num;
    }
}
