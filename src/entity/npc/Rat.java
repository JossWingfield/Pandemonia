package entity.npc;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import entity.buildings.Door;
import entity.buildings.EscapeHole;
import entity.buildings.Fridge;
import entity.items.Food;
import main.GamePanel;
import utility.RoomHelperMethods;

public class Rat extends NPC {
	
	Random r;
	
	private Food currentItem = null;
	
	private Fridge fridge;
	
	private boolean fetchingItem, retrievingItem, stocking, depositing;
	private boolean inKitchen = false;
	private double stockTimer = 0;
	private double maxStockTime = 1.4;

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
	private void importImages() {
		animations = new BufferedImage[5][10][10];
		animations[0][0][0] = importImage("/npcs/mannequin.png").getSubimage(16, 0, 16, 32);
		
		name = "Rat";
		
		importPlayerSpriteSheet("/npcs/rat/Run", 4, 1, 0, 0, 0, 32, 32);
	}
	private void findFridge() {
		fridge = (Fridge)findBuildingInRoom("Fridge", RoomHelperMethods.KITCHEN);
    }
	public void update(double dt) {
		if(!walking && inKitchen && !depositing) {
			walking = true;
		} else {
			if(fetchingItem) {
				if(inKitchen) {
					if(walkToBuildingInRoom(dt, "Escape Hole", RoomHelperMethods.STORES)) {
						removeNPCFromRoom();
					}
				}
			} else if(stocking) {
				stockTimer+=dt;
				if(stockTimer >= maxStockTime) {
					stockTimer = 0;
					stocking = false;
					walking = true;
					retrievingItem = true;
					currentAnimation = 4;
				}
			} else if(retrievingItem) {
				if(!inKitchen) {
					if(walkToDoorWithDoorNum(dt, RoomHelperMethods.KITCHEN)) {
						inKitchen = true;
					}
				} else {
					if(!depositing) {
						if(fridge == null) {
							findFridge();
						} else {
							if(walkToBuilding(dt, fridge)) {
								depositing = true;
								walking = false;
							}
						}
					} else {
						stockTimer+=dt;
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
	  public void drawCurrentItem(Graphics2D g2, int xDiff, int yDiff) {
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
  		g2.drawImage(img, (int)(hitbox.x - xDiff- + xOffset), (int)(hitbox.y - yDiff + yOffset), (int)(48), (int)(48), null);

	    }
	  private void pickUpItem() {
		  currentItem = fridge.getRandomItem();
	  }
	public void draw(Graphics2D g2, int xDiff, int yDiff) {
        if(direction == "Up") {
        	//drawCurrentItem(g2);
        }
        drawCurrentItem(g2, xDiff, yDiff);
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
	    	  g2.drawImage(img, (int)(hitbox.x - xDrawOffset - xDiff), (int) (hitbox.y - yDrawOffset - yDiff), (int)(drawWidth), (int)(drawHeight), null);
        }
        if(direction != "Up") {
        	//drawCurrentItem(g2);
        }
	      
	  }
}
