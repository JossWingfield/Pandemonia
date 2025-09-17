package entity;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class Entity implements Cloneable {
    //The parent class for everything that has a hitbox in game

    GamePanel gp;
    public Rectangle2D.Float hitbox; //The rectangle which is used to check collisions
    protected int movementSpeed;
    protected int direction = 0;
    
    //ANIMATIONS
    public BufferedImage[][][] animations; //The array which stores all animation images
    public int currentAnimation; //The index for the current animation
    protected int animationCounter = 0; //The index for the current frame of the animation
    protected int animationSpeed; //The counter which counts down to the next frame
    protected int animationSpeedFactor = 6;
    public int xDrawOffset, yDrawOffset, drawWidth, drawHeight;
    protected int drawScale = 3;
    
    public BufferedImage normalImage, litImage;

    public Entity(GamePanel gp, float xPos, float yPos, float width, float height) {
        this.gp = gp;
        makeHitbox(xPos, yPos, width, height);

        importAnimations();
    }
	public Entity() {
		importAnimations();
	}

    public void importAnimations() {} //This method is overridden in the child classes
    //Initialises the hitbox
    protected void makeHitbox(float xPos, float yPos, float width, float height) {
        hitbox = new Rectangle2D.Float(xPos, yPos, width, height);
    }
    //Draws the hitbox, so collisions can be seen
    public void drawHitbox(Graphics2D g) {
        //FOR COLLISION TESTING
        g.drawRect((int)(hitbox.x), (int)(hitbox.y), (int)hitbox.width, (int)hitbox.height);
    }
    public void drawHitbox(Graphics2D g, float xDiff, float yDiff) {
        //FOR COLLISION TESTING
        g.drawRect((int)(hitbox.x - xDiff), (int)(hitbox.y - yDiff), (int)hitbox.width, (int)hitbox.height);
    }
    protected BufferedImage importImage(String filePath) { //Imports and stores the image
        BufferedImage importedImage = null;
        try {
            importedImage = ImageIO.read(getClass().getResourceAsStream(filePath));
            //BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        } catch(IOException e) {
            e.printStackTrace();
        }
        return importedImage;
    }
    protected void importFromSpriteSheet(String filePath, int columnNumber, int rowNumber, int currentAnimation, int startX, int startY, int width, int height, int direction) {

        int arrayIndex = 0;

        BufferedImage img = importImage(filePath);

        for(int i = 0; i < columnNumber; i++) {
            for(int j = 0; j < rowNumber; j++) {
                animations[direction][currentAnimation][arrayIndex] = img.getSubimage(i*width + startX, j*height + startY, width, height);
                arrayIndex++;
            }
        }

    }
    
    protected void importFromSpriteSheetWithNormal(String filePath, int columnNumber, int rowNumber, int currentAnimation, int startX, int startY, int width, int height) {

	        int arrayIndex = 0;
	
	        BufferedImage img = importImage(filePath + ".png");
	        //BufferedImage normalImage = importImage(filePath + "Normal.png");
	
	        for(int i = 0; i < columnNumber; i++) {
	            for(int j = 0; j < rowNumber; j++) {
	                animations[0][currentAnimation][arrayIndex] = img.getSubimage(i*width + startX, j*height + startY, width, height);
	                //normalImages[0][currentAnimation][arrayIndex] = normalImage.getSubimage(i*width + startX, j*height + startY, width, height);
	                arrayIndex++;
	            }
	        }

    }

    private static BufferedImage createTransformed(BufferedImage image, AffineTransform at) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.transform(at);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }

    protected static BufferedImage createHorizontalFlipped(BufferedImage image) {
        AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(-1, 1));
        at.concatenate(AffineTransform.getTranslateInstance(-image.getWidth(), 0));
        return createTransformed(image, at);
    }
    protected static BufferedImage createVerticalFlipped(BufferedImage image) {
        AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(1, -1));
        at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));
        return createTransformed(image, at);
    }
    protected static BufferedImage rotateClockwise90(BufferedImage src) {
        int width = src.getWidth();
        int height = src.getHeight();

        BufferedImage dest = new BufferedImage(height, width, src.getType());

        Graphics2D graphics2D = dest.createGraphics();
        graphics2D.translate((height - width) / 2, (height - width) / 2);
        graphics2D.rotate(Math.PI / 2, height / 2, width / 2);
        graphics2D.drawRenderedImage(src, null);

        return dest;
    }
    
    public void resetAnimation(int animationNum) {
    	currentAnimation = animationNum;
    	animationCounter = 0;
    	animationSpeed = 0;
     	litImage = null;
    }
    
    public int getDirection() {
    	return direction;
    }
    public void update() {}
    public boolean isInSimulationChunks(int simulationDistance) {
    	if(gp == null) {
    		return false;
    	}
        int chunkSize = gp.mapM.chunkSize;
        int tileSize = gp.tileSize;

        // Convert entity's world position to chunk position
        int entityChunkX = (int) (hitbox.x / (chunkSize * tileSize));
        int entityChunkY = (int) (hitbox.y / (chunkSize * tileSize));

        // Get player's chunk position
        int playerChunkX = (int) ((gp.player.hitbox.x + gp.player.hitbox.width / 2) / (chunkSize * tileSize));
        int playerChunkY = (int) ((gp.player.hitbox.y + gp.player.hitbox.height / 2) / (chunkSize * tileSize));

        // Check if entity is within the simulation chunk range
        return Math.abs(entityChunkX - playerChunkX) <= simulationDistance &&
               Math.abs(entityChunkY - playerChunkY) <= simulationDistance;
    }
    public boolean isOnScreen(float xDiff, float yDiff, int width, int height) {

        int buffer = 500; // Expand the draw range by 100 pixels in each direction

        // Get entity's position relative to the screen
        float entityScreenX = hitbox.x - xDiff;
        float entityScreenY = hitbox.y - yDiff;

        // Check if any part of the entity (including buffer zone) is within the screen bounds
        return entityScreenX + hitbox.width > -buffer && entityScreenX < width + buffer &&
               entityScreenY + hitbox.height > -buffer && entityScreenY < height + buffer;
    }
    
    public void draw(Graphics2D g2) {}

}

