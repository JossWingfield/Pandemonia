package entity.npc;

import static utility.CollisionMethods.isTileSolid;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import ai.Node;
import ai.PathFinder;
import entity.Entity;
import entity.buildings.Building;
import entity.buildings.Door;
import main.GamePanel;
import map.Room;
import utility.CollisionMethods;

public abstract class NPC extends Entity {

    GamePanel gp;

    protected String direction;
    PathFinder pathF;
    Random r; 
    
    //DIALOGUE
    protected Rectangle2D.Float talkHitbox, interactHitbox;
    String dialogues[];
    int dialogueIndex = 0;
    protected boolean talking = false;
    public boolean isSolid = false;
    public BufferedImage portrait;
    protected String npcType;

    protected String name;
    public int type;
    protected boolean followingPlayer;
    protected float speed;
    protected int animationUpdateSpeed = 1;
    protected int idleCounter = 0;
    
    //PATHFINDING
    protected String pathDirection = "";
    private boolean onPath = false;
    private int nextX, nextY;
    protected double movementAngle = -1;
    protected int currentRoomNum = 0;
    
    protected int drawWidth, drawHeight, xOffset, yOffset;
    
    private int cooldownCounter = 0;
    private Integer pfGoalCol = null, pfGoalRow = null;
    
    //FLEE LOGIC
    protected boolean walking, fleeing;
	protected int fleeCooldown = 0; 
	protected final int ALERT_DISTANCE = 2 * 48; 
    protected final int SAFE_DISTANCE = 4 * 48;

    public NPC(GamePanel gp, float xPos, float yPos, float width, float height) {
        super(gp, xPos, yPos, width, height);
        this.gp = gp;
        setDialogue();
        pathF = new PathFinder(gp);
        r = new Random();
    }
    
    protected void setDialogue() {}
    public String getName() {
    	return name;
    }
    protected void nextDialogueLine() {
        dialogueIndex++;
        if(dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        }
    }
    public String getNPCType() {
		return npcType;
	}
	public void update() {
		
		if(CollisionMethods.getDistance(hitbox.x, hitbox.y, gp.player.hitbox.x, gp.player.hitbox.y) > gp.player.talkDistance) {
			talking = false;
		}
		
		if(cooldownCounter > 0) {
			cooldownCounter--;
		}
		
		if(talking) {
			if(gp.keyI.qPressed && cooldownCounter == 0) {
				gp.keyI.qPressed = false;
		        nextDialogueLine();
		        cooldownCounter = 4;
			}
		}
		
	}
	protected void fleeFromPlayer() {
        float playerX = gp.player.hitbox.x + gp.player.hitbox.width / 2f;
        float playerY = gp.player.hitbox.y + gp.player.hitbox.height / 2f;
        float duckX = hitbox.x + hitbox.width / 2f;
        float duckY = hitbox.y + hitbox.height / 2f;

        float dx = duckX - playerX;
        float dy = duckY - playerY;
        float distSq = dx * dx + dy * dy;

        if (fleeCooldown > 0) fleeCooldown--;

        if (!fleeing && distSq < ALERT_DISTANCE * ALERT_DISTANCE) {
            startFleeing();
        }

        if (fleeing) {
            // Recalculate if the player gets close again while fleeing
            if (distSq < ALERT_DISTANCE * ALERT_DISTANCE && fleeCooldown == 0) {
                fleeCooldown = 30; // short delay before next recalculation
                fleeHelperMethod();
            }

            // Continue following or find a new path if finished
            if (pathF.pathList.isEmpty()) {
            	fleeHelperMethod();
            } else {
                followPath();
            }

            // Stop fleeing when safe again
            if (distSq > SAFE_DISTANCE * SAFE_DISTANCE && fleeCooldown == 0) {
                stopFleeing();
            }
        }
	}
    private void fleeHelperMethod() {
        float playerX = gp.player.hitbox.x + gp.player.hitbox.width / 2f;
        float playerY = gp.player.hitbox.y + gp.player.hitbox.height / 2f;
        float duckX = hitbox.x + hitbox.width / 2f;
        float duckY = hitbox.y + hitbox.height / 2f;

        float dx = duckX - playerX;
        float dy = duckY - playerY;
        float mag = (float) Math.sqrt(dx * dx + dy * dy);
        if (mag == 0) return;

        dx /= mag;
        dy /= mag;

        Room currentRoom = gp.mapM.getCurrentRoom(this);

        for (int attempt = 0; attempt < 8; attempt++) {
            float angleOffset = (r.nextFloat() - 0.5f) * (float) Math.PI / 1.5f;
            float cos = (float) Math.cos(angleOffset);
            float sin = (float) Math.sin(angleOffset);

            int[] randomNode = pathF.getRandomReachableNode(currentRoom, duckX, duckY, ALERT_DISTANCE);
            int goalCol = randomNode[0];
            int goalRow = randomNode[1];

            pfGoalCol = null;
            pfGoalRow = null;
            pathF.setNodes(
                (int)(duckX / gp.tileSize),
                (int)(duckY / gp.tileSize),
                goalCol, goalRow,
                currentRoom
            );

            if (pathF.search(currentRoom)) {
                searchPath(goalCol, goalRow);
            }
        }
    }

