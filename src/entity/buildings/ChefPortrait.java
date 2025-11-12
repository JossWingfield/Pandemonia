package entity.buildings;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class ChefPortrait extends Building{
	
	
	public ChefPortrait(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48*2, 48*2);
		
		isSolid = true;
		blueprint = false;
		drawWidth = 32*3;
		drawHeight = 32*3;
		importImages();
		canBePlaced = false;
		isDecor = true;
		mustBePlacedOnWall = true;
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*3, hitbox.height-3*3);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*3, hitbox.height-3*3);
	}
	public Building clone() {
		ChefPortrait building = new ChefPortrait(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new ChefPortrait(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new BufferedImage[1][1][1];
		
		name = "Chef Portrait";
    	animations[0][0][0] = importImage("/decor/EmptyPainting.png");
	}
	public void draw(Graphics2D g2, int xDiff, int yDiff) {
	     g2.drawImage(animations[0][0][0], (int)(hitbox.x - xDrawOffset - xDiff), (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);

	    
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int)(hitbox.x - xDrawOffset - xDiff), (int) (hitbox.y - yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
	    
	}
	
}
