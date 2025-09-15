package entity.buildings;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class Table extends Building {
	
	public Table(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48*2);
		
		isSolid = true;
		blueprint = false;
		drawWidth = 16*3;
		drawHeight = 40*3;
		//xDrawOffset = 16;
		importImages();
		isSolid = true;
		isSecondLayer = true;
		//isBottomLayer = true;
		isKitchenBuilding = true;
		mustBePlacedOnFloor = true;
	}
	public Building clone() {
		Table building = new Table(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Table(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new BufferedImage[1][1][1];
		
		name = "Table 1";
    	animations[0][0][0] = importImage("/decor/table.png").getSubimage(48, 24, 16, 40);
	}
	public void draw(Graphics2D g2) {
		 
	    g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		 
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
	        
	}
}