    protected void startFleeing() {
        fleeing = true;
        walking = true;
        fleeCooldown = 60;
        fleeFromPlayer();
    }

    protected void stopFleeing() {
        fleeing = false;
        walking = false;
        pathF.pathList.clear();
    }
    protected void followPath() {
        if (pathF.pathList.isEmpty()) return;

        Node node = pathF.pathList.get(0);
        float cell = gp.tileSize / (float) pathF.nodesPerTile;
        float targetX = node.col * cell + cell * 0.5f;
        float targetY = node.row * cell + cell * 0.5f;

        flyTowardsPoint(targetX, targetY);

        float cx = hitbox.x + hitbox.width * 0.5f;
        float cy = hitbox.y + hitbox.height * 0.5f;
        float tol = Math.max(2f, cell * 0.35f);
        float dx = cx - targetX;
        float dy = cy - targetY;

        if (dx * dx + dy * dy <= tol * tol) {
            pathF.pathList.remove(0);
        }
    }
	public void searchFullPath(int goalCol, int goalRow) {
	    Room currentRoom = gp.mapM.getCurrentRoom(this);

	    // If no path exists, create one
	    if (pathF.pathList.isEmpty() && pfGoalCol != null && pfGoalRow != null) {
	        int startX = (int)((hitbox.x + hitbox.width  * 0.5f) / gp.tileSize);
	        int startY = (int)((hitbox.y + hitbox.height * 0.5f) / gp.tileSize);
	        pathF.setNodes(startX, startY, goalCol, goalRow, currentRoom);
	        if (!pathF.search(currentRoom)) return;
	    }

	    if (pathF.pathList.isEmpty()) return;

	    Node node = pathF.pathList.get(0);
	    float cell = gp.tileSize / (float) pathF.nodesPerTile;
	    float targetX = node.col * cell + cell * 0.5f;
	    float targetY = node.row * cell + cell * 0.5f;

	    flyTowardsPoint(targetX, targetY);

	    float cx = hitbox.x + hitbox.width * 0.5f;
	    float cy = hitbox.y + hitbox.height * 0.5f;
	    float tol = Math.max(2f, cell * 0.35f);

	    float dx = cx - targetX;
	    float dy = cy - targetY;

	    // Reached this node
	    if (dx * dx + dy * dy <= tol * tol) {
	        pathF.pathList.remove(0);

	        // If we’ve reached the final node, clear goal
	        if (pathF.pathList.isEmpty()) {
	            pfGoalCol = null;
	            pfGoalRow = null;
	        }
	    }
	}
    protected void importPlayerSpriteSheet(String filePath, int columnNumber, int rowNumber, int currentAnimation, int startX, int startY, int width, int height) {

    	for(int k = 0; k < 4; k++) {
	        int arrayIndex = 0;
	
	        BufferedImage img = importImage(filePath + ".png");
	        //BufferedImage handImage = importImage(filePath + "Hand.png");
	
	        for(int i = 0; i < columnNumber; i++) {
	            for(int j = 0; j < rowNumber; j++) {
	                animations[k][currentAnimation][arrayIndex] = img.getSubimage(i*width + startX, j*height + startY, width, height);
	                //handImages[k][currentAnimation][arrayIndex] = handImage.getSubimage(i*width + startX, j*height + startY, width, height);
	                arrayIndex++;
	            }
	        }
	        if(k > 0) {
	        	startY += height;
	        }
    	}

    }
    public void drawHitbox(Graphics2D g) {
        g.drawRect((int)(hitbox.x- gp.player.xDiff), (int) (hitbox.y- gp.player.yDiff), (int)(hitbox.width), (int)(hitbox.height));
    }
    
