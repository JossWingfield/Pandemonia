package map;

import main.GamePanel;
import main.renderer.AssetPool;
import main.renderer.Texture;
import main.renderer.TextureRegion;

public class Tile {

	GamePanel gp;
	
    public TextureRegion image; //the image for the tile
    public boolean solid = false; //Determines if the tile is solid or not(the player can't pass through if it's solid)
    public boolean seasonal = false;
    public boolean isWall = false;
    public boolean isFloor = false;
    public boolean isBeam = false;
    
    public TextureRegion springImage, summerImage, autumnImage, winterImage;
    
    public Tile(GamePanel gp, String fileName, TextureRegion img) { //Reads and stores the image of the tile
    	this.gp = gp;
    
            if(img == null) {
                this.image = importImage(fileName).toTextureRegion();
            } else {
                image = img;
            }

    }
    public Tile(GamePanel gp, String fileName, Texture img) { //Reads and stores the image of the tile
    	this.gp = gp;
    
            if(img == null) {
                this.image = importImage(fileName).toTextureRegion();
            } else {
                image = img.toTextureRegion();
            }

    }
    public Texture importImage(String filePath) {
		Texture texture = AssetPool.getTexture(filePath);
	    return texture;
	}
    
    public Tile(GamePanel gp, String fileName, int x, int y, boolean isSeasonal) { //Reads and stores the image of the tile
    	this.gp = gp;
    	this.seasonal = isSeasonal;
    	String spring = "/tiles/spring/" + fileName + ".png";
    	String summer = "/tiles/summer/" + fileName + ".png";;
    	String autumn = "/tiles/autumn/" + fileName + ".png";;
    	String winter = "/tiles/winter/" + fileName + ".png";;
    	
        this.springImage = importImage(spring).getSubimage(x*16, y*16, 16, 16);
        this.summerImage = importImage(summer).getSubimage(x*16, y*16, 16, 16);
        this.autumnImage = importImage(autumn).getSubimage(x*16, y*16, 16, 16);
        this.winterImage = importImage(winter).getSubimage(x*16, y*16, 16, 16);
        this.image = springImage;

    }
}
