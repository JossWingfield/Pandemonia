package entity.npc;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import entity.buildings.Door;
import main.GamePanel;

public class Server extends Employee {

	private int variant;
	private boolean walking = false;
	private boolean serving = true;
	private boolean takingOrder = false;
	
	private Customer customer;
	private Rectangle2D.Float startHitbox;
	
	public Server(GamePanel gp, int xPos, int yPos) {
		super(gp, xPos, yPos);
		drawWidth = 48;
		drawHeight = 48*2;
		speed = 2;
		
		startHitbox = new Rectangle2D.Float(9*48, 9*48, 48, 48);
		
		npcType = "Server";
		
		r = new Random();
				
		importImages();
	}
	
	private void importImages() {
		 animations = new BufferedImage[5][20][15];
		 importPlayerSpriteSheet("/npcs/employees/server/idle", 4, 1, 0, 0, 0, 80, 80); //IDLE
	     importPlayerSpriteSheet("/npcs/employees/server/walk", 8, 1, 1, 0, 0, 80, 80); //RUN
	        
	     //importPlayerSpriteSheet("/player/holdingIdle", 4, 1, 2, 0, 0, 80, 80); //HOLDING IDLE
	     //importPlayerSpriteSheet("/player/holdingRun", 8, 1, 3, 0, 0, 80, 80); //HOLDING RUN

	     //importPlayerSpriteSheet("/player/pickup", 6, 1, 4, 0, 0, 80, 80); //Pick Up
	     drawWidth = 80*drawScale;
	     drawHeight = 80*drawScale;
	     xDrawOffset = 34*drawScale;
	     yDrawOffset = 36*drawScale;
		 name = "Paulina";
	}
	
	private void findCustomer() {
		customer = gp.mapM.findCustomerWaiting(0);
		if(customer != null) {
			walking = true;
		}
    }
	protected void walkToBase() {
		int goalCol = (int)(9);
	    int goalRow = (int)(9);  
	    searchPath(goalCol, goalRow);
	    
	    if(startHitbox.intersects(hitbox)) {
	    	walking = false;
	    	serving = true;
	    	customer = null;
	    }
    }
	public void update() {
		
		if(customer == null) {
			findCustomer();
		} else {
			if(serving && !takingOrder) {
				int goalCol = (int)((customer.hitbox.x + customer.hitbox.width/2)/gp.tileSize)-1;
		        int goalRow = (int)((customer.hitbox.y + customer.hitbox.height)/gp.tileSize)-1;  
				walkToPoint(goalCol, goalRow);
				if(customer.talkHitbox != null) {
					if(customer.talkHitbox.intersects(hitbox)) {
						takingOrder = true;
						walking = false;
					}
				}
			} else if(takingOrder) {
				if(!customer.waitingToOrder) {
					takingOrder = false;
					walking = true;
					serving = false;
				} else {
					customer.takeOrder();
				}
			} else {
				walkToBase();
			}
		}
		
	}	
    public void draw(Graphics2D g2) {
    	
        animationSpeed+=animationUpdateSpeed; //Update the animation frame
        if(animationSpeed == 5) {
            animationSpeed = 0;
            animationCounter++;
        }
        if(walking) {
    		currentAnimation = 1;
    	} else {
    		currentAnimation = 0;
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
		    	  if(direction.equals("Left") && img != null) {
		          	img = createHorizontalFlipped(img);
		          }
	    	  }   
	    	  g2.drawImage(img, (int)(hitbox.x - xDrawOffset - gp.player.xDiff), (int) (hitbox.y - yDrawOffset - gp.player.yDiff), (int)(drawWidth), (int)(drawHeight), null);
        }
        if(talking) {
        	//gp.gui.drawDialogueScreen(g2, (int)hitbox.x - gp.tileSize*2- gp.player.xDiff, (int)hitbox.y - 48*3- gp.player.yDiff, dialogues[dialogueIndex], this);
        }
    }
}

