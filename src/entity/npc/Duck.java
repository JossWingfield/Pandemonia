package entity.npc;

import java.util.Random;

import org.lwjgl.glfw.GLFW;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.TextureRegion;

public class Duck extends NPC {
	
	Random r;
	private boolean leaving = false;
	private int timesPetted = 0;

	public Duck(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		animationSpeedFactor = 0.06;
		drawScale = 3;
		drawWidth = 32*drawScale;
	    drawHeight = 16*drawScale;
	    xDrawOffset = 24;
		speed = 4*60;
		npcType = "Duck";
		r = new Random();
		gp.world.animalPresent = true;
	
		
		importImages();
	}
	private void importImages() {
		animations = new TextureRegion[5][10][10];
		
		name = "Duck";
		
		importFromSpriteSheet("/npcs/duck/Duck.png", 2, 1, 0, 0, 0, 32, 16, 0);
		importFromSpriteSheet("/npcs/duck/Duck.png", 5, 1, 1, 0, 16, 32, 16, 0);
	}    
	public void update(double dt) {
		if(!leaving) {
			fleeFromPlayer(dt);
			if(hitbox.intersects(gp.player.hitbox)) {
				if(gp.keyL.isKeyPressed(GLFW.GLFW_KEY_E)) {
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
		animationSpeed+=animationUpdateSpeed*dt; //Update the animation frame
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
        }
    }
	protected void leave(double dt) {
		super.leave(dt);
		gp.gui.addMessage("The duck is leaving now", Colour.GREEN);
    }
	public void draw(Renderer renderer) {
        if(animations != null) {
            TextureRegion img = animations[0][currentAnimation][animationCounter];
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
	    	  renderer.draw(img, (int)(hitbox.x - xDrawOffset ), (int) (hitbox.y - yDrawOffset ), (int)(drawWidth), (int)(drawHeight));
        }
	      
        //g2.setColor(new Color(255, 0, 0, 50)); // red transparent
        //g2.fillOval((int)((hitbox.x + hitbox.width / 2f) - ALERT_DISTANCE - gp.player.xDiff),(int)((hitbox.y + hitbox.height / 2f) - ALERT_DISTANCE - gp.player.yDiff),ALERT_DISTANCE * 2,ALERT_DISTANCE * 2);
	  }
}
