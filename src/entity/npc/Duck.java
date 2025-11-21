package entity.npc;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import main.GamePanel;

public class Duck extends NPC {
	
	Random r;
	private boolean leaving = false;
	private int timesPetted = 0;

	public Duck(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		animationSpeedFactor = 2;
		drawScale = 3;
		drawWidth = 32*drawScale;
	    drawHeight = 16*drawScale;
	    xDrawOffset = 24;
		speed = 4;
		npcType = "Duck";
		r = new Random();
		gp.world.animalPresent = true;
	
		
		importImages();
	}
	private void importImages() {
		animations = new BufferedImage[5][10][10];
		
		name = "Duck";
		
		importFromSpriteSheet("/npcs/duck/Duck.png", 2, 1, 0, 0, 0, 32, 16, 0);
		importFromSpriteSheet("/npcs/duck/Duck.png", 5, 1, 1, 0, 16, 32, 16, 0);
	}    
	public void update(double dt) {
		if(!leaving) {
			fleeFromPlayer(dt);
			if(hitbox.intersects(gp.player.hitbox)) {
				if(gp.keyI.ePressed) {
					timesPetted++;
					if(timesPetted >= 60) {
						gp.world.animalPresent = false;
						gp.npcM.removeNPC(this);
						leaving = true;
					}
				}
			}
		} else {
			walking = true;
			leave(dt);
		}
    }
	
	protected void leave(double dt) {
		super.leave(dt);
		gp.gui.addMessage("The duck is leaving now", Color.GREEN);
    }
	   
	public void draw(Graphics2D g2, int xDiff, int yDiff) {
        animationSpeed+=animationUpdateSpeed; //Update the animation frame
        if(walking) {
        	currentAnimation = 1;
    		animationSpeedFactor = 2;
        } else {
          	currentAnimation = 0;
    		animationSpeedFactor = 8;
        }
        if(animationSpeed >= animationSpeedFactor) {
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
	    	  a = 0;
	    	  img = animations[a][currentAnimation][animationCounter];
		    	  if(direction.equals("Right")){
		          	img = createHorizontalFlipped(img);
		          }
	    	  }
	    	  g2.drawImage(img, (int)(hitbox.x - xDrawOffset - xDiff), (int) (hitbox.y - yDrawOffset - yDiff), (int)(drawWidth), (int)(drawHeight), null);
        }
	      
        //g2.setColor(new Color(255, 0, 0, 50)); // red transparent
        //g2.fillOval((int)((hitbox.x + hitbox.width / 2f) - ALERT_DISTANCE - gp.player.xDiff),(int)((hitbox.y + hitbox.height / 2f) - ALERT_DISTANCE - gp.player.yDiff),ALERT_DISTANCE * 2,ALERT_DISTANCE * 2);
	  }
}
