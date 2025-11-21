package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entity.npc.Customer;
import main.GamePanel;
import map.LightSource;

public class Breaker extends Building{
	
	private LightSource light;
	private Rectangle2D.Float interactHitbox;
	private boolean powerOn = true;
	private boolean firstUpdate = true;
	
	public Breaker(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48*2, 48*2);
		
		isSolid = true;
		blueprint = false;
		drawWidth = 32*3;
		drawHeight = 48*3;
		yDrawOffset = 24;
		importImages();
		isSolid = true;
		canBePlaced = false;
		interactHitbox = new Rectangle2D.Float(hitbox.x+56, hitbox.y+48, 32, hitbox.height);
		npcHitbox = interactHitbox;
		buildHitbox = new Rectangle2D.Float(hitbox.x + 3*2, hitbox.y+3*4, hitbox.width-3*4, hitbox.height-3*5);

	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x + 3*2, hitbox.y+3*4, hitbox.width-3*4, hitbox.height-3*5);
	}
	public Building clone() {
		Breaker building = new Breaker(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Breaker(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new BufferedImage[1][1][4];
		
		name = "Breaker";
    	animations[0][0][0] = importImage("/decor/Breaker.png").getSubimage(0, 0, 32, 48);
    	animations[0][0][1] = importImage("/decor/Breaker.png").getSubimage(32, 0, 32, 48);
    	animations[0][0][2] = importImage("/decor/Breaker.png").getSubimage(64, 0, 32, 48);
     	animations[0][0][3] = importImage("/decor/Breaker.png").getSubimage(96, 0, 32, 48);
     	     	
    }
	public void cutPower() {
		powerOn = false;
		gp.lightingM.updateLightColor(3, light, Color.RED);
		gp.lightingM.addLight(gp.player.playerLight);
	}
	public boolean isPowerOff() {
		return !powerOn;
	}
	public void update(double dt) {
		super.update(dt);
		if(firstUpdate) {
			firstUpdate = false;
			light = new LightSource((int)(hitbox.x+ hitbox.width/2), (int)(hitbox.y + hitbox.height/2), Color.GREEN, 100);
			gp.mapM.getRoom(3).addLight(light);
		}
	}
	public void draw(Graphics2D g2, int xDiff, int yDiff) {
		if(powerOn) {
		    if(gp.player.interactHitbox.intersects(interactHitbox)) {
			    g2.drawImage(animations[0][0][2], (int)(hitbox.x - xDrawOffset - xDiff), (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		    } else {
			    g2.drawImage(animations[0][0][0], (int)(hitbox.x - xDrawOffset - xDiff), (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		    }
		} else {
			gp.lightingM.updateLightColor(3, light, Color.RED);
			if(gp.player.interactHitbox.intersects(interactHitbox)) {
			    g2.drawImage(animations[0][0][3], (int)(hitbox.x - xDrawOffset - xDiff), (int)(hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
			    if(gp.keyI.ePressed) {
			    	powerOn = true;
			    	gp.gui.addMessage("Power Back Online", Color.GREEN);
			    	gp.lightingM.removeLight(gp.player.playerLight);
			    	gp.lightingM.setPowerOn();
					gp.lightingM.updateLightColor(3, light, Color.GREEN);
					if(!gp.cutsceneM.cutscenePlayed.contains("Ignis I")) {
						gp.cutsceneM.cutsceneQueued = true;
						gp.cutsceneM.cutsceneName = "Ignis I";
					}
			    }
			} else {
			    g2.drawImage(animations[0][0][1], (int)(hitbox.x - xDrawOffset - xDiff), (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		    }
		}
	    
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int)(hitbox.x - xDrawOffset - xDiff), (int) (hitbox.y - yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
	    
	}
	
	
}
