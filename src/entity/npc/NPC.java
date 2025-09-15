package entity.npc;

import static utility.CollisionMethods.isTileSolid;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import ai.Node;
import ai.PathFinder;
import entity.Entity;
import entity.buildings.Building;
import main.GamePanel;
import map.Room;
import utility.CollisionMethods;

public abstract class NPC extends Entity {

    GamePanel gp;

    protected String direction;
    PathFinder pathF;
    Random r; 
    
    //DIALOGUE
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
    
    protected int drawWidth, drawHeight, xOffset, yOffset;
    
    private int cooldownCounter = 0;
    private Integer pfGoalCol = null, pfGoalRow = null;

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
    /*
    public void searchPath(int goalCol, int goalRow) {

        Room currentRoom = gp.mapM.getCurrentRoom(this);

        int startX = (int)((hitbox.x + hitbox.width / 2) / gp.tileSize);
        int startY = (int)((hitbox.y + hitbox.height / 2) / gp.tileSize);

        pathF.setNodes(startX, startY, goalCol, goalRow, currentRoom);

        if (pathF.search(currentRoom)) {

            Node node = null;

            if (pathF.pathList.size() > 0) {
                node = pathF.pathList.get(0);

                // Follow the path like before
                for (int j = 0; j < pathF.pathList.size() - 1; j++) {
                    node = node.parent;
                }
            }

            if (node != null) {
                // Compute center of the tile of the final node
                int finalTileX = node.col / 1;
                int finalTileY = node.row / 1;

                float nextXPosition = finalTileX * gp.tileSize + gp.tileSize / 2f;
                float nextYPosition = finalTileY * gp.tileSize + gp.tileSize / 2f;

                // Fly towards the center of the final tile
                flyTowardsPoint(nextXPosition, nextYPosition);
            }
        }
    }
    */
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
    private void moveAtMovementAngle(boolean invertAngle, boolean canPassThroughWalls) { //ADD COLLISIONS

        if(movementAngle != -1) {
            if(direction.equals("Right")) {
            	float dist = (float)(speed*Math.cos(movementAngle));
            	if(CollisionMethods.tileCheck(hitbox.x + dist, hitbox.y, hitbox.width, hitbox.height, gp) || canPassThroughWalls) {
	            		hitbox.x += dist;
	            		direction = "Right";
            	}
            } else {
            	float dist = (float)(speed*Math.cos(movementAngle));
            	if(CollisionMethods.tileCheck(hitbox.x - dist, hitbox.y, hitbox.width, hitbox.height, gp) || canPassThroughWalls) {
	            		hitbox.x -= dist;
	            		direction = "Left";
            	}
            }
            if(!invertAngle) {
            	float dist = (float)(speed*Math.sin(movementAngle));
            	if(CollisionMethods.tileCheck(hitbox.x, hitbox.y+dist, hitbox.width, hitbox.height, gp) || canPassThroughWalls) {
            		hitbox.y += dist;
            	}
            } else {
            	float dist = (float)(speed*Math.sin(movementAngle));
            	if(CollisionMethods.tileCheck(hitbox.x, hitbox.y-dist, hitbox.width, hitbox.height, gp) || canPassThroughWalls) {
            		hitbox.y -= dist;
            	}
            }
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
    protected void walkToBuilding(Building building) {
		if(building != null) {
			int goalCol = (int)((building.hitbox.x + building.hitbox.width/2)/gp.tileSize);
	        int goalRow = (int)((building.hitbox.y + building.hitbox.height/2 - 1)/gp.tileSize);  
	        searchPath(goalCol, goalRow);
		}
    }
    protected void walkToPoint(int x, int y) {
    	searchPath(x, y);
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
