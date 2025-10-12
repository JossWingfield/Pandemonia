package entity.npc;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import entity.buildings.Door;
import entity.buildings.Fridge;
import entity.buildings.StorageFridge;
import entity.items.Food;
import entity.items.Item;
import entity.items.Plate;
import main.GamePanel;
import utility.Recipe;
import utility.RoomHelperMethods;

public class Stocker extends Employee {
	
	Random r;
	
	private Food currentItem = null;
	private int variant;
	
	private StorageFridge storeFridge;
	private Fridge fridge;
	
	private boolean fetchingItem, retrievingItem, stocking, depositing;
	private boolean inKitchen = true;
	private int stockTimer = 0;
	private int maxStockTime = 100;
	
	public Stocker(GamePanel gp, int xPos, int yPos) {
		super(gp, xPos, yPos);
		drawWidth = 48;
		drawHeight = 48*2;
		speed = 1;
		
		npcType = "Stocker";
		
		r = new Random();
				
		importImages();
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
		 name = "Gazza";
	}
	private void findFridge() {
		fridge = (Fridge)findBuildingInRoom("Fridge", currentRoomNum);
    }
	public void update() {
		if(!walking && inKitchen && !depositing) {
			walking = true;
			fetchingItem = true;
		} else {
			if(fetchingItem) {
				if(walkToBuildingInRoom("Storage Fridge", RoomHelperMethods.STORES, true)) {
					stocking = true;
					walking = false;
					fetchingItem = false;
					inKitchen = false;
				}
			} else if(stocking) {
				stockTimer++;
				if(stockTimer >= maxStockTime) {
					stockTimer = 0;
					stocking = false;
					walking = true;
					retrievingItem = true;
					storeFridge = null;
					currentAnimation = 4;
					pickUpItem();
				}
			} else if(retrievingItem) {
				if(!inKitchen) {
					if(walkToDoorWithDoorNum(RoomHelperMethods.MAIN)) {
						inKitchen = true;
					}
				} else {
					if(!depositing) {
						if(fridge == null) {
							findFridge();
						} else {
							if(walkToBuilding(fridge, fridge.npcHitbox)) {
								depositing = true;
								walking = false;
							}
						}
					} else {
						stockTimer++;
						if(stockTimer >= maxStockTime) {
							stockTimer = 0;
							depositing = false;
							walking = false;
							fetchingItem = true;
							retrievingItem = false;
							fridge.addItem((Food)gp.itemRegistry.getItemFromName(currentItem.getName(), 0));
							currentItem = null;
							fridge = null;
						}
					}
				}
			}
		}
		
	}	
	public void drawCurrentItem(Graphics2D g2) {
	    	if(currentItem == null) {
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
	    	
	    	BufferedImage img = currentItem.animations[0][0][0];
	    	if(currentItem instanceof Food) {
	    		Food f = (Food)currentItem;
	    		img = f.getImage();
	    	}
    		g2.drawImage(img, (int)(hitbox.x - gp.player.xDiff- xDrawOffset + xOffset), (int)(hitbox.y - gp.player.yDiff - yDrawOffset + yOffset), (int)(48), (int)(48), null);

	    }
    private void pickUpItem() {
    	if(storeFridge == null) {
    		storeFridge = (StorageFridge)findBuildingInRoom("Storage Fridge", currentRoomNum);
    	}
    	if(gp.world.getTodaysMenu().size() > 0) {
	    	int recipeIndex = r.nextInt(gp.world.getTodaysMenu().size());
	    	Recipe recipe = gp.world.getTodaysMenu().get(recipeIndex);
	    	currentItem = (Food)gp.itemRegistry.getItemFromName(recipe.getIngredients().get(r.nextInt(recipe.getIngredients().size())), 0);
    	} else {
    		currentItem = storeFridge.getRandomItem();
    	}
		
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
        
        if(currentItem == null) {
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
        if(talking) {
        	//gp.gui.drawDialogueScreen(g2, (int)hitbox.x - gp.tileSize*2- gp.player.xDiff, (int)hitbox.y - 48*3- gp.player.yDiff, dialogues[dialogueIndex], this);
        }
        if(direction != "Up") {
        	drawCurrentItem(g2);
        }
    }
}
