package entity.npc;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import entity.buildings.Door;
import entity.buildings.EscapeHole;
import entity.buildings.Fridge;
import entity.items.Food;
import main.GamePanel;

public class Rat extends NPC {
	
	Random r;
	
	private Food currentItem = null;
	
	private EscapeHole escapeHole;
	private Door storeDoor;
	private Fridge fridge;
	
	private boolean fetchingItem, retrievingItem, walking, stocking, depositing;
	private boolean inKitchen = false;
	private int stockTimer = 0;
	private int maxStockTime = 100;

	public Rat(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		animationSpeedFactor = 5;
		drawScale = 3;
		drawWidth = 32*drawScale;
	    drawHeight = 32*drawScale;
	    xDrawOffset = 24;
	    yDrawOffset = 24;
		speed = 3;
		npcType = "Rat";
		r = new Random();
		
		retrievingItem = true;
		fetchingItem = false;
		
		importImages();
	}
	private void findDoor() {
		storeDoor = gp.mapM.findStoreDoor(0);
		if(storeDoor != null) {
			walking = true;
		}
    }
	private void findFridge() {
		fridge = gp.mapM.findFridge(0);
    }
	private void findExitDoor() {
		storeDoor = gp.mapM.findKitchenDoor(1);
    }
	private void findEscapeHole() {
		escapeHole = gp.mapM.findEscapeHole(1);
    }
	private void importImages() {
		animations = new BufferedImage[5][10][10];
		animations[0][0][0] = importImage("/npcs/mannequin.png").getSubimage(16, 0, 16, 32);
		
		name = "Rat";
		
		importPlayerSpriteSheet("/npcs/rat/Run", 4, 1, 0, 0, 0, 32, 32);
	}
	public void update() {
		if(!walking && inKitchen && !depositing) {
			findDoor();
		} else {
			if(fetchingItem) {
				if(inKitchen) {
					int goalCol = (int)((storeDoor.hitbox.x + storeDoor.hitbox.width/2)/gp.tileSize)-1;
			        int goalRow = (int)((storeDoor.hitbox.y + storeDoor.hitbox.height)/gp.tileSize)-1; 
					walkToPoint(goalCol, goalRow);
					if(storeDoor.npcHitbox != null) {
						if(storeDoor.npcHitbox.intersects(hitbox)) {
							inKitchen = false;
							Door door = (Door)gp.mapM.findCorrectDoorInRoom(1, storeDoor.facing);
							if(door != null) {
				    			hitbox.x = door.hitbox.x + door.hitbox.width/2 - hitbox.width/2;
				    			if(door.facing == 0) {
						    		hitbox.y = door.hitbox.y+door.hitbox.height-48;
					    		} else {
					    			hitbox.y = door.hitbox.y+16-48;
					    		}
					    	}
							gp.mapM.addNPCToRoom(this, 1);
							gp.mapM.removeNPCFromRoom(this, 0);
						}
					}
				} else {
					//System.out.println("searching for fridge");
					if(escapeHole != null) {
						int goalCol = (int)((escapeHole.hitbox.x + escapeHole.hitbox.width/2)/gp.tileSize);
				        int goalRow = (int)((escapeHole.hitbox.y + escapeHole.hitbox.height)/gp.tileSize);  
						walkToPoint(goalCol, goalRow);
						if(escapeHole.npcHitbox != null) {
							if(escapeHole.npcHitbox.intersects(hitbox)) {
								if(gp.mapM.isInRoom(1)) {
									gp.npcM.removeNPC(this);
								} else {
									gp.mapM.getRoom(1).removeNPC(this);
								}
							}
						}
					} else {
						findEscapeHole();
					}
				}
			} else if(stocking) {
				stockTimer++;
				if(stockTimer >= maxStockTime) {
					stockTimer = 0;
					stocking = false;
					walking = true;
					retrievingItem = true;
					storeDoor = null;
					currentAnimation = 4;
				}
			} else if(retrievingItem) {
				if(!inKitchen) {
					if(storeDoor == null) {
						findExitDoor();
					} else {
						int goalCol = (int)((storeDoor.hitbox.x + storeDoor.hitbox.width/2)/gp.tileSize);
				        int goalRow = (int)((storeDoor.hitbox.y + storeDoor.hitbox.height)/gp.tileSize) - 2;   
						walkToPoint(goalCol, goalRow);
						if(storeDoor.npcHitbox != null) {
							if(storeDoor.npcHitbox.intersects(hitbox)) {
								inKitchen = true;
								Door door = (Door)gp.mapM.findCorrectDoorInRoom(0, storeDoor.facing);
								if(door != null) {
					    			hitbox.x = door.hitbox.x + door.hitbox.width/2 - hitbox.width/2;
					    			if(door.facing == 0) {
							    		hitbox.y = door.hitbox.y+door.hitbox.height-48;
						    		} else {
						    			hitbox.y = door.hitbox.y+16-48;
						    		}
						    	}
								gp.mapM.addNPCToRoom(this, 0);
								gp.mapM.removeNPCFromRoom(this, 1);
							}
						}
					}
				} else {
					if(!depositing) {
						if(fridge == null) {
							findFridge();
						} else {
							int goalCol = (int)((fridge.hitbox.x + fridge.hitbox.width/2)/gp.tileSize);
					        int goalRow = (int)((fridge.hitbox.y + fridge.hitbox.height)/gp.tileSize);  
							walkToPoint(goalCol, goalRow);
							if(fridge.npcHitbox != null) {
								if(fridge.npcHitbox.intersects(hitbox)) {
									depositing = true;
									walking = false;
								}
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
							pickUpItem();
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
	    
	    	
	    	switch(direction) {
	    	case "Left":
		    	xOffset = 16;
		    	yOffset = 0;
	    	break;
	    	case "Right":
	    	 	xOffset = -16;
		    	yOffset = 0;
		    break;
	    	case "Up":
		    	xOffset = 0;
		    	yOffset = -16;
		    break;
	    	case "Down":
	    		xOffset = 0;
	    		yOffset = 16;
	    	break;
	    	}
	    	
	    	BufferedImage img = currentItem.animations[0][0][0];
	    	if(currentItem instanceof Food) {
	    		Food f = (Food)currentItem;
	    		img = f.getImage();
	    	}
  		g2.drawImage(img, (int)(hitbox.x - gp.player.xDiff- + xOffset), (int)(hitbox.y - gp.player.yDiff + yOffset), (int)(48), (int)(48), null);

	    }
	  private void pickUpItem() {
		  currentItem = fridge.getRandomItem();
	  }
	public void draw(Graphics2D g2) {
        if(direction == "Up") {
        	//drawCurrentItem(g2);
        }
        drawCurrentItem(g2);
        animationSpeed+=animationUpdateSpeed; //Update the animation frame
        if(animationSpeed == 5) {
            animationSpeed = 0;
            animationCounter++;
        }
        if(animations != null) {
            if (animations[0][currentAnimation][animationCounter] == null) { //If the next frame is empty
                animationCounter = 0; //Loops the animation
            }
            
            BufferedImage img = animations[0][currentAnimation][animationCounter];
	    	  int a = 0;
	    	  if(direction != null) {
	    	  switch(direction) {
	    	  case "Left":
	    		  a = 0;
	    		  break;
	    	  case "Right":
	    		  a = 1;
	    		  break;
	    	  case "Up":
	    		  a = 3;
	    	  	  break;
	    	  case "Down":
	    		  a = 2;
	    	  	  break;
	    	  }
	    	  img = animations[a][currentAnimation][animationCounter];
		    	  if(direction.equals("Right")){
		          	img = createHorizontalFlipped(img);
		          }
	    	  }
	    	  g2.drawImage(img, (int)(hitbox.x - xDrawOffset - gp.player.xDiff), (int) (hitbox.y - yDrawOffset - gp.player.yDiff), (int)(drawWidth), (int)(drawHeight), null);
        }
        if(direction != "Up") {
        	//drawCurrentItem(g2);
        }
	      
	  }
}
