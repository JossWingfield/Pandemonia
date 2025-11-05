package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.GamePanel;
import map.LightSource;
import utility.DayPhase;

public class SoulLantern extends Building{
	
	private LightSource light;
	private boolean firstUpdate = true;
	private Color soulColour;
	
	public SoulLantern(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48*2);
		
		drawWidth = 48;
		isDecor = true;
        drawHeight = 96;
        hitbox.height = 80;
		isSolid = false;
		blueprint = false;
		importImages();
		mustBePlacedOnWall = true;
		canBePlaced = false;
	}
	public Building clone() {
		SoulLantern building = new SoulLantern(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new SoulLantern(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new BufferedImage[1][1][1];
		
		soulColour = new Color(100, 166, 147);
		
		name = "Soul Lantern";
    	animations[0][0][0] = importImage("/decor/SoulLantern.png");
	}
	public void draw(Graphics2D g2, int xDiff, int yDiff) {
		
		if(firstUpdate) {
			firstUpdate = false;
			light = new LightSource((int)(hitbox.x+ hitbox.width/2), (int)(hitbox.y + hitbox.height/2), soulColour, 64);
			light.setIntensity(0.8f);
			gp.lightingM.addLight(light);
		}
		

	     g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
	    
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
	    
	}
	
	
}
