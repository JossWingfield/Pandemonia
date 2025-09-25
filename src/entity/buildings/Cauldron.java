package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entity.PlayerMP;
import entity.items.Item;
import main.GamePanel;
import map.LightSource;
import utility.RecipeManager;

public class Cauldron extends Building {
	
	private LightSource light;
	private boolean isActive = false;
	private int activeCounter = 0;
	private int activeTime = 120;
	
	public Cauldron(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		
		isSolid = true;
		blueprint = false;
		drawWidth = 32*3;
		drawHeight = 32*3;
		importImages();
		xDrawOffset = 24;
		yDrawOffset = 24;
		mustBePlacedOnFloor = true;
	}
	public Building clone() {
		Cauldron building = new Cauldron(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Cauldron(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new BufferedImage[1][1][2];
		
		name = "Cauldron";
    	animations[0][0][0] = importImage("/decor/Cauldron.png").getSubimage(0, 0, 32, 32);
    	animations[0][0][1] = importImage("/decor/Cauldron.png").getSubimage(32, 0, 32, 32);
	}
	public void draw(Graphics2D g2) {

		if(!isActive) {
			if(gp.player.interactHitbox.intersects(hitbox)) {
				if(gp.keyI.ePressed) {
					isActive = true;
					activeCounter = 0;
					light = new LightSource((int)(hitbox.x+ hitbox.width/2), (int)(hitbox.y + hitbox.height/2), Color.GREEN, 48*4);
					light.setIntensity(0.5f);
					gp.lightingM.addLight(light);
				}
			}
		}
		
		if(isActive) {
			activeCounter++;
			if(activeCounter >= activeTime) {
				activeCounter = 0;
				isActive = false;
				Item item = gp.itemRegistry.getItemFromName(RecipeManager.getCurrentHauntedIngredient(), 0);
				gp.player.currentItem = item;
				gp.lightingM.removeLight(light);
			}
			g2.drawImage(animations[0][0][1], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		} else {
			g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		}
		
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
	        
	}
}
