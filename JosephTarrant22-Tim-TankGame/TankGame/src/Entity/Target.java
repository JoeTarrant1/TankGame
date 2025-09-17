package Entity;

import Main.GamePanel;

import java.awt.*;

public class Target {
    private int targetX;
    private int targetY;
    private int targetWidth;
    private int targetHeight;
    private Rectangle targetArea;
    private boolean bulletCollision = false;
    private GamePanel gp;

    public Target(int x, int y, GamePanel gamePanel){
        targetX = x;
        targetY = y;
        gp = gamePanel;
        targetWidth = gp.getTileSize();
        targetHeight = gp.getTileSize();
        targetArea = new Rectangle(targetX, targetY, targetWidth, targetHeight);
    }

    public boolean isBulletCollision(){
        return bulletCollision;
    }
    public void setBulletCollision(boolean val){
        bulletCollision = val;
    }
    public Rectangle getTargetArea(){
        return targetArea;
    }
}
