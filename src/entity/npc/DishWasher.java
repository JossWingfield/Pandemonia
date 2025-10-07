package entity.npc;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import entity.buildings.Sink;
import entity.buildings.TablePlate;
import entity.items.Food;
import entity.items.Plate;
import main.GamePanel;

public class DishWasher extends Employee {

	private int variant;
	private boolean collectingPlates = true;
	private boolean washingPlates = false;
	
	private Sink sink;
	private TablePlate table;
	private Plate plate;
	
	public DishWasher(GamePanel gp, int xPos, int yPos) {
		super(gp, xPos, yPos);
		drawWidth = 48;
		drawHeight = 48*2;
		speed = 2;
		direction = "Left";
				
		npcType = "Dish Washer";
		
		r = new Random();
				
		importImages();
		interactHitbox = new Rectangle2D.Float(0, 0, 1, 1);
	}
	
	private void importImages() {
		 animations = new BufferedImage[5][20][15];
		 importPlayerSpriteSheet("/player/idle", 4, 1, 0, 0, 0, 80, 80); //IDLE
	     importPlayerSpriteSheet("/player/run", 8, 1, 1, 0, 0, 80, 80); //RUN
	        
	     importPlayerSpriteSheet("/player/holdingIdle", 4, 1, 2, 0, 0, 80, 80); //HOLDING IDLE
	     importPlayerSpriteSheet("/player/holdingRun", 8, 1, 3, 0, 0, 80, 80); //HOLDING RUN

	     importPlayerSpriteSheet("/player/pickup", 6, 1, 4, 0, 0, 80, 80); //Pick Up
	     
	     drawWidth = 80*drawScale;
	     drawHeight = 80*drawScale;
	     xDrawOffset = 34*drawScale;
	     yDrawOffset = 36*drawScale;
		 name = "Gav";
	}
	
