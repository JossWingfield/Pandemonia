package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.GamePanel;
import map.LightSource;
import utility.DayPhase;

public class Lantern extends Building{
	
	private LightSource light;
	private boolean firstUpdate = true;
	private boolean turnedOn = true;
	
	public Lantern(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48*2);
		
		description = "Light up your restaurant.";
		cost = 20;
		
		drawWidth = 48;
		isDecor = true;
        drawHeight = 96;
        hitbox.height = 80;
		isSolid = false;
		blueprint = false;
		importImages();
		mustBePlacedOnWall = true;
	}
	public Building clone() {
		Lantern building = new Lantern(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Lantern(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new BufferedImage[1][1][2];
		
		name = "Lantern";
    	animations[0][0][0] = importImage("/decor/Lantern.png").getSubimage(0, 0, 16, 32);
    	animations[0][0][1] = importImage("/decor/Lantern.png").getSubimage(16, 0, 16, 32);
	}
	public void turnOff() {
		turnedOn = false;
		gp.lightingM.removeLight(light);
	}
	public void turnOn() {
		turnedOn = true;
		gp.lightingM.addLight(light);
	}
	public void destroy() {
		gp.lightingM.removeLight(light);
	}
	public void draw(Graphics2D g2, int xDiff, int yDiff) {
		
		if(firstUpdate) {
			firstUpdate = false;
			light = new LightSource((int)(hitbox.x+ hitbox.width/2), (int)(hitbox.y + hitbox.height/2), Color.ORANGE, 240);
			light.setIntensity(0.4f);
			if(turnedOn) {
				gp.lightingM.addLight(light);
			}
		}
		
		int i = 0;
		if(!turnedOn) {
			i = 1;
		}

	     g2.drawImage(animations[0][0][i], (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
	    
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
	    
	}
	
	
}