    public void interact() {
    	talking = !talking;
    }
    public void stopTalking() {
    	talking = false;
    }
    protected void moveTowardsPlayer() {

    	onPath = true;
        if(onPath) {

            int goalCol = (int)((gp.player.hitbox.x + gp.player.hitbox.width/2)/gp.tileSize);
            int goalRow = (int)((gp.player.hitbox.y + gp.player.hitbox.height/2 - 1)/gp.tileSize);
            
            searchPath(goalCol, goalRow);
        }

    }
    public void searchPath(int goalCol, int goalRow) {

        Room currentRoom = gp.mapM.getCurrentRoom(this);

        // Rebuild path only when needed (goal changed or no current path)
        boolean needNewPath =
                pathF.pathList.isEmpty() ||
                pfGoalCol == null || pfGoalRow == null ||
                goalCol != pfGoalCol || goalRow != pfGoalRow;

        if (needNewPath) {
            int startX = (int)((hitbox.x + hitbox.width  * 0.5f) / gp.tileSize);
            int startY = (int)((hitbox.y + hitbox.height * 0.5f) / gp.tileSize);

            pathF.setNodes(startX, startY, goalCol, goalRow, currentRoom);

            // If no path found, bail this frame
            if (!pathF.search(currentRoom)) return;

            pfGoalCol = goalCol;
            pfGoalRow = goalRow;
        }

        if (pathF.pathList.isEmpty()) {
            // Already at the goal sub-node (nothing to follow)
            return;
        }

        // Follow the FIRST node in the list; remove it when reached
        Node node = pathF.pathList.get(0);

        // Size of one sub-node cell in world units
        float cell = gp.tileSize / (float) pathF.nodesPerTile;

        // Center of the sub-node in world coordinates
        float targetX = node.col * cell + cell * 0.5f;
        float targetY = node.row * cell + cell * 0.5f;

        // Move towards this sub-node
        flyTowardsPoint(targetX, targetY);

        // If close enough to this sub-node, consume it
        float cx = hitbox.x + hitbox.width  * 0.5f;
        float cy = hitbox.y + hitbox.height * 0.5f;

        // Tolerance scales with sub-node size (min ~2px)
        float tol = Math.max(2f, cell * 0.35f);
        float dx = cx - targetX;
        float dy = cy - targetY;

        if (dx * dx + dy * dy <= tol * tol) {
            // reached this node; move to the next one on the next update
            pathF.pathList.remove(0);
        }
    }
    protected void flyTowardsPoint(float xPos, float yPos) {

        float enemyX = (int)((hitbox.x + hitbox.width/2));
        float enemyY = (int)((hitbox.y + hitbox.height/2));
        if (enemyX < xPos) {
        	hitbox.x += speed; 
        	direction = "Right";
        }
        if (enemyX > xPos) {
        	hitbox.x -= speed;
      		direction = "Left";
        }
        if (enemyY < yPos) {
        	hitbox.y += speed;
        	direction = "Down";
        }
        if (enemyY > yPos) {
        	hitbox.y -= speed;
           	direction = "Up";
        }
        
    }
    protected void drawNextNode(Graphics2D g2) {
        if (gp.mapM.drawPath) {
            g2.setColor(new Color(100, 255, 150, 80));
            g2.fillRect(nextX * gp.tileSize, nextY * gp.tileSize, 48, 48);
        }

        g2.setColor(new Color(100, 255, 150, 80));
        if (pathF.pathList.size() > 0) {
            // Iterate through all the nodes in the pathList
            for (int j = 0; j < pathF.pathList.size(); j++) {
                Node node = pathF.pathList.get(j);
                int x = node.col * gp.tileSize;
                int y = node.row * gp.tileSize;
                // Draw a rectangle for each node in the path
                g2.fillRect(x, y, gp.tileSize, gp.tileSize);
            }
        }
    }
    protected boolean walkToBuilding(Building building, Rectangle2D.Float stopHitbox) {
    	if(stopHitbox == null) {
    		return false;
    	}
		if(building != null) {
	    	walking = true;
			int goalCol = (int)((stopHitbox.x + stopHitbox.width/2)/gp.tileSize);
	        int goalRow = (int)((stopHitbox.y + stopHitbox.height/2 - 1)/gp.tileSize);  
	        searchPath(goalCol, goalRow);
	        if(stopHitbox.intersects(hitbox)) {
	        	return true;
	        }
		}
		return false;
    }
    protected boolean walkToBuildingWithInteractHitbox(Building building, Rectangle2D.Float stopHitbox) {
		if(building != null) {
	    	walking = true;
			int goalCol = (int)((stopHitbox.x + stopHitbox.width/2)/gp.tileSize);
	        int goalRow = (int)((stopHitbox.y + stopHitbox.height/2 - 1)/gp.tileSize);  
	        searchPath(goalCol, goalRow);
	        if(stopHitbox.intersects(interactHitbox)) {
	        	return true;
	        }
		}
		return false;
    }
    protected boolean walkToBuildingWithName(String name, boolean isNPCHitbox) {
    	Building building = findBuildingInRoom(name, currentRoomNum);
    	walking = true;
		if(building != null) {
			Rectangle2D.Float stopHitbox = building.hitbox;
			if(isNPCHitbox) {
				if(building.npcHitbox != null) {
					stopHitbox =building.npcHitbox;
				}
			}
			int goalCol = (int)((stopHitbox.x + stopHitbox.width/2)/gp.tileSize);
	        int goalRow = (int)((stopHitbox.y + stopHitbox.height/2 - 1)/gp.tileSize);  
	        searchPath(goalCol, goalRow);
	        if(stopHitbox.intersects(hitbox)) {
	        	return true;
	        }
		}
		return false;
    }
    protected boolean walkToDoorWithDoorNum(int doorRoomNum) {
    	Door door;
    	if(gp.mapM.currentRoom.equals(gp.mapM.getRoom(currentRoomNum))) {
			door = gp.buildingM.findDoor(doorRoomNum);
		} else {
			door =gp.mapM.getRoom(currentRoomNum).findDoor(doorRoomNum);
		}
    	
		int goalCol = (int)((door.npcHitbox.x + door.npcHitbox.width/2)/gp.tileSize);
        int goalRow = (int)((door.npcHitbox.y + door.npcHitbox.height/2 - 1)/gp.tileSize);  
        searchPath(goalCol, goalRow);
    	
    	walking = true;
    	if(door.npcHitbox != null) {
    		if(door.npcHitbox.intersects(hitbox)) {
    			changeRoom(door.roomNum, door.facing);
    			return true;
    		}
    	}
    	
    	return false;
    }
    protected void removeNPCFromRoom() {
		if(gp.mapM.isInRoom(currentRoomNum)) {
			gp.npcM.removeNPC(this);
		} else {
			gp.mapM.getRoom(currentRoomNum).removeNPC(this);
		}
    }
    protected void walkToPoint(int x, int y) {
    	searchPath(x, y);
    	walking = true;
    }
    protected Building findBuildingInRoom(String buildingName, int roomNum) {
		if(gp.mapM.currentRoom.equals(gp.mapM.getRoom(roomNum))) {
			return gp.buildingM.findBuildingWithName(buildingName);
		} else {
			return gp.mapM.getRoom(roomNum).findBuildingWithName(buildingName);
		}
    }
    protected void changeRoom(int newRoomNum, int prevDoorFacing) {
		Door door = (Door)gp.mapM.findCorrectDoorInRoom(newRoomNum, prevDoorFacing);
    	if(door != null) {
    		if(door.facing == 2) {
    			hitbox.x = door.hitbox.x + door.hitbox.width + 32;
    		} else if(door.facing == 3) {
    			hitbox.x = door.hitbox.x - 32;
    		}  else {
    			hitbox.x = door.hitbox.x + door.hitbox.width/2 - hitbox.width/2;
    		}
    		
    		if(door.facing == 0) {
	    		hitbox.y = door.hitbox.y+door.hitbox.height-48;
    		} else if(door.facing == 1){
    			hitbox.y = door.hitbox.y+16-48;
    		} else {
    			hitbox.y = door.hitbox.y+48;
    		}
    	}
		gp.mapM.addNPCToRoom(this, newRoomNum);
		gp.mapM.removeNPCFromRoom(this, currentRoomNum);
		currentRoomNum = newRoomNum;
    }
    protected boolean walkToNPC(NPC npc) {
		int goalCol = (int)((npc.hitbox.x + npc.hitbox.width/2)/gp.tileSize)-1;
        int goalRow = (int)((npc.hitbox.y + npc.hitbox.height)/gp.tileSize)-1;  
		walkToPoint(goalCol, goalRow);
		if(npc.talkHitbox != null) {
			if(npc.talkHitbox.intersects(hitbox)) {
				return true;
			}
		}
		return false;
    }
    public void draw(Graphics2D g2) {
        animationSpeed+=animationUpdateSpeed; //Update the animation frame
        if(animationSpeed == 12) {
            animationSpeed = 0;
            animationCounter++;
        }
        if(animations != null) {
            if (animations[0][currentAnimation][animationCounter] == null) { //If the next frame is empty
                animationCounter = 0; //Loops the animation
            }
            g2.drawImage(animations[0][currentAnimation][animationCounter], (int)(hitbox.x - xOffset - gp.player.xDiff), (int) (hitbox.y - yOffset - gp.player.yDiff), (int)(drawWidth), (int)(drawHeight), null);
        }
        if(talking) {
        	//gp.gui.drawDialogueScreen(g2, (int)hitbox.x - gp.tileSize*2- gp.player.xDiff, (int)hitbox.y - 48*3- gp.player.yDiff, dialogues[dialogueIndex], this);
        }
    }
    
}
