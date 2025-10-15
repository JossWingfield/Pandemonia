package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class Leak extends Building {
	
	private int spillCount = 0;
	private int maxSpillTime = 60*7;
	private Rectangle2D.Float effectArea;
	private boolean removedSpill = false;
	
	public Leak(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48*2, 48*2);
		
		isSolid = false;
		isBottomLayer = true;
		blueprint = false;
		drawWidth = 64*3;
		drawHeight = 64*3;
		xDrawOffset = 48;
		yDrawOffset = 48;
		importImages();
		canBePlaced = false;
		effectArea = new Rectangle2D.Float(hitbox.x - 32, hitbox.y - 32, hitbox.width + 64, hitbox.height + 64);
	}
	public Building clone() {
		Leak building = new Leak(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Leak(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new BufferedImage[1][1][2];
		
		name = "Leak";
    	animations[0][0][0] = importImage("/decor/Leak.png").getSubimage(0, 0, 64, 64);

     	animations[0][0][1] = importImage("/decor/Leak.png").getSubimage(64, 0, 64, 64);
	}
	public void draw(Graphics2D g2, int xDiff, int yDiff) {
	    
	    if(hitbox.intersects(gp.player.interactHitbox)) {
		    g2.drawImage(animations[0][0][1], (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		    if(gp.keyI.ePressed) {
		    	spillCount++;
		    	if(spillCount >= maxSpillTime) {
		    		spillCount = 0;
		    		gp.player.setNormalSpeed();
		    		removedSpill = true;
		    		gp.buildingM.removeBuilding(this);
		    	}
		    }
	    } else {
		    g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
	    }
	    if(effectArea.intersects(gp.player.hitbox)) {
		    gp.player.slowSpeed();
	    } else {
	    	gp.player.setNormalSpeed();
	    }
	    if(removedSpill) {
	    	gp.player.setNormalSpeed();
	    }
	    
	    if(spillCount > 0) {
	    	drawChoppingBar(g2, hitbox.x+24 + 48, hitbox.y+24 + 48+32, spillCount, maxSpillTime, xDiff, yDiff);
	    }
	    
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
	    
	}
	private void drawChoppingBar(Graphics2D g2, float worldX, float worldY, int cookTime, int maxCookTime, int xDiff, int yDiff) {
	    float screenX = worldX - xDrawOffset - xDiff;
	    float screenY = worldY - yDrawOffset - yDiff;

	    int barWidth = 48;
	    int barHeight = 6;
	    int xOffset = 0;
	    int yOffset = -10; // raise the bar above the pan

	    float progress = Math.min(1.0f, cookTime / (float) maxCookTime);

	    // Interpolate from red to green
	    int r = (int) ((1 - progress) * 255);
	    int g = (int) (progress * 255);

	    // Optional: draw a border
	    g2.setColor(Color.BLACK);
	    g2.fillRect((int) screenX + xOffset, (int) screenY + yOffset, barWidth, barHeight);
	    
	    g2.setColor(new Color(r, g, 0));
	    g2.fillRect((int) screenX + xOffset, (int) screenY + yOffset, (int) (barWidth * progress), barHeight);

	}
	
}
