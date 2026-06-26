package entity.buildings;

import java.awt.geom.Rectangle2D;

import entity.items.Item;
import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.TextureRegion;
import utility.CollisionMethods;

public class KitchenCounter extends Building {
	
	public Item currentItem = null;
	public Rectangle2D.Float interactHitbox;
	private int interactSize = 32;
	
	public KitchenCounter(GamePanel gp, float xPos, float yPos, int preset) {
		super(gp, xPos, yPos, 48, 48);
		this.preset = preset;
		
		isSolid = true;
		
		drawWidth = 16*3;
		drawHeight = 16*3;
		importImages();
		isKitchenBuilding = true;
		mustBePlacedOnTable = true;
		tileGrid = true;
		buildHitbox = hitbox;
	}
	public void setBuildHitbox() {
		buildHitbox = hitbox;
	}
	public Building clone() {
		KitchenCounter building = new KitchenCounter(gp, hitbox.x, hitbox.y, preset);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new KitchenCounter(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ", " + preset + ");");
		System.out.println("arrayCounter++;");	
	}
	public void onPlaced() {
		updateConnections();
	    updateNeighbor(hitbox.x - gp.tileSize, hitbox.y);
	    updateNeighbor(hitbox.x + gp.tileSize, hitbox.y);
	    setBuildHitbox();
	}
	private void importImages() {
		animations = new TextureRegion[1][1][2];
		
        name = "Kitchen Counter";
        
    	hitbox.height = 11*3;
    	hitbox.width = 12*3;
    	xDrawOffset = 3*3;
    	yDrawOffset = 1*3;
		isBottomLayer = true;
		isKitchenBuilding = true;
		mustBePlacedOnFloor = true;
		interactHitbox = buildHitbox;
		
		xDrawOffset = 0*3;

		
	    // Assign sprites
	    //
	    // 0 = left end
	    // 1 = horizontal middle
	    // 2 = right end
	    // 3 = top connection
	    // 4 = bottom connection
	    // 5 = single
	    // 6 = top-left corner
	    // 7 = top-right corner
	    // 8 = bottom-left corner
	    // 9 = bottom-right corner
		switch(preset) {
		case 0:
	    	animations[0][0][0] = importImage("/decor/Counter.png").getSubimage(0, 16, 16, 16);
			break;
		case 1:
	    	animations[0][0][0] = importImage("/decor/Counter.png").getSubimage(16, 16, 16, 16);
			break;
		case 2:
	    	animations[0][0][0] = importImage("/decor/Counter.png").getSubimage(32, 16, 16, 16);
			break;
		case 3:
			animations[0][0][0] = importImage("/decor/Counter.png").getSubimage(16, 64, 16, 16);
			break;
		case 4:
	       	animations[0][0][0] = importImage("/decor/Counter.png").getSubimage(16, 32, 16, 16);
			break;
		case 5:
	    	animations[0][0][0] = importImage("/decor/Counter.png").getSubimage(96, 16, 16, 16);
			break;
		case 6:
	    	animations[0][0][0] = importImage("/decor/Counter.png").getSubimage(48, 16, 16, 16);
			break;
		case 7:
	    	animations[0][0][0] = importImage("/decor/Counter.png").getSubimage(64, 16, 16, 16);
			break;
		case 8:
	    	animations[0][0][0] = importImage("/decor/Counter.png").getSubimage(48, 32, 16, 16);
			break;
		case 9:
	    	animations[0][0][0] = importImage("/decor/Counter.png").getSubimage(64, 32, 16, 16);
			break;
		case 10:
	    	animations[0][0][0] = importImage("/decor/Counter.png").getSubimage(16, 48, 16, 16);
			break;
		}
		
	}
	public void updateConnections() {

	    boolean leftCounter = CollisionMethods.getBuildingAt(
	            gp,
	            (int)(hitbox.x - gp.tileSize),
	            (int)(hitbox.y),
	            "Kitchen Counter") instanceof KitchenCounter || CollisionMethods.getBuildingAt(
	    	            gp,
	    	            (int)(hitbox.x - gp.tileSize),
	    	            (int)(hitbox.y),
	    	            "Gate") instanceof Gate;


	    boolean rightCounter = CollisionMethods.getBuildingAt(
	            gp,
	            (int)(hitbox.x + gp.tileSize),
	            (int)(hitbox.y),
	            "Kitchen Counter") instanceof KitchenCounter || CollisionMethods.getBuildingAt(
	    	            gp,
	    	            (int)(hitbox.x + gp.tileSize),
	    	            (int)(hitbox.y),
	    	            "Gate") instanceof Gate;


	    boolean topCounter = CollisionMethods.getBuildingAt(
	            gp,
	            (int)(hitbox.x),
	            (int)(hitbox.y - gp.tileSize),
	            "Kitchen Counter") instanceof KitchenCounter || CollisionMethods.getBuildingAt(
	    	            gp,
	    	            (int)(hitbox.x),
	    	            (int)(hitbox.y - gp.tileSize),
	    	            "Gate") instanceof Gate;


	    boolean bottomCounter = CollisionMethods.getBuildingAt(
	            gp,
	            (int)(hitbox.x),
	            (int)(hitbox.y + gp.tileSize),
	            "Kitchen Counter") instanceof KitchenCounter || CollisionMethods.getBuildingAt(
	    	            gp,
	    	            (int)(hitbox.x),
	    	            (int)(hitbox.y + gp.tileSize),
	    	            "Gate") instanceof Gate;



	    // Preview placement connection check
	    int size = 16 * 3;

	    int xPos = ((int)(gp.mouseL.getScreenX()) / size) * size;
	    int yPos = ((int)(gp.mouseL.getScreenY()) / size) * size;


	    if(gp.world.customiser.selectedBuilding != null &&
	       (gp.world.customiser.selectedBuilding.getName().equals("Kitchen Counter") || gp.world.customiser.selectedBuilding.getName().equals("Gate"))) {


	        if ((int)(hitbox.x - gp.tileSize) == xPos 
	                && hitbox.y == yPos)
	            leftCounter = true;


	        if ((int)(hitbox.x + gp.tileSize) == xPos 
	                && hitbox.y == yPos)
	            rightCounter = true;


	        if ((int)(hitbox.x) == xPos 
	                && (hitbox.y - gp.tileSize) == yPos)
	            topCounter = true;


	        if ((int)(hitbox.x) == xPos 
	                && (hitbox.y + gp.tileSize) == yPos)
	            bottomCounter = true;
	    }



	    int connections = 0;

	    if(leftCounter) connections++;
	    if(rightCounter) connections++;
	    if(topCounter) connections++;
	    if(bottomCounter) connections++;


	    // Prevent T junctions / crosses
	    if(gp.world.customiser.selectedBuilding != null &&
	       (gp.world.customiser.selectedBuilding.getName().equals("Kitchen Counter") || gp.world.customiser.selectedBuilding.getName().equals("Gate"))) {

	        if(connections > 2) {

	            leftCounter = false;
	            rightCounter = false;
	            topCounter = false;
	            bottomCounter = false;

	        }
	    }


	    if(leftCounter && rightCounter) {

	        preset = 1; // horizontal middle

	    }
	    else if(topCounter && bottomCounter) {

	        preset = 10; // vertical middle

	    }


	    // Corners
	    else if(rightCounter && bottomCounter) {

	        preset = 6; // top-left corner

	    }
	    else if(leftCounter && bottomCounter) {

	        preset = 7; // top-right corner

	    }
	    else if(rightCounter && topCounter) {

	        preset = 8; // bottom-left corner

	    }
	    else if(leftCounter && topCounter) {

	        preset = 9; // bottom-right corner

	    }


	    // Single direction pieces
	    else if(leftCounter) {

	        preset = 2; // connects left

	    }
	    else if(rightCounter) {

	        preset = 0; // connects right

	    }
	    else if(topCounter) {

	        preset = 3; // connects up

	    }
	    else if(bottomCounter) {

	        preset = 4; // connects down

	    }
	    else {

	        preset = 5; // single
	    }


	    importImages();
	}
	public void destroy() {

	        BuildingManager bm = gp.world.buildingM;
	        
	        
	        Building[] buildings = bm.getBuildings();
	        
	        if(!gp.world.mapM.isInRoom(roomNum)) {
	        	buildings = gp.world.mapM.getRoom(roomNum).getBuildings();
	        }
	        
	        for (int i = 0; i < buildings.length; i++) {
	            Building b = buildings[i];

	            if (b == null || b == this) continue;

	            // If it was placed on a table
	            if (b.mustBePlacedOnTable || b.canBePlacedOnTable) {
	            	
	                // Optional: ensure it's actually on THIS table
	                if (this.hitbox.intersects(b.hitbox)) {

	                    // Return to customiser inventory
	                	if(b.canBePlaced) {
	                		gp.world.customiser.addToInventory(b);
	                	}
	                    
	                    // Destroy & remove it
	                    b.destroy();
	                    if(gp.world.mapM.isInRoom(roomNum)) {
		                    bm.getBuildings()[i] = null;
	                    } else {
	                    	gp.world.mapM.getRoom(roomNum).getBuildings()[i] = null;
	                    }
	                }
	            }
	        }
			preset = 5;
			importImages();
	}
	public void resetForCustomiser() {
		preset = 5;
		importImages();
	}
    private void updateNeighbor(float checkX, float checkY) {
        Building b = CollisionMethods.getBuildingAt(gp, (int)checkX, (int)checkY, "Kitchen Counter");
        if (b instanceof KitchenCounter) {
            ((KitchenCounter) b).updateConnections();
        }
        b = CollisionMethods.getBuildingAt(gp, (int)checkX, (int)checkY, "Gate");
	    if (b instanceof Gate) {
	    	((Gate) b).updateConnections();
	    }
    }
	public void draw(Renderer renderer) {
		if(currentItem != null) {
			int newSize = 16;
			currentItem.hitbox.x = hitbox.x - xDrawOffset;
			currentItem.hitbox.y = hitbox.y - newSize/2 - 6;
		}
		
	    renderer.draw(animations[0][0][0], (int)(hitbox.x - xDrawOffset ), (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
	    
	    //renderer.setColour(Colour.GREEN);
        //renderer.drawRect((int)interactHitbox.x, (int)interactHitbox.y, (int)interactHitbox.width, (int)interactHitbox.height);
		 
	}
}
