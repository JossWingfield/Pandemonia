package entity.npc;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import main.GamePanel;

public class Pet extends NPC{
	
	Random r;
	private boolean leaving = false;
	private int timesPetted = 0;
	private Customer owner;
	private int petType;

	public Pet(GamePanel gp, float xPos, float yPos, Customer npc, int petType) {
		super(gp, xPos, yPos, 48, 48);
		this.owner = npc;
		this.petType = petType;
		animationSpeedFactor = 0.1;
		drawScale = 3;
		drawWidth = 32*drawScale;
	    drawHeight = 16*drawScale;
	    xDrawOffset = 24;
		speed = 1*60;
		npcType = "Pet";
		r = new Random();
		
		gp.world.animalPresent = true;
	
		importImages();
	}
	private void importImages() {
		animations = new BufferedImage[5][10][10];
		
		switch(petType) {
		case 0:
			name = "Dexter";
			importFromSpriteSheet("/npcs/pet/Dog.png", 6, 1, 0, 0, 0, 32, 16, 0);
			importFromSpriteSheet("/npcs/pet/Dog.png", 8, 1, 1, 0, 16, 32, 16, 0);
			break;
		case 1:
			drawWidth = 48*3;
			drawHeight = 48*3;
			xDrawOffset = 48;
			yDrawOffset = 48;
			name = "Catherine";
			importFromSpriteSheet("/npcs/pet/Cat1.png", 6, 1, 0, 0, 0, 48, 48, 0);
			importFromSpriteSheet("/npcs/pet/Cat1.png", 8, 1, 1, 0, 96, 48, 48, 0);
			break;
		case 2:
			drawWidth = 48*3;
			drawHeight = 48*3;
			xDrawOffset = 48;
			yDrawOffset = 48;
			name = "Snorf";
			importFromSpriteSheet("/npcs/pet/Cat2.png", 6, 1, 0, 0, 0, 48, 48, 0);
			importFromSpriteSheet("/npcs/pet/Cat2.png", 8, 1, 1, 0, 96, 48, 48, 0);
			break;
		case 3:
			drawWidth = 48*3;
			drawHeight = 48*3;
			xDrawOffset = 48;
			yDrawOffset = 48;
			name = "Olive";
			importFromSpriteSheet("/npcs/pet/Cat3.png", 6, 1, 0, 0, 0, 48, 48, 0);
			importFromSpriteSheet("/npcs/pet/Cat3.png", 8, 1, 1, 0, 96, 48, 48, 0);
			break;
		case 4:
			drawWidth = 48*3;
			drawHeight = 48*3;
			xDrawOffset = 48;
			yDrawOffset = 48;
			name = "Ginger";
			importFromSpriteSheet("/npcs/pet/Cat4.png", 6, 1, 0, 0, 0, 48, 48, 0);
			importFromSpriteSheet("/npcs/pet/Cat4.png", 8, 1, 1, 0, 96, 48, 48, 0);
			break;
		}

	}    
	public void update(double dt) {
		followNPC(dt, owner);
	       animationSpeed+=animationUpdateSpeed*dt; //Update the animation frame
	        if(walking) {
	        	currentAnimation = 1;
	        } else {
	          	currentAnimation = 0;
	        }
	        if(animationSpeed >= animationSpeedFactor) {
	            animationSpeed = 0;
	            animationCounter++;
	        }
	        if(animations != null) {
	            if (animations[0][currentAnimation][animationCounter] == null) { //If the next frame is empty
	                animationCounter = 0; //Loops the animation
	            }
	        }
    }
	   
	public void draw(Graphics2D g2, int xDiff, int yDiff) {
        if(animations != null) {
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
		    	  if(direction.equals("Left")){
		          	img = createHorizontalFlipped(img);
		          }
	    	  }
	    	  g2.drawImage(img, (int)(hitbox.x - xDrawOffset - xDiff), (int) (hitbox.y - yDrawOffset - yDiff), (int)(drawWidth), (int)(drawHeight), null);
        }
	      
        //g2.setColor(new Color(255, 0, 0, 50)); // red transparent
        //g2.fillOval((int)((hitbox.x + hitbox.width / 2f) - ALERT_DISTANCE - xDiff),(int)((hitbox.y + hitbox.height / 2f) - ALERT_DISTANCE - yDiff),ALERT_DISTANCE * 2,ALERT_DISTANCE * 2);
	  }
}
