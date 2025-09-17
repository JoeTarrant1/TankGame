package Main;

import java.awt.*;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;

public class MapReader {

    private GamePanel gp;
    private String[][] mapArray = new String[60][30];

    public MapReader(GamePanel gamePanel){
        gp = gamePanel;
    }

    public void fillMapArray(){
        //This will try to find the specified file, if it cant it will throw an exception
        try (Scanner scanner = new Scanner(Paths.get("C:\\Users\\tarra\\OneDrive\\Documents\\TankGame\\TankGame\\res\\Maps\\Test Map.txt"))) {
            int row = 0;
            //This loop will read lines of the file until it reaches the last line
            while (scanner.hasNextLine()){
                int column = 0;
                //This will read the current line of the file into the line variable
                String line = scanner.nextLine();
                //This splits the line variable into an array of each of the numbers
                String[] nums = line.split("");

                //This for loop will iterate through the array and add the number to the array
                for (int i = 0; i < nums.length; i++){

                    mapArray[column][row] = nums[i];
                    column++;
                }
                row++;
            }
        }
        //This is the code that will run if the file cannot be found
        catch(Exception e){
            System.out.println("Error");
        }
    }

    public void drawMap(Graphics2D g2){
        g2.setColor(Color.DARK_GRAY);
        int tileSize = gp.getTileSize();
        //The outer loop represents the rows of the grid, it will iterate once the row is filled
        for (int y = 0; y < gp.getMapHeight(); y += tileSize){
            //The inner loop fills each row one column at a time
            for (int x = 0; x < gp.getMapWidth(); x += tileSize){
                //This checks if the current tile is a wall tile and draws a square if appropriate
                if (mapArray[x/tileSize][y/tileSize].equals("1")) {
                    g2.fillRect(x, y, tileSize, tileSize);
                }
            }
        }
    }
    public Point findEmptySpace(){
        String tile = "1";
        Random random = new Random();
        int column = 0;
        int row = 0;

        while (tile.equals("1")){
            column = random.nextInt(1,59);
            row = random.nextInt(1,29);
            tile = mapArray[column][row];
            if (tile == null){
                tile = "1";
            }
        }
        return new Point(column * gp.getTileSize(), row * gp.getTileSize());
    }
    public String getMapArray(int x, int y){
        return mapArray[x][y];
    }
}

