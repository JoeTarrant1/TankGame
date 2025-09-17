package Entity;

import Main.CollisionChecker;
import Main.GamePanel;

import java.awt.*;
import java.util.ArrayList;

public class TargetManager {
    private ArrayList<Target> targets = new ArrayList<>();
    private GamePanel gp;
    private Player player;
    private CollisionChecker collisionChecker;
    public TargetManager(GamePanel gamePanel, Player player, CollisionChecker collisionChecker){
        gp = gamePanel;
        this.player = player;
        this.collisionChecker = collisionChecker;
        setTargets();
    }

    //This sets the fixed locations of 10 separate targets
    public void setTargets(){
        targets.add(new Target(gp.getTileSize()*3, gp.getTileSize()*8, gp));
        targets.add(new Target(gp.getTileSize()*9, gp.getTileSize()*18, gp));
        targets.add(new Target(gp.getTileSize()*41, gp.getTileSize()*17, gp));
        targets.add(new Target(gp.getTileSize()*55, gp.getTileSize()*26, gp));
        targets.add(new Target(gp.getTileSize()*53, gp.getTileSize()*8, gp));
        targets.add(new Target(gp.getTileSize()*20, gp.getTileSize()*3, gp));
        targets.add(new Target(gp.getTileSize()*47, gp.getTileSize()*8, gp));
        targets.add(new Target(gp.getTileSize()*23, gp.getTileSize()*27, gp));
        targets.add(new Target(gp.getTileSize()*33, gp.getTileSize()*28, gp));
        targets.add(new Target(gp.getTileSize()*11, gp.getTileSize()*9, gp));
    }

    //This method goes through each target and sets its collision variable back to false
    public void resetTargets(){
        for (int i = 0; i < targets.size(); i++){
            targets.get(i).setBulletCollision(false);
        }
    }

    public void update(){
        collisionChecker.checkCollision(this, player);
    }

    public void paint(Graphics2D g2){
        g2.setColor(Color.red);
        //This will go through each target and draw it if its collision variable is false
        for (int i = 0; i < targets.size(); i++){
            if (!targets.get(i).isBulletCollision()){
                g2.fill(targets.get(i).getTargetArea());
            }
        }
    }

    public ArrayList<Target> getTargets() {
        return targets;
    }
}
