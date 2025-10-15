package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entity.Entity;
import main.GamePanel;

public class Building extends Entity {
	
	protected GamePanel gp;
	
	private transient Color blueprintColour;
	protected boolean blueprint = true;
	private transient BufferedImage displayImage;
	protected int[] resourceCount;
	public boolean lightSourceActive = true;
	
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
	protected transient BufferedImage destructionImage;
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
	
	public int cost = 0;
	private int arrayCounter = 0;
	public int roomNum;
	
	protected String name = "";
	protected String description = "No Description"; 
	
	public Rectangle2D.Float npcHitbox;

	public Building(GamePanel gp, float xPos, float yPos, float width, float height) {
		super(gp, xPos, yPos, width, height);
		this.gp = gp;
		destructionImage = importImage("/UI/DestructionImage.png");
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
	
	protected void getBlueprintImage() {
		blueprintColour = new Color(197, 198, 198, 150);
		int argb = blueprintColour.getRGB();
		displayImage = new BufferedImage((int)animations[0][0][0].getWidth(), (int)animations[0][0][0].getHeight(), 2);
		for(int i = 0; i < displayImage.getWidth(); i++) {
			for(int j = 0; j < displayImage.getHeight(); j++) {
				if(animations[0][0][0].getRGB(i, j) != 0) {
					int x = animations[0][0][0].getRGB(i, j);
					int y = mediateARGB(x, argb);
					displayImage.setRGB(i, j, y);
				}
			}
		}
	}
	
	private int mediateARGB(int c1, int c2){
	    int a1 = (c1 & 0xFF000000) >>> 24;
	    int r1 = (c1 & 0x00FF0000) >> 16;
	    int g1 = (c1 & 0x0000FF00) >> 8;
	    int b1 = (c1 & 0x000000FF) ;

	    int a2 = (c2 & 0xFF000000) >>> 24;
	    int r2 = (c2 & 0x00FF0000) >> 16;
	    int g2 = (c2 & 0x0000FF00) >> 8;
	    int b2 = (c2 & 0x000000FF) ;

	    int am = (a1 + a2) / 2;
	    int rm = (r1 + r2) / 2;
	    int gm = (g1 + g2) / 2;
	    int bm = (b1 + b2) / 2;

	    int m = (am << 24) + (rm << 16) + (gm << 8) + bm; 


	    return m;
	}
	
	public void setData(GamePanel gp, float xPos, float yPos, float width, float height) {
	    this.gp = gp;

        makeHitbox(xPos, yPos, width, height);
	}
	
	public Rectangle2D getHitbox() {
		return hitbox;
	}
	public int[] getResourceCount() {
		return resourceCount;
	}
	public void checkTilableTiles(Rectangle2D.Float h, boolean original) {}
	public int getPresetNum() {
		return presetNum;
	}
	
	public void update() {
		int xDiff = gp.camera.getXDiff();
		int yDiff = gp.camera.getYDiff();
		if(canBePlaced) {
			if(hitbox.contains(gp.mouseI.mouseX + xDiff, gp.mouseI.mouseY + yDiff)) {
				if(gp.keyI.shiftPressed) {
					openDestructionUI();
				}
			}
			
			if(!hitbox.contains(gp.mouseI.mouseX + xDiff, gp.mouseI.mouseY + yDiff)) {
				closeDestructionUI();
			}
			if(destructionUIOpen) {
				if(gp.mouseI.rightClickPressed) {
					gp.customiser.addToInventory(this);
					destroy();
					gp.buildingM.destroyBuilding(this);
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
	
	public void interact() {}

	public void drawOverlayUI(Graphics2D g2, int xDiff, int yDiff) {}
	
	public void draw(Graphics2D g2, int xDiff, int yDiff) {
		
	     g2.drawImage(animations[0][0][0], (int) (hitbox.x - xDrawOffset - xDiff), (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		 
		 if(destructionUIOpen) {
		     g2.drawImage(destructionImage, (int) (hitbox.x - xDrawOffset - xDiff), (int) (hitbox.y - yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		 }
	        
	}

}
