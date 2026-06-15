package map;

import main.GamePanel;
import main.renderer.AssetPool;
import main.renderer.Texture;
import main.renderer.TextureRegion;

public class Tile {

    GamePanel gp;

    public TextureRegion image;
    public boolean solid = false;
    public boolean seasonal = false;
    public boolean isWall = false;
    public boolean isFloor = false;
    public boolean isBeam = false;

    public boolean animated = false;
    private float animationTimer = 0f;
    private float frameDuration = 0.1f; // seconds per frame
    public int currentFrame = 0;

    // Seasonal frames
    public TextureRegion[] springFrames, summerFrames, autumnFrames, winterFrames;

    // Current active frames
    public TextureRegion[] activeFrames;
    
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
    public Tile(GamePanel gp, String fileName, int x, int y, boolean isSeasonal, boolean isItchShowcase) { //Reads and stores the image of the tile
    	this.gp = gp;
    	this.seasonal = isSeasonal;
    	String spring = "/itch/tiles/spring/" + fileName + ".png";
    	String summer = "/itch/tiles/summer/" + fileName + ".png";
    	String autumn = "/itch/tiles/autumn/" + fileName + ".png";
    	String winter = "/itch/tiles/winter/" + fileName + ".png";
    	
        this.springImage = importImage(spring).getSubimage(x*16, y*16, 16, 16);
        this.summerImage = importImage(summer).getSubimage(x*16, y*16, 16, 16);
        this.autumnImage = importImage(autumn).getSubimage(x*16, y*16, 16, 16);
        this.winterImage = importImage(winter).getSubimage(x*16, y*16, 16, 16);
        this.image = springImage;

    }
    public Tile(GamePanel gp, String fileName, int row, int col, int frameCount, boolean isSeasonal, boolean isItch, int totalColumns) {
        this.gp = gp;
        this.seasonal = isSeasonal;
        this.animated = true;

        String base = isItch ? "/itch/tiles/" : "/tiles/";

        String spring = base + "spring/" + fileName + ".png";
        String summer = base + "summer/" + fileName + ".png";
        String autumn = base + "autumn/" + fileName + ".png";
        String winter = base + "winter/" + fileName + ".png";

        this.springFrames = loadFrames(spring, row, col, frameCount, totalColumns);
        this.summerFrames = loadFrames(summer, row, col, frameCount, totalColumns);
        this.autumnFrames = loadFrames(autumn, row, col, frameCount, totalColumns);
        this.winterFrames = loadFrames(winter, row, col, frameCount, totalColumns);

        this.activeFrames = springFrames;
        this.image = activeFrames[0];
    }
    private TextureRegion[] loadFrames(String path, int row, int col, int frameCount, int totalColumns) {
        Texture texture = importImage(path);
        TextureRegion[] frames = new TextureRegion[frameCount];

        int tileSize = 16;

        for (int f = 0; f < frameCount; f++) {
            int x = (col + f * totalColumns) * tileSize;
            int y = row * tileSize;

            frames[f] = texture.getSubimage(x, y, tileSize, tileSize);
        }

        return frames;
    }
    public void update(float dt) {
        if (!animated || activeFrames == null) return;

        animationTimer += dt;

        if (animationTimer >= frameDuration) {
            animationTimer = 0f;
            currentFrame = (currentFrame + 1) % activeFrames.length;
            image = activeFrames[currentFrame];
        }
    }
}