	private void findTable() {
		table =  (TablePlate)findBuildingInRoom("Table Plate", currentRoomNum);
    }
	private void findSink() {
		sink = (Sink)findBuildingInRoom("Kitchen Sink 1", currentRoomNum);
    }
    public void updateInteractHitbox() {
        float baseX = hitbox.x;
        float baseY = hitbox.y;

        float horizontalWidth = 48;
        float horizontalHeight = 16;

        float verticalWidth = 16;
        float verticalHeight = 48;

        switch (direction) {
            case "Right": // RIGHT
                interactHitbox = new Rectangle2D.Float(
                    baseX + hitbox.width,
                    baseY + (hitbox.height / 2f - horizontalHeight / 2f),
                    horizontalWidth,
                    horizontalHeight
                );
                break;
            case "Left": // LEFT
                interactHitbox = new Rectangle2D.Float(
                    baseX - horizontalWidth,
                    baseY + (hitbox.height / 2f - horizontalHeight / 2f),
                    horizontalWidth,
                    horizontalHeight
                );
                break;
            case "Down": // DOWN
                interactHitbox = new Rectangle2D.Float(
                    baseX + (hitbox.width / 2f - verticalWidth / 2f),
                    baseY + hitbox.height,
                    verticalWidth,
                    verticalHeight
                );
                break;
            case "Up": // UP
                interactHitbox = new Rectangle2D.Float(
                    baseX + (hitbox.width / 2f - verticalWidth / 2f),
                    baseY - verticalHeight,
                    verticalWidth,
                    verticalHeight
                );
                break;
        }
    }
	protected void walkToSink() {
		if(sink == null) {
			findSink();
		}
		
		if(walkToBuildingWithInteractHitbox(sink, sink.npcHitbox)) {
	    	walking = false;
	    	washingPlates = true;
	    	sink.addPlate(plate);
	    	plate = null;
	    	table = null;
		}
    }
	public void update() {
		updateInteractHitbox();
		
		if(table == null) {
			findTable();
		} else {
			if(collectingPlates) {
				if(walkToBuilding(table, table.npcHitbox)) {
					collectingPlates = false;
					table.showDirtyPlate = false;
					plate = table.plate;
					table.plate = null;
				}
			} else if(washingPlates) {
				if(!sink.hasDirtyPlates()) {
					collectingPlates = true;
					washingPlates = false;
					table = null;
				} else {
					sink.washPlates();
					walking = false;
				}
			} else {
				walkToSink();
			}
		}
		
	}	
	public void drawCurrentItem(Graphics2D g2) {
	    	if(plate == null) {
	    		return;
	    	}
	    	
	    	int xOffset = 120 - 24;
	    	int yOffset = 132 - 24;
	    	
	    	xOffset = 120-24;
	    	yOffset = 132-(48+8);
	    	int finalOffset = yOffset;
	    	int baseOffset = 132-(16); 

	    	if(currentAnimation == 4) {
		    	int currentStage = animationCounter; // goes 0 → 6
		    	int totalStages = 2;  
		    	
		    	// Clamp to avoid going out of range
		    	if (currentStage < 0) currentStage = 0;
		    	if (currentStage > totalStages) currentStage = totalStages;
		    	yOffset = baseOffset + (finalOffset - baseOffset) * currentStage / totalStages;
	    	}
	    	
	    	BufferedImage img = plate.animations[0][0][0];
  		g2.drawImage(img, (int)(hitbox.x - gp.player.xDiff- xDrawOffset + xOffset), (int)(hitbox.y - gp.player.yDiff - yDrawOffset + yOffset), (int)(48), (int)(48), null);

	    }
    public void draw(Graphics2D g2) {
        if(direction == "Up") {
        	drawCurrentItem(g2);
        }
        animationSpeed+=animationUpdateSpeed; //Update the animation frame
        if(animationSpeed == 5) {
            animationSpeed = 0;
            animationCounter++;
        }
        
        if(plate == null) {
        	if(walking) {
        		currentAnimation = 1;
        	} else {
        		currentAnimation = 0;
        	}
        } else {
        	if(currentAnimation != 4) {
            	if(walking) {
            		currentAnimation = 3;
            	} else {
            		currentAnimation = 2;
            	}
        	}
        }
        if(animations != null) {
            if (animations[0][currentAnimation][animationCounter] == null) { //If the next frame is empty
                animationCounter = 0; //Loops the animation
                if(currentAnimation == 4) {
                	currentAnimation = 2;
                }
            }
            
            BufferedImage img = animations[0][currentAnimation][animationCounter];
	    	  int a = 0;
	    	  if(direction != null) {
	    	  switch(direction) {
	    	  case "Left":
	    		  a = 1;
	    		  break;
	    	  case "Right":
	    		  a = 0;
	    		  break;
	    	  case "Up":
	    		  a = 3;
	    	  	  break;
	    	  case "Down":
	    		  a = 2;
	    	  	  break;
	    	  }
	    	  img = animations[a][currentAnimation][animationCounter];
		    	  if(direction.equals("Left")){
		          	img = createHorizontalFlipped(img);
		          }
	    	  }   
	    	  g2.drawImage(img, (int)(hitbox.x - xDrawOffset - gp.player.xDiff), (int) (hitbox.y - yDrawOffset - gp.player.yDiff), (int)(drawWidth), (int)(drawHeight), null);
        }
        
        //g2.setColor(Color.YELLOW);
      	//g2.drawRect((int)interactHitbox.x, (int)interactHitbox.y, (int)interactHitbox.width, (int)interactHitbox.height);
        
        if(talking) {
        	//gp.gui.drawDialogueScreen(g2, (int)hitbox.x - gp.tileSize*2- gp.player.xDiff, (int)hitbox.y - 48*3- gp.player.yDiff, dialogues[dialogueIndex], this);
        }
        if(direction != "Up") {
        	drawCurrentItem(g2);
        }
    }
}
