package entity.buildings;

import java.awt.geom.Rectangle2D;

import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;
import utility.CollisionMethods;

public class Shelf extends Building {

	public int type;
	private boolean firstUpdate = true;
	private Rectangle2D.Float invisHitbox;
	
	private boolean onLeftWall, onRightWall;
	
	public Shelf(GamePanel gp, float xPos, float yPos, int type) {
		super(gp, xPos, yPos, 48, 48);
		this.type = type;
		
		isSolid = false;
		
		drawWidth = 16*3;
		drawHeight = 16*3;
		name = "Shelf";
		importImages();
		mustBePlacedOnWall = true;
		cost = 8;
        isDecor = true;
        isFourthLayer = true;
        tileGrid = true;
        description = "Can be placed on walls, other items can be placed on the shelf.";
        setBuildHitbox();
	}
	public Shelf(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		
		isSolid = false;
		
		drawWidth = 16*3;
		drawHeight = 16*3;
		name = "Shelf";
		mustBePlacedOnWall = true;
		cost = 8;
        isDecor = true;
        isFourthLayer = true;
        animations = new TextureRegion[1][1][1];
        animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(48, 16, 16, 16);
        tileGrid = true;
        description = "Can be placed on walls, other items can be placed on the shelf.";
        setBuildHitbox();
	}
	public void setBuildHitbox() {
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*2, hitbox.width-3*7, hitbox.height-3*7);
	}
    public void onPlaced() {
        updateConnections();
        updateNeighbor(hitbox.x - gp.tileSize, hitbox.y);
        updateNeighbor(hitbox.x + gp.tileSize, hitbox.y);
        setBuildHitbox();
    }
	public Building clone() {
		Shelf building = new Shelf(gp, hitbox.x, hitbox.y, type);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Shelf(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ", " + type + ");");
		System.out.println("arrayCounter++;");
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][1];
		
		switch(type) {
		 case 0: //LEFT EDGE
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(0, 0, 16, 16);
	         break;
		 case 1: //MIDDLE
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(16, 0, 16, 16);
	         break;
		 case 2: //RIGHT EDGE
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(32, 0, 16, 16);
	         break;
		 case 3: //LEFT CORNER
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(48, 0, 16, 16);
	         break;
		 case 4: //RIGHT CORNER
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(64, 0, 16, 16);
	         break;
		 case 5: //LEFT WALL TOP EDGE
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(0, 16, 16, 16);
	         break;
		 case 6: //LEFT WALL MIDDLE
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(0, 32, 16, 16);
	         break;
		 case 7: //LEFT WALL BOTTOM EDGE
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(0, 48, 16, 16);
	         break;
		 case 8: //RIGHT WALL TOP EDGE
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(32, 16, 16, 16);
	         break;
		 case 9: //RIGHT WALL MIDDLE
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(32, 32, 16, 16);
	         break;
		 case 10: //RIGHT WALL BOTTOM EDGE
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(32, 48, 16, 16);
	         break;
		 case 11: //SOLO BACK
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(48, 16, 16, 16);
	         break;
		 case 12: //SOLO LEFT
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(48, 32, 16, 16);
	         break;
		 case 13: //SOLO RIGHT
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(64, 32, 16, 16);
	         break;
		}
	}
    public void updateConnections() {
        boolean leftShelf = CollisionMethods.getBuildingAt(gp, (int)(hitbox.x - gp.tileSize), (int)(hitbox.y),"Shelf") instanceof Shelf;
        boolean rightShelf = CollisionMethods.getBuildingAt(gp, (int)(hitbox.x + gp.tileSize), (int)(hitbox.y),"Shelf") instanceof Shelf;
        boolean topShelf   = CollisionMethods.getBuildingAt(gp, (int)(hitbox.x), (int)(hitbox.y - gp.tileSize), "Shelf") instanceof Shelf;
        boolean bottomShelf= CollisionMethods.getBuildingAt(gp, (int)(hitbox.x), (int)(hitbox.y + gp.tileSize), "Shelf") instanceof Shelf;

        onLeftWall = false;
        onRightWall = false;
        

        int tileX = (int)(hitbox.x / gp.tileSize)-1;
        int tileYStart = (int)(hitbox.y / gp.tileSize);
        int tileYEnd = (int)((hitbox.y + hitbox.height) / gp.tileSize);

        for (int ty = tileYStart; ty < tileYEnd; ty++) {
            if (gp.mapM.currentRoom.mapGrid[1][tileX][ty] == 0) onRightWall = true;
            if (gp.mapM.currentRoom.mapGrid[1][tileX+2][ty] == 0) onLeftWall = true;
        }
                
    	int size = 16*3;
		int xPos = (int)((gp.mouseL.getWorldX())/size) * size;
		int yPos = (int)((gp.mouseL.getWorldY())/size) * size;
		if (gp.customiser.selectedBuilding != null && gp.customiser.selectedBuilding.getName().equals("Shelf")) {

		    // Horizontal / vertical neighbors
		    if ((int)(hitbox.x - gp.tileSize) == xPos && hitbox.y == yPos) leftShelf = true;
		    if ((int)(hitbox.x + gp.tileSize) == xPos && hitbox.y == yPos) rightShelf = true;
		    if ((int)(hitbox.x) == xPos && (hitbox.y - gp.tileSize) == yPos) topShelf = true;
		    if ((int)(hitbox.x) == xPos && (hitbox.y + gp.tileSize) == yPos) bottomShelf = true;
		    
		    boolean hasVerticalShelf = topShelf || bottomShelf;

	        // --- Corners only if NOT vertically stacked ---
	        if (!hasVerticalShelf) {
	            if (onLeftWall && (int)(hitbox.x + gp.tileSize) == xPos && hitbox.y == yPos)
	                rightShelf = true; // left wall corner
	            if (onRightWall && (int)(hitbox.x - gp.tileSize) == xPos && hitbox.y == yPos)
	                leftShelf = true;  // right wall corner
	        }
		    
		    // Corners
		    //if (onLeftWall && (int)(hitbox.x + gp.tileSize) == xPos && hitbox.y == yPos) rightShelf = true; // left wall corner
		    //if (onRightWall && (int)(hitbox.x - gp.tileSize) == xPos && hitbox.y == yPos) leftShelf = true;  // right wall corner
		}
        
        if (onLeftWall) {
            if (leftShelf) type = 4; // left wall cornera
            else if (topShelf && bottomShelf) type = 9; // vertical middle
            else if (topShelf) type = 10;               // bottom end
            else if (bottomShelf) type = 8;             // top end
            else type = 13;                             // single

        } else if (onRightWall) {
            if (rightShelf) type = 3; // right wall corner
            else if (topShelf && bottomShelf) type = 6; // vertical middle
            else if (topShelf) type = 7;                // bottom end
            else if (bottomShelf) type = 5;             // top end
            else type = 12;                             // single
        } else {
            // Horizontal / free-standing
            if (leftShelf && rightShelf) type = 1; // horizontal middle
            else if (leftShelf) type = 2;          // right end
            else if (rightShelf) type = 0;         // left end
            else type = 11;                         // single
        }
        importImages();
    }
    public void destroy() {
        animations = new TextureRegion[1][1][1];
        animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(48, 16, 16, 16);
        updateNeighbor(hitbox.x - gp.tileSize, hitbox.y);
        updateNeighbor(hitbox.x + gp.tileSize, hitbox.y);
    }
    private void updateNeighbor(float checkX, float checkY) {
        Building b = CollisionMethods.getBuildingAt(gp, (int)checkX, (int)checkY, "Shelf");
        if (b instanceof Shelf) {
            ((Shelf) b).updateConnections();
        }
    }
	public void draw(Renderer renderer) {
		if(firstUpdate) {
			firstUpdate = false;
			if(onRightWall) {
				invisHitbox = new Rectangle2D.Float(hitbox.x, hitbox.y, 80, hitbox.height);
			} else if(onLeftWall) {
				invisHitbox = new Rectangle2D.Float(hitbox.x-80, hitbox.y, 80, hitbox.height);
			}
		}
		
		if(invisHitbox == null) {
		     renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		} else {
			if(gp.player.hitbox.intersects(invisHitbox)) {
				TextureRegion img = animations[0][0][0];
				//img = CollisionMethods.reduceImageAlpha(img, 0.25f);
				renderer.draw(img, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
			} else {
			     renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
	
				}
			}
	     
		 if(destructionUIOpen) {
		     renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		 }
	        
	}

}
