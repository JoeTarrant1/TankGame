package Main;

import Entity.Player;

import java.awt.*;

public class ExtraAmmo extends PowerUp{
    public ExtraAmmo(GamePanel gp, MapReader mr, Point spawn){
        super(gp, mr);
        x = spawn.x;
        y = spawn.y;
        powerUpArea = new Rectangle(x, y, width, height);
    }
    @Override
    public void draw(Graphics2D g2){
        g2.setColor(Color.ORANGE);
        g2.fill(powerUpArea);
        g2.setColor(Color.BLACK);

        Font old = g2.getFont();

        g2.setFont(new Font("TimesRoman", Font.PLAIN, 7));
        g2.drawString("E", (int)powerUpArea.getCenterX(), (int)powerUpArea.getCenterY());

        g2.setFont(old);
    }

    @Override
    public void action(Player player){
        //This will set the players ammo back to 15
        player.setAmmo(15);
        //The powerup is done so it can be despawned now
        despawn = true;
    }
}
