package entity;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import main.GamePanel;
import main.renderer.AssetPool;
import main.renderer.GLSLCamera;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;

public abstract class Entity implements Cloneable {
    //The parent class for everything that has a hitbox in game

	GamePanel gp;
    public Rectangle2D.Float hitbox; //The rectangle which is used to check collisions
    protected int movementSpeed;
    protected int direction = 0;
    
    //ANIMATIONS
    public TextureRegion[][][] animations; //The array which stores all animation images
    public int currentAnimation; //The index for the current animation
    protected int animationCounter = 0; //The index for the current frame of the animation
    protected double animationSpeed; //The counter which counts down to the next frame
    protected double animationSpeedFactor = 0.1;
    public int xDrawOffset, yDrawOffset, drawWidth, drawHeight;
    protected int drawScale = 3;

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
    public void drawHitbox(Renderer renderer) {
        //FOR COLLISION TESTING
        renderer.fillRect((int)(hitbox.x), (int)(hitbox.y), (int)hitbox.width, (int)hitbox.height);
    }
    public void drawHitbox(Graphics2D g, float xDiff, float yDiff) {
        //FOR COLLISION TESTING
        g.drawRect((int)(hitbox.x ), (int)(hitbox.y ), (int)hitbox.width, (int)hitbox.height);
    }
    public void resetAnimation(int animationNum) {
    	currentAnimation = animationNum;
    	animationCounter = 0;
    	animationSpeed = 0;
    }
    
    public int getDirection() {
    	return direction;
    }
    public void updateState(double dt) {}
    public void inputUpdate(double dt) {}
    /*
    public boolean isInSimulationChunks(int simulationDistance) {
    	if(gp == null) {
    		return false;
    	}
        int chunkSize = gp.world.mapM.chunkSize;
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
    */
    public boolean isOnScreen(GLSLCamera camera, float screenWidth, float screenHeight) {
        float camX = camera.position.x;
        float camY = camera.position.y;

        return hitbox.x + hitbox.width  >= camX &&
               hitbox.x             <= camX + screenWidth &&
               hitbox.y + hitbox.height >= camY &&
               hitbox.y             <= camY + screenHeight;
    }
    public Texture importImage(String filePath) {
		Texture texture = AssetPool.getTexture(filePath);
	    return texture;
	}
    public TextureRegion importTextureRegion(String filePath) {
        Texture texture = AssetPool.getTexture(filePath);
        // Full image UVs: u0,v0 = 0,0 and u1,v1 = 1,1
        return new TextureRegion(texture, 0f, 0f, 1f, 1f);
    }
    public TextureRegion createHorizontalFlipped(TextureRegion original) {
        // Swap U coordinates (flip horizontally)
        float u0 = original.u1;
        float u1 = original.u0;

        // Keep V coordinates the same
        float v0 = original.v0;
        float v1 = original.v1;

        return new TextureRegion(original.texture, u0, v0, u1, v1);
    }
    protected void importFromSpriteSheet(String filePath,int columnNumber,int rowNumber,int currentAnimation,int startX,int startY,int width, int height,int direction) {

        Texture sheetTexture = AssetPool.getTexture(filePath);
        int sheetWidth  = sheetTexture.getWidth();
        int sheetHeight = sheetTexture.getHeight();

        int arrayIndex = 0;

        for (int j = 0; j < rowNumber; j++) {          // rows (top → bottom in pixels)
            for (int i = 0; i < columnNumber; i++) {  // columns (left → right)

                int px = startX + i * width;
                int py = startY + j * height;

                // Pixel → UV (top-left origin)
                float u0 = px / (float) sheetWidth;
                float u1 = (px + width) / (float) sheetWidth;

                float vTop    = py / (float) sheetHeight;
                float vBottom = (py + height) / (float) sheetHeight;

                // Flip V ONCE for OpenGL bottom-left origin
                float v0 = 1f - vBottom;
                float v1 = 1f - vTop;

                animations[direction][currentAnimation][arrayIndex++] =
                        new TextureRegion(sheetTexture, u0, v0, u1, v1);
            }
        }
    }
    
    public void draw(Renderer renderer) {}

}

