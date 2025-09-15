package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class EscapeHole extends Building {
	
	public Rectangle2D.Float npcHitbox;
	private boolean firstUpdate = true;
	
	public EscapeHole(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		
		isSolid = false;
		blueprint = false;
		drawWidth = 16*3;
		drawHeight = 16*3;
		importImages();
		npcHitbox = new Rectangle2D.Float(hitbox.x + 24, hitbox.y+hitbox.height - 48, 48, 80);
	}
	public Building clone() {
		EscapeHole building = new EscapeHole(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new EscapeHole(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new BufferedImage[1][1][1];
		
		name = "Escape Hole";
    	animations[0][0][0] = importImage("/decor/EscapeHole.png");

	}
	public void draw(Graphics2D g2) {
		
        //g2.setColor(Color.YELLOW);
      	//g2.drawRect((int)npcHitbox.x, (int)npcHitbox.y, (int)npcHitbox.width, (int)npcHitbox.height);
      	
		//g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
	        
	}
}
