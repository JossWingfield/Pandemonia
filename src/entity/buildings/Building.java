package entity.buildings;

import java.awt.geom.Rectangle2D;

import org.lwjgl.glfw.GLFW;

import entity.Entity;
import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;

public class Building extends Entity {
	
	protected GamePanel gp;
	
	public boolean isTileable = false;
	public boolean isFloor = false;
	public boolean isSolid = true;
	public boolean isWall = false;
	public boolean isWireable = false;
	public boolean canBePlaced = true;
	
	public boolean isSecondLayer = false;
	public boolean isThirdLayer = false;
	public boolean isFourthLayer = false;
	public boolean isFifthLayer = false;
	public boolean isBottomLayer = false;
	protected int presetNum = -1;
	protected boolean destructionUIOpen = false;
	protected transient Texture destructionImage;
	public boolean isDecor = false;
	public boolean isKitchenBuilding = false;
	public boolean isStoreBuilding = false;
	public boolean isBathroomBuilding = false;
	public boolean mustBePlacedOnFloor = false;
	public boolean mustBePlacedOnWall = false;
	public boolean canBePlacedOnTable = false;
	public boolean canBePlacedOnShelf = false;
	public boolean mustBePlacedOnTable = false;
	public boolean canBuildingBePlacedOn = false;
	public boolean tileGrid = false;
	
	public int cost = 0;
	private int arrayCounter = 0;
	public int roomNum;
	public TextureRegion catalogueIcon;
	
	protected String name = "";
	protected String description = "No Description"; 
	
	public Rectangle2D.Float npcHitbox;
	public Rectangle2D.Float buildHitbox;

	public Building(GamePanel gp, float xPos, float yPos, float width, float height) {
		super(gp, xPos, yPos, width, height);
		this.gp = gp;
		destructionImage = importImage("/UI/DestructionImage.png");
		npcHitbox = hitbox;
		buildHitbox = hitbox;
	}
	
	public Building(GamePanel gp) {
		super(gp, 0, 0, 1, 1);
		destructionImage = importImage("/UI/DestructionImage.png");
	}
	
	public Building(GamePanel gp, Building b, float xPos, float yPos, float width, float height) {
		super(gp, xPos, yPos, width, height);
	}
	public String getDescription() {
		return description;
	}
	public void setCatalogueIcon(TextureRegion icon) {
		this.catalogueIcon = icon;
	}
	public void drawBuildHitbox(Renderer renderer) {
	    //FOR COLLISION TESTING
		renderer.fillRect((int)(buildHitbox.x ), (int)(buildHitbox.y ), (int)buildHitbox.width, (int)buildHitbox.height);
	}
	public Building clone() {
        try {
            return (Building) super.clone(); // Shallow copy
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Item clone not supported", e);
        }
    }
	
	public void destroy() {}
	
	public void setArrayCounter(int arrayCounter) {
		this.arrayCounter = arrayCounter;
	}
	public int getArrayCounter() {
		return arrayCounter;
	}
	
	public void setDimensions(float x, float y, float w, float h) {
        makeHitbox(x, y, w, h);
	 }
	
	public void setData(GamePanel gp, float xPos, float yPos, float width, float height) {
	    this.gp = gp;

        makeHitbox(xPos, yPos, width, height);
	}
	
	public Rectangle2D getHitbox() {
		return hitbox;
	}
	public void checkTilableTiles(Rectangle2D.Float h, boolean original) {}
	public int getPresetNum() {
		return presetNum;
	}
	
	public void update(double dt) {
		if(canBePlaced) {
			if(hitbox.contains(gp.mouseL.getWorldX(), gp.mouseL.getWorldY())) {
				if(gp.keyL.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
					openDestructionUI();
				}
			}
			
			if(!hitbox.contains(gp.mouseL.getWorldX(), gp.mouseL.getWorldY())) {
				closeDestructionUI();
			}
			if(destructionUIOpen) {
				if(gp.mouseL.mouseButtonDown(1)) {
					destroy();
					gp.customiser.addToInventory(this);
					gp.buildingM.destroyBuilding(this);
					gp.buildingM.checkBuildingConnections();
				}
			}
		}
	}
	public void printOutput() {
	}
	public void refreshImages() {
	}
	
	public void openDestructionUI() {
		destructionUIOpen = true;
	}
	protected void closeDestructionUI() {
		destructionUIOpen = false;
	}
	
	public String getName() {
		return name;
	}
	
	public void onPlaced() {
	}
	public void interact() {}

	public void drawOverlayUI(Renderer renderer) {}
	
	public void draw(Renderer renderer) {
		
		renderer.draw(animations[0][0][0], (int) (hitbox.x - xDrawOffset), (int) (hitbox.y)-yDrawOffset, drawWidth, drawHeight);
		 
		 if(destructionUIOpen) {
			renderer.draw(destructionImage, (int) (hitbox.x - xDrawOffset), (int) (hitbox.y)-yDrawOffset, 48, 48);
		 }
	        
	}
	public void drawEmissive(Renderer renderer) {
	}
	public void drawGodRay(Renderer renderer) {
	}
}
