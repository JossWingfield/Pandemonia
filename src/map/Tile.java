package map;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import main.GamePanel;

import static javax.imageio.ImageIO.read;

public class Tile {

	GamePanel gp;
	
    public BufferedImage image; //the image for the tile
    public BufferedImage normalImage;
    public boolean solid = false; //Determines if the tile is solid or not(the player can't pass through if it's solid)
    public boolean seasonal = false;
    public boolean isWall = false;
    public boolean isFloor = false;
    public boolean isBeam = false;
    
    public BufferedImage springImage, summerImage, autumnImage, winterImage;
    
    public Tile(GamePanel gp, String fileName, BufferedImage img, BufferedImage normalImg) { //Reads and stores the image of the tile
    	this.gp = gp;
    	
        try {
            if(img == null) {
                this.image = read(Objects.requireNonNull(getClass().getResourceAsStream(fileName)));
                this.normalImage = normalImg;
            } else {
                image = img;
                normalImage = normalImg;
            }

        }
        catch(IOException e) {
            e.printStackTrace();
        }

    }
    
    public Tile(GamePanel gp, String fileName, int x, int y, boolean isSeasonal) { //Reads and stores the image of the tile
    	this.gp = gp;
    	this.seasonal = isSeasonal;
    	String spring = "/tiles/spring/" + fileName + ".png";
    	String summer = "/tiles/summer/" + fileName + ".png";;
    	String autumn = "/tiles/autumn/" + fileName + ".png";;
    	String winter = "/tiles/winter/" + fileName + ".png";;
    	String normal = "/tiles/spring/" + fileName+"Normal" + ".png";;
    	
        try {
        	this.springImage = read(Objects.requireNonNull(getClass().getResourceAsStream(spring))).getSubimage(x*16, y*16, 16, 16);
            this.summerImage = read(Objects.requireNonNull(getClass().getResourceAsStream(summer))).getSubimage(x*16, y*16, 16, 16);
            this.autumnImage = read(Objects.requireNonNull(getClass().getResourceAsStream(autumn))).getSubimage(x*16, y*16, 16, 16);
            this.winterImage = read(Objects.requireNonNull(getClass().getResourceAsStream(winter))).getSubimage(x*16, y*16, 16, 16);
            this.normalImage = read(Objects.requireNonNull(getClass().getResourceAsStream(normal))).getSubimage(x*16, y*16, 16, 16);
            this.image = springImage;
        }
        catch(IOException e) {
            e.printStackTrace();
        }

    }
}
