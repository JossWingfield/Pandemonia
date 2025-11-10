package utility;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entity.buildings.Building;
import main.GamePanel;

public class CollisionMethods {
	
	GamePanel gp;

    public CollisionMethods(GamePanel gp) {
        this.gp = gp;
    }

    //Checks entity on tile collisions
    public static boolean tileCheck(float x, float y, float width, float height, GamePanel gp) { //True if the player can move
        //CHECKS IF THE ENTITY CAN MOVE THROUGH EACH TILE

        boolean solid = false;
        //Checks each corner of the entity, to determine collisions

        //CHECKS TOP LEFT
        if (!isTileSolid(x, y, gp)) {
            //CHECKS BOTTOM RIGHT
            if (!isTileSolid(x + width - 1, y + height - 1, gp)) {
                //CHECKS BOTTOM LEFT
                if (!isTileSolid(x, y + height - 1, gp)) {
                    //CHECKS TOP RIGHT;
                    if (!isTileSolid(x + width - 1, y, gp)) {
                        //TOP MIDDLE
                        if (!isTileSolid(x+(width/2), y, gp)) {
                            //Bottom middle
                            if (!isTileSolid(x+(width/2), y+height-1, gp)) {
                                //Left middle
                                if (!isTileSolid(x, y+(height/2), gp)) {
                                    //Right middle
                                    if (!isTileSolid(x+width-1, y+(height/2), gp)) {
                                        //PLAYER CAN MOVE
                                        solid = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return solid;
    }
    
    public static boolean canFallThroughGround(float x, float y, float width, float height, float airSpeed, boolean canPassThroughPlatform, GamePanel gp, boolean isPlayer) {

        boolean solid = false;
        //Checks each corner of the entity, to determine collisions

        //CHECKS TOP LEFT
        if (!isTileSolid(x, y, gp)) {
            //CHECKS BOTTOM RIGHT
            if (!isTileSolid(x + width - 1, y + height - 1, gp)) {
                //CHECKS BOTTOM LEFT
                if (!isTileSolid(x, y + height - 1, gp)) {
                    //CHECKS TOP RIGHT;
                    if (!isTileSolid(x + width - 1, y, gp)) {
                        //TOP MIDDLE
                        if (!isTileSolid(x+(width/2), y, gp)) {
                            //Bottom middle
                            if (!isTileSolid(x+(width/2), y+height-1, gp)) {
                                //Left middle
                                if (!isTileSolid(x, y+(height/2), gp)) {
                                    //Right middle
                                    if (!isTileSolid(x+width-1, y+(height/2), gp)) {
                                        //PLAYER CAN MOVE
                                        solid = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return solid;
    }
    
    public static boolean groundCheck(float x, float y, float width, float height, GamePanel gp) {
        //CHECKS IF THE ENTITY CAN MOVE THROUGH EACH TILE

        boolean solid = false;
        boolean platform = false;
        //Checks each corner of the entity, to determine collisions

        if(isTileSolid(x, y+height, gp) || isTileSolid(x+(width-1), y+height, gp)) {
        	solid = true;
        }

        return solid || platform;
    }

    //Checks if tile at position is solid
    public static boolean isTileSolid(float x, float y, GamePanel gp) {
        //CHECKS IF CORRESPONDING TILE IN 2D ARRAY IS SOLID
    	if(x > (gp.mapM.currentRoom.mapWidth-1)*gp.tileSize) {
    		return true;
    	}
        int value = gp.mapM.currentRoom.mapGrid[1][(int)(x/gp.tileSize)][(int)(y/gp.tileSize)];
        return gp.mapM.tiles[value].solid;
    }
    public static Building getBuildingAt(GamePanel gp, int worldX, int worldY, String name) {
        for (Building b : gp.buildingM.getBuildings()) {
        	if(b != null && b.getName().equals(name)) {
        		if ((int)b.hitbox.x == worldX && (int)b.hitbox.y == worldY)
        			return b;
        	}
        }
        return null;
    }
    public static boolean canLightPassThroughTile(int tx, int ty, GamePanel gp) {
        // bounds: if outside world, treat as solid (or treat as passable depending on your game)
        if (tx < 0 || ty < 0 || tx >= gp.mapM.currentMapWidth || ty >= gp.mapM.currentMapHeight) {
            return false; // outside = blocked; set to true if you prefer outside to be open
        }

        int value = gp.mapM.currentRoom.mapGrid[3][tx][ty];
        int value2 = gp.mapM.currentRoom.mapGrid[0][tx][ty];

        // If 88 really means "empty/passable" keep equality; otherwise invert.
        return value != 88 && value2 != 1110;
    }

    //Calculates the position for an entity to sit flush with the wall
    public static float getWallPos(float x, GamePanel gp) {
        int tileX = (int)(x/gp.tileSize); //Gets current position
        int newXPos = (tileX) * gp.tileSize; //Sets new position
        return newXPos;
    }
    //Calculates the position for an entity to sit flush with the floor
    public static float getFloorPos(float y, GamePanel gp ) {
        int tileY = (int)(y/gp.tileSize); //Gets current position
        int newYPos = (tileY) * gp.tileSize; //Sets new position
        return newYPos;
    }
    //Checks if entity collides with an entity
    public static boolean entityCheck(Rectangle2D.Float entityHitbox, Rectangle2D.Float playerHitbox) {
        if(playerHitbox.intersects(entityHitbox)) { //Uses the intersects function to check rectangle collision
            return true;
        }
        return false;
    }
    public static boolean onSameLevel(Rectangle2D.Float enemyHitbox, Rectangle2D.Float playerHitbox) {
        //Checks if centre of player is within the top and bottom of the enemy
        if(((playerHitbox.y + playerHitbox.height/2) >= enemyHitbox.y) && (playerHitbox.y + playerHitbox.height/2) <= enemyHitbox.y + enemyHitbox.height) {
            return true;
        }
        return false;
    }
    public static boolean onSameColumn(Rectangle2D.Float enemyHitbox, Rectangle2D.Float playerHitbox) {
        //Checks if centre of player is within the top and bottom of the enemy
        if(((playerHitbox.x + playerHitbox.width/2) >= enemyHitbox.x) && (playerHitbox.x + playerHitbox.width/2) <= enemyHitbox.x + enemyHitbox.width) {
            return true;
        }
        return false;
    }
    //Checks if there are tiles between the entity and the player
    public static boolean isPathClear(String direction, Rectangle2D.Float enemyhitbox, Rectangle2D.Float playerHitbox, GamePanel gp) {
        boolean isClear = true;
        if (direction.equals("Left")) {
            int difference = (int)((enemyhitbox.x - playerHitbox.x)/ gp.tileSize);
            //loops through each tile between it and the player, checks if path is clear
            for(int i = 0; i < difference; i++) {
                if(!tileCheck(enemyhitbox.x - (i * gp.tileSize), enemyhitbox.y, enemyhitbox.width, enemyhitbox.height, gp)) {
                    isClear = false;
                }
            }
        } else if(direction.equals("Right")) {
            int difference = (int)((playerHitbox.x - enemyhitbox.x)/gp.tileSize);
            //loops through each tile between it and the player, checks if path is clear
            for(int i = 0; i < difference; i++) {
                if(!tileCheck(enemyhitbox.x + (i * gp.tileSize), enemyhitbox.y, enemyhitbox.width, enemyhitbox.height, gp)) {
                    isClear = false;
                }
            }
        }
        return isClear;
    }
    //Checks if there are tiles between the entity and the player
    public static boolean isColumnClear(String direction, Rectangle2D.Float enemyhitbox, Rectangle2D.Float playerHitbox, GamePanel gp) {
        boolean isClear = true;
        if (direction.equals("Up")) {
            int difference = (int)((enemyhitbox.y - playerHitbox.y)/ gp.tileSize);
            //loops through each tile between it and the player, checks if path is clear
            for(int i = 0; i < difference; i++) {
                if(!tileCheck(enemyhitbox.x, enemyhitbox.y - (i * gp.tileSize), enemyhitbox.width, enemyhitbox.height, gp)) {
                    isClear = false;
                }
            }
        } else if(direction.equals("Down")) {
            int difference = (int)((playerHitbox.y - enemyhitbox.y)/gp.tileSize);
            //loops through each tile between it and the player, checks if path is clear
            for(int i = 0; i < difference; i++) {
                if(!tileCheck(enemyhitbox.x, enemyhitbox.y + (i * gp.tileSize), enemyhitbox.width, enemyhitbox.height, gp)) {
                    isClear = false;
                }
            }
        }
        return isClear;
    }
    //Checks if patrolling entities need to change direction to stay on a platform
    public static String checkCloseToEdge(String direction, Rectangle2D.Float hitbox, GamePanel gp) {
        if(direction.equals("Left") && (canFallThroughGround(hitbox.x-hitbox.width, hitbox.y+1, hitbox.width, hitbox.height, 1, false, gp, false))) { //Checks the tile below and left of the entity
            return "Right"; //Changes the direction if needed
        } else if(direction.equals("Right") && canFallThroughGround(hitbox.x+hitbox.width, hitbox.y+1, hitbox.width, hitbox.height, 1, false, gp, false)) { //Checks the tile below and right of the entity
            return "Left";  //Changes the direction if needed
        }
        return direction;
    }
    public static boolean isAtEdgeOfFloor(String direction, Rectangle2D.Float hitbox, GamePanel gp, String wallDirection) {
    	float yPos;
    	if(wallDirection.equals("Up")) {
    		yPos = hitbox.y + hitbox.height+1;
    	} else {
    		yPos = hitbox.y - 1;
    	}
        if(direction.equals("Left") && (tileCheck(hitbox.x-hitbox.width, yPos, hitbox.width, hitbox.height, gp))) { //Checks the tile below and left of the entity
            return true; //Changes the direction if needed
        } else if(direction.equals("Right") && tileCheck(hitbox.x+hitbox.width, yPos, hitbox.width, hitbox.height, gp)) { //Checks the tile below and right of the entity
            return true;  //Changes the direction if needed
        }
        return false;
    }
    public static boolean isAtEdgeOfWall(String direction, Rectangle2D.Float hitbox, GamePanel gp, String wallDirection) {
    	float xPos;
    	if(wallDirection.equals("Left")) {
    		xPos = hitbox.x + hitbox.width+1;
    	} else {
    		xPos = hitbox.x -1;
    	}
        if(direction.equals("Up") && (tileCheck(xPos, hitbox.y-hitbox.height, hitbox.width, hitbox.height, gp))) { //Checks the tile below and left of the entity
            return true; //Changes the direction if needed
        } else if(direction.equals("Down") && tileCheck(xPos, hitbox.y+hitbox.height, hitbox.width, hitbox.height, gp)) { //Checks the tile below and right of the entity
            return true;  //Changes the direction if needed
        }
        return false;
    }
    public static boolean isFacingPlayer(Rectangle2D.Float enemyHitbox, Rectangle2D.Float playerHitbox, String direction) {

        int enemyX = (int)(enemyHitbox.x + enemyHitbox.width/2);
        int playerX = (int)(playerHitbox.x + playerHitbox.width/2);

        if(enemyX > playerX) {
            return direction.equals("Left");
        } else {
            return direction.equals("Right");
        }

    }

    public static boolean isInRange(Rectangle2D.Float enemyHitbox, Rectangle2D.Float playerHitbox, float range, GamePanel gp) {

        float enemyX = ((enemyHitbox.x + enemyHitbox.width/2)/gp.tileSize);
        float playerX = ((playerHitbox.x + playerHitbox.width/2)/gp.tileSize);

        float difference = Math.abs(enemyX-playerX);

        if(difference <= range) {
            return true;
        } else {
            return false;
        }

    }
    
    public static boolean isInRadius(Rectangle2D.Float enemyHitbox, Rectangle2D.Float playerHitbox, float range, GamePanel gp) {

        float enemyX = ((enemyHitbox.x + enemyHitbox.width/2)/gp.tileSize);
        float playerX = ((playerHitbox.x + playerHitbox.width/2)/gp.tileSize);
        
        float enemyY = ((enemyHitbox.y + enemyHitbox.height/2)/gp.tileSize);
        float playerY = ((playerHitbox.y + playerHitbox.height/2)/gp.tileSize);

        float xDifference = Math.abs(enemyX-playerX);
        float yDifference = Math.abs(enemyY-playerY);
        
        float sum = (xDifference*xDifference) + (yDifference*yDifference);
        float modulus = (float)Math.sqrt(sum);

        if(modulus <= range) {
            return true;
        } else {
            return false;
        }

    }
    
	public static BufferedImage getMaskedImage(Color c, BufferedImage img) {
		
		BufferedImage displayImage = new BufferedImage((int)img.getWidth(), (int)img.getHeight(), 2);
		
		int argb = c.getRGB();
		displayImage = new BufferedImage((int)img.getWidth(), (int)img.getHeight(), 2);
		for(int i = 0; i < displayImage.getWidth(); i++) {
			for(int j = 0; j < displayImage.getHeight(); j++) {
				if(img.getRGB(i, j) != 0) {
					int x = img.getRGB(i, j);
					int y = mediateARGB(x, argb);
					displayImage.setRGB(i, j, y);
				}
			}
		}
		return displayImage;
	}
	
	public static BufferedImage replaceImageColour(Color c, BufferedImage img) {
		
		BufferedImage displayImage = new BufferedImage((int)img.getWidth(), (int)img.getHeight(), 2);
		
		int argb = c.getRGB();
		displayImage = new BufferedImage((int)img.getWidth(), (int)img.getHeight(), 2);
		for(int i = 0; i < displayImage.getWidth(); i++) {
			for(int j = 0; j < displayImage.getHeight(); j++) {
				if(img.getRGB(i, j) != 0) {
					int x = img.getRGB(i, j);
					displayImage.setRGB(i, j, argb);
				}
			}
		}
		return displayImage;
	}
	private static int mediateARGB(int c1, int c2){
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
	
	public static boolean inLineOfSight(GamePanel gp, Rectangle2D.Float hitbox, Rectangle2D.Float playerHitbox) {
		
		float playerX = playerHitbox.x + playerHitbox.width/2;
        float playerY = playerHitbox.y + playerHitbox.height/2;

        float currentX = hitbox.x + hitbox.width/2;
        float currentY = hitbox.y + hitbox.height/2;

        float dx = currentX - playerX;
        float dy = currentY - playerY;
        
        String direction;
        
        if(playerX < currentX) {
            direction = "Left";
        } else {
            direction = "Right";
        }
        
        double angle = Math.atan2(dy,dx);
        angle = angle+Math.PI;
        
        float length = (float)Math.sqrt((dx*dx) + (dy*dy));
        int intervalNum = 8;
        
        int loopNumber = (int)(length/8f) -2;
        
        for(int i = 0; i < loopNumber; i++) {
        	
        	float xPos = currentX + i*intervalNum * (float)Math.cos(angle);
        	float yPos = currentY + i*intervalNum* (float)Math.sin(angle);
        	
        	int tileX = (int)(xPos/gp.tileSize);
        	int tileY = (int)(yPos/gp.tileSize);
        	
        	if(isTileSolid(xPos, yPos, gp)) {
        		return false;
        	}
        }
		
		return true;
	}
	public static boolean canPlaceBuilding(GamePanel gp, Building building, float x, float y, float width, float height) {
	    if(building.isKitchenBuilding) {
	    	if(!gp.mapM.isInRoom(RoomHelperMethods.KITCHEN)) {
	    		return false;
	    	}
	    }
	    if(building.isStoreBuilding) {
	    	if(!gp.mapM.isInRoom(RoomHelperMethods.STORES)) {
	    		return false;
	    	}
	    }
	    if(building.isBathroomBuilding) {
	    	if(!gp.mapM.isInRoom(RoomHelperMethods.BATHROOM)) {
	    		return false;
	    	}
	    }

		Rectangle2D.Float buildHitbox = new Rectangle2D.Float(x, y, width, height);
	    
	    if ("Shelf".equals(building.getName())) {
	        int tileSize = gp.tileSize;
	        int[][] grid = gp.mapM.currentRoom.mapGrid[1];

	        int startX = (int)(x / tileSize) - 1;
	        int startY = (int)(y / tileSize);
	        int endX = (int)((x + width) / tileSize);
	        int endY = (int)((y + height) / tileSize);

	        // Clamp within bounds
	        startX = Math.max(0, Math.min(startX, grid.length - 1));
	        endX = Math.max(0, Math.min(endX, grid.length - 1));
	        startY = Math.max(0, Math.min(startY, grid[0].length - 1));
	        endY = Math.max(0, Math.min(endY, grid[0].length - 1));
	        
	        // ------------------- Detect left edge -------------------
	        boolean leftEdge = true;
	        for (int iy = startY; iy < endY; iy++) {
	            if (grid[startX][iy] != 0) { // left void should be 0
	                leftEdge = false;
	                break;
	            }
	        }

	        // ------------------- Detect right edge -------------------
	        boolean rightEdge = true;
	        for (int iy = startY; iy < endY; iy++) {
	            if (grid[endX][iy] != 0) { // right void should be 0
	                rightEdge = false;
	                break;
	            }
	        }

	        // ------------------- Check if placement is valid -------------------
	        if (leftEdge || rightEdge) {
	            int inwardX = leftEdge ? startX + 1 : endX - 1; // column that must be wall

	            boolean innerWall = true;
	            for (int iy = startY; iy < endY; iy++) {
	                if (grid[inwardX][iy] == 0) { // must be wall
	                    innerWall = false;
	                    break;
	                }
	            }
	            
	            for (Building b : gp.buildingM.getBuildings()) {
		            if (b != null && b.hitbox.intersects(buildHitbox)) {
		            	if(b.getName().equals("Shelf")) {
		            		return false;
		            	}
		            }
		        }

	            if (innerWall) {
	                return true;
	            }
	        }
	    }
		

	    // ---------------- Universal corner check (mapGrid == 0) ----------------
	    int[][] grid = gp.mapM.currentRoom.mapGrid[1];
	    int tileSize = gp.tileSize;

	    // corners of the hitbox
	    int[][] corners = {
	        { (int) x / tileSize,             (int) y / tileSize             }, // top-left
	        { (int) (x + width) / tileSize,   (int) y / tileSize             }, // top-right
	        { (int) x / tileSize,             (int) (y + height) / tileSize  }, // bottom-left
	        { (int) (x + width) / tileSize,   (int) (y + height) / tileSize  }  // bottom-right
	    };

	    for (int[] c : corners) {
	        int cx = c[0];
	        int cy = c[1];

	        // Bounds safety check
	        if (cx < 0 || cy < 0 || cx >= grid.length || cy >= grid[0].length) {
	            return false; // outside map
	        }

	        if (grid[cx][cy] == 0) {
	            return false; // corner in void tile
	        }
	    }
	    // ---------------- Floor-only placement ----------------
	    if (building.mustBePlacedOnFloor) {
	        for (Building b : gp.buildingM.getBuildings()) {
	            if (b != null && b.hitbox.intersects(buildHitbox)) {
	                return false;
	            }
	        }

	        // Floor items must NOT be on walls
	        int startX = (int) x / tileSize;
	        int startY = (int) y / tileSize;
	        int endX = (int) (x + width) / tileSize;
	        int endY = (int) (y + height) / tileSize;

	        for (int ix = startX; ix < endX; ix++) {
	            for (int iy = startY; iy < endY; iy++) {
	                if (gp.mapM.tiles[grid[ix][iy]].isWall) {
	                    return false;
	                }
	            }
	        }
	        return true;
	    }

	    // ---------------- Table-only placement ----------------
	    if (building.mustBePlacedOnTable) {
	        boolean onTable = false;
	        for (Building b : gp.buildingM.getBuildings()) {
	            if (b == null) continue;

	            boolean isTable = "Table Piece".equals(b.getName()) || "Table Corner 1".equals(b.getName()) || "Table 1".equals(b.getName()) || "Table 2".equals(b.getName());

	            if (b.hitbox.intersects(buildHitbox)) {
	                if (isTable) {
	                    onTable = true;
	                } else {
	                    return false;
	                }
	            }
	        }
	        return onTable;
	    }

	    // ---------------- Can be placed on table or floor ----------------
	    if (building.canBePlacedOnTable) {
	        boolean onTable = false;
	        for (Building b : gp.buildingM.getBuildings()) {
	            if (b == null) continue;

	            boolean isTable = "Table Piece".equals(b.getName()) || "Table Corner 1".equals(b.getName()) || "Table 1".equals(b.getName()) || "Table 2".equals(b.getName());

	            if (b.hitbox.intersects(buildHitbox)) {
	                if (isTable) {
	                    onTable = true;
	                } else {
	                    return false;
	                }
	            }
	        }

	        if (onTable) {
	            return true;
	        } else {
	            // Floor case → must NOT overlap walls
	            for (Building b : gp.buildingM.getBuildings()) {
	                if (b != null && b.hitbox.intersects(buildHitbox)) {
	                    return false;
	                }
	            }

	            int startX = (int) x / tileSize;
	            int startY = (int) y / tileSize;
	            int endX = (int) (x + width) / tileSize;
	            int endY = (int) (y + height) / tileSize;

	            for (int ix = startX; ix < endX; ix++) {
	                for (int iy = startY; iy < endY; iy++) {
	                    if (gp.mapM.tiles[grid[ix][iy]].isWall) {
	                        return false;
	                    }
	                }
	            }
	            return true;
	        }
	    }

	    // ---------------- Wall-only placement ----------------
	    if (building.mustBePlacedOnWall) {
	        for (Building b : gp.buildingM.getBuildings()) {
	            if (b != null && b.hitbox.intersects(buildHitbox)) {
	                return false;
	            }
	        }

	        int startX = (int) x / tileSize;
	        int startY = (int) y / tileSize;
	        int endX = (int) (x + width) / tileSize;
	        int endY = (int) (y + height) / tileSize;

	        for (int ix = startX; ix < endX; ix++) {
	            for (int iy = startY; iy < endY; iy++) {
	                if (!gp.mapM.tiles[grid[ix][iy]].isWall) {
	                    return false;
	                }
	            }
	        }
	        return true;
	    }

	    return false;
	}
	public static BufferedImage reduceImageAlpha(BufferedImage img, float alphaReductionFactor) {
	    BufferedImage displayImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

	    for (int i = 0; i < displayImage.getWidth(); i++) {
	        for (int j = 0; j < displayImage.getHeight(); j++) {
	            int pixel = img.getRGB(i, j);
	            int alpha = (pixel >> 24) & 0xFF; // Extract alpha
	            int red = (pixel >> 16) & 0xFF;
	            int green = (pixel >> 8) & 0xFF;
	            int blue = pixel & 0xFF;

	            // Reduce alpha
	            alpha = Math.max(0, (int) (alpha * alphaReductionFactor));

	            // Reconstruct ARGB value with modified alpha
	            int modifiedPixel = (alpha << 24) | (red << 16) | (green << 8) | blue;
	            displayImage.setRGB(i, j, modifiedPixel);
	        }
	    }
	    return displayImage;
	}
	public static float getDistance(float x1, float y1, float x2, float y2) {

        float dx = x1 - x2;
        float dy = y1 - y2;
        
        float dist = (float)Math.sqrt((dx*dx) + (dy*dy));
        return dist;
	}
}
