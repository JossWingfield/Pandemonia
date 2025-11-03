package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class Rubble extends Building {
	
	private int spillCount = 0;
	private int maxSpillTime = 60*1;
	private Rectangle2D.Float effectArea;
	private boolean removedSpill = false;
	
	private int preset;
	private boolean barricade = false;
	
	public Rubble(GamePanel gp, float xPos, float yPos, int preset) {
		super(gp, xPos, yPos, 48, 48);
		this.preset = preset;
		
		isSolid = true;
		isBottomLayer = true;
		blueprint = false;
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
		animations = new BufferedImage[1][1][2];
		
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
	public void draw(Graphics2D g2, int xDiff, int yDiff) {
	    
	    if(hitbox.intersects(gp.player.interactHitbox)) {
		    g2.drawImage(animations[0][0][1], (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		    if(gp.keyI.ePressed && !barricade) {
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
		    //gp.player.slowSpeed();
	    } else {
	    	//gp.player.setNormalSpeed();
	    }
	    if(removedSpill) {
	    	//gp.player.setNormalSpeed();
	    }
	    
	    if(spillCount > 0) {
	    	drawChoppingBar(g2, hitbox.x+24, hitbox.y+24, spillCount, maxSpillTime, xDiff, yDiff);
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
