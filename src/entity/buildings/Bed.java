package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.GamePanel;
import utility.DayPhase;

public class Bed extends Building{
	
	private Rectangle2D.Float interactHitbox;
	
	private int cooldown = 0;
	
	public Bed(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48*2, 48*2);
		
		isSolid = true;
		blueprint = false;
		drawWidth = 32*3;
		drawHeight = 48*3;
		yDrawOffset = 24;
		importImages();
		interactHitbox = new Rectangle2D.Float(hitbox.x+56, hitbox.y+48, 32, hitbox.height);
		isDecor = true;
		mustBePlacedOnFloor = true;
	}
	public Building clone() {
		Bed building = new Bed(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Bed(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new BufferedImage[1][1][1];
		
		name = "Bed";
    	animations[0][0][0] = importImage("/decor/bed.png").getSubimage(160, 96, 32, 48);
    	normalImage = importImage("/decor/bedNormal.png").getSubimage(160, 96, 32, 48);
	}
	public void draw(Graphics2D g2) {
		
		if(cooldown > 0) {
			cooldown--;
		}
		 		
		if(normalImage != null) {
			 if(litImage == null) {
				 litImage = gp.lightingM.getLitImage(animations[0][0][0], normalImage, (int)hitbox.x, (int)hitbox.y);
			 }
		     g2.drawImage(litImage, (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		 } else {
		     g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		 }
		
	    if(hitbox.intersects(gp.player.interactHitbox)) {
	    	if(gp.world.getCurrentPhase() == DayPhase.AFTER_HOURS) {
	    		if(gp.keyI.ePressed && cooldown == 0) {
	    			gp.world.sleep();
	    			cooldown = 60;
	    		}
	    	}
	    }
	    
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
	    
	}
	
	
}