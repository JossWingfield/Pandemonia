package entity.buildings;

import java.awt.geom.Rectangle2D;
import java.util.List;

import entity.PlayerMP;
import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.TextureRegion;
import utility.CollisionMethods;

public class Gate extends Building {
	
	private Rectangle2D.Float hitbox2;
	private boolean firstUpdate = true;
	private int type;
	
	public Gate(GamePanel gp, float xPos, float yPos, int type) {
		super(gp, xPos, yPos, 48, 48);
		this.type = type;
		
		isSolid = true;
		
		drawWidth = 32*3;
		drawHeight = 32*3;
    	hitbox.width = 16*3;
    	hitbox.height = 11*3;
		importImages();
		xDrawOffset = 2*3;
    	yDrawOffset = 17*3;
		isKitchenBuilding = true;
		mustBePlacedOnFloor = true;
		tileGrid = true;
		isSolid = false;
		
		
        List<Building> counters = gp.world.buildingM.findBuildingsWithName("Kitchen Counter");
        for(Building c: counters) {
        	if(c.hitbox.intersects(hitbox)) {
        		gp.world.buildingM.removeBuilding(c);
        	}
        }
	}
	public Building clone() {
		Gate building = new Gate(gp, hitbox.x, hitbox.y, type);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Gate(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ", " + type +  ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new TextureRegion[1][1][2];
		
		name = "Gate";
		switch(type) {
		case 0:
			animations[0][0][0] = importImage("/decor/Counter.png").getSubimage(64, 64, 32, 32);
			animations[0][0][1] = importImage("/decor/Counter.png").getSubimage(96, 64, 32, 32);
			break;
		case 1:
		   	hitbox.width = 11*3;
	    	hitbox.height = 11*3;
	    	drawWidth = 16*3;
			animations[0][0][0] = importImage("/decor/Counter.png").getSubimage(64, 96, 16, 32);
			animations[0][0][1] = importImage("/decor/Counter.png").getSubimage(96, 96, 16, 32);
			break;
		}
		
    
	}
	public void refreshImages() {
       	animations[0][0][0] = gp.world.mapM.getRooms()[roomNum].getCounterSkin().getTableImage(64, 64, 32, 32);
       	animations[0][0][1] = gp.world.mapM.getRooms()[roomNum].getCounterSkin().getTableImage(96, 64, 32, 32);
	}
	public void getNewImage() {
		updateConnections();
	}
	public void onPlaced() {
		updateConnections();
	    updateNeighbor(hitbox.x - gp.tileSize, hitbox.y);
	    updateNeighbor(hitbox.x + gp.tileSize, hitbox.y);
		buildHitbox = hitbox;
	}
	public void updateConnections() {

	    boolean leftCounter = CollisionMethods.getBuildingAt(
	            gp,
	            (int)(hitbox.x - gp.tileSize),
	            (int)hitbox.y,
	            "Kitchen Counter"
	    ) instanceof KitchenCounter;


	    boolean rightCounter = CollisionMethods.getBuildingAt(
	            gp,
	            (int)(hitbox.x + gp.tileSize),
	            (int)hitbox.y,
	            "Kitchen Counter"
	    ) instanceof KitchenCounter;


	    boolean topCounter = CollisionMethods.getBuildingAt(
	            gp,
	            (int)hitbox.x,
	            (int)(hitbox.y - gp.tileSize),
	            "Kitchen Counter"
	    ) instanceof KitchenCounter;


	    boolean bottomCounter = CollisionMethods.getBuildingAt(
	            gp,
	            (int)hitbox.x,
	            (int)(hitbox.y + gp.tileSize),
	            "Kitchen Counter"
	    ) instanceof KitchenCounter;



	    // Horizontal counter line
	    if(leftCounter || rightCounter) {
	        type = 0;
	    }

	    // Vertical counter line
	    else if(topCounter || bottomCounter) {
	        type = 1;
	    }

	    // Default fallback
	    else {
	        type = 0;
	    }


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
	public void destroy() {
		type = 0;
		importImages();
        gp.world.customiser.addToInventory(new KitchenCounter(gp, 0, 0, 0));
	}
	public void resetForCustomiser() {
		type = 0;
		importImages();
	}
	public void draw(Renderer renderer) {
		
		if(firstUpdate) {
			hitbox2 = new Rectangle2D.Float(hitbox.x, hitbox.y, 16*3, 38);
			firstUpdate = false;
		}

		int i = 0;
		if(gp.multiplayer) {
			for(PlayerMP player: gp.playerList) {
				if(player.currentRoomIndex == 0) {
					if(hitbox2.intersects(player.hitbox) || !gp.world.npcM.entityCheck(hitbox2)) {
						i = 1;
					}
				}
			}
		} else {
			if(hitbox2.intersects(gp.player.hitbox) || !gp.world.npcM.entityCheck(hitbox2)) {
				i = 1;
			}
		}

		renderer.draw(animations[0][0][i], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
	        
	}
}
