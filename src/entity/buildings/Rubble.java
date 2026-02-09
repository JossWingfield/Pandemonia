package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.lwjgl.glfw.GLFW;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.TextureRegion;

public class Rubble extends Building {
	
	private double spillCount = 0;
	private int maxSpillTime = 8;
	private Rectangle2D.Float effectArea;
	private boolean removedSpill = false;
	
	public int preset;
	public boolean barricade = false;
	private int fadeAlpha = 255;
	
	public Rubble(GamePanel gp, float xPos, float yPos, int preset) {
		super(gp, xPos, yPos, 48, 48);
		this.preset = preset;
		
		animationSpeedFactor = 6;
		isSolid = true;
		isBottomLayer = true;
		
		drawWidth = 16*3;
		drawHeight = 16*3;
		importImages();
		canBePlaced = false;
		effectArea = new Rectangle2D.Float(hitbox.x - 32, hitbox.y - 32, hitbox.width + 64, hitbox.height + 64);
	}
	public Building clone() {
		Spill building = new Spill(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Rubble(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + "," + preset  +");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new TextureRegion[1][1][2];
		
		name = "Rubble";
		
		switch(preset) {
		case 0:
	       	animations[0][0][0] = importImage("/decor/firewood.png").getSubimage(48, 0, 16, 16);
			break;
		case 1:
        	animations[0][0][0] = importImage("/decor/general store props.png").getSubimage(96, 256-32, 32, 32);
        	isSolid = true;
          	drawWidth = 48*2;
        	drawHeight = 48*2;
        	hitbox.width = 32*3;
          	hitbox.height = 16*3;
           	yDrawOffset = 48;
			break;
		case 2:
			animations[0][0][0] = importImage("/decor/general store props.png").getSubimage(64, 256-32, 32, 32);
        	isSolid = true;
          	drawWidth = 48*2;
        	drawHeight = 48*2;
        	hitbox.width = 32*3;
          	hitbox.height = 16*3;
           	yDrawOffset = 48;
			break;
		case 3:
			animations[0][0][0] = importImage("/decor/general store props.png").getSubimage(96, 192, 32, 32);
        	isSolid = true;
          	drawWidth = 48*2;
        	drawHeight = 48*2;
        	hitbox.width = 32*3;
          	hitbox.height = 16*3;
           	yDrawOffset = 24;
  			break;
		}
		
    	animations[0][0][1] = animations[0][0][0];
	}
	public void setBarricade() {
		name = "Barricade";
		barricade = true;
	}
	public void explode() {
		if(name.equals("Barricade")) {
			currentAnimation = 1;
		    fadeAlpha = 255;
		}
	}
	public void inputUpdate(double dt) {
		super.inputUpdate(dt);
		if(hitbox.intersects(gp.player.interactHitbox)) {
		    if(gp.keyL.isKeyPressed(GLFW.GLFW_KEY_E)) {
		    	spillCount+=dt;
		    	if(spillCount >= maxSpillTime) {
		    		spillCount = 0;
		    		gp.player.setNormalSpeed();
		    		removedSpill = true;
		    		gp.world.buildingM.removeBuilding(this);
		    	}
		    }
		}
	}
	public void draw(Renderer renderer) {
	    
	    if(hitbox.intersects(gp.player.interactHitbox)) {
		    renderer.draw(animations[0][0][1], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
	    } else {
	    	if(currentAnimation == 1) {
	    		// Fade-out effect for barricade rubble
	    	    if (fadeAlpha > 0) {
	    	        fadeAlpha -= 5; // fade speed (smaller = slower fade)
	    	        if (fadeAlpha < 0) fadeAlpha = 0;
	    	    }

	    	    // Draw faded image
	    	    renderer.draw(animations[0][0][0], 
	    	        (int) hitbox.x - xDrawOffset , 
	    	        (int) (hitbox.y ) - yDrawOffset, 
	    	        drawWidth, drawHeight 
	    	        );

	    	    // Remove when fully faded
	    	    if (fadeAlpha <= 0) {
	    	        gp.world.buildingM.removeBuilding(this);
	    	        return;
	    	    }
	    	}
		    renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
	    }
	    if(spillCount > 0) {
	    	drawChoppingBar(renderer, hitbox.x+24, hitbox.y+24, (int)spillCount, (int)maxSpillTime);
	    }
	    
	}
	private void drawChoppingBar(Renderer renderer, float worldX, float worldY, int cookTime, int maxCookTime) {
	    float screenX = worldX - xDrawOffset ;
	    float screenY = worldY - yDrawOffset ;

	    int barWidth = 48;
	    int barHeight = 6;
	    int xOffset = 0;
	    int yOffset = -10; // raise the bar above the pan

	    float progress = Math.min(1.0f, cookTime / (float) maxCookTime);

	    // Interpolate from red to green
	    int r = (int) ((1 - progress) * 255);
	    int g = (int) (progress * 255);

	    // Optional: draw a border
	    renderer.fillRect((int) screenX + xOffset, (int) screenY + yOffset, barWidth, barHeight, Colour.BLACK);
	    
	    renderer.fillRect((int) screenX + xOffset, (int) screenY + yOffset, (int) (barWidth * progress), barHeight, new Colour(r, g, 0));

	}
	
}
