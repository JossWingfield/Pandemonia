package entity.buildings;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class Table2 extends Building {
	
	public Table2(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48*2, 48);
		
		isSolid = true;
		blueprint = false;
		drawWidth = 32*3;
		drawHeight = 24*3;
		//xDrawOffset = 16;
		importImages();
		isSolid = true;
		isSecondLayer = true;
		//isBottomLayer = true;
		isKitchenBuilding = true;
		mustBePlacedOnFloor = true;
	}
	public Building clone() {
		Table2 building = new Table2(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Table2(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new BufferedImage[1][1][1];
		
		name = "Table 2";
    	animations[0][0][0] = importImage("/decor/table.png").getSubimage(32, 0, 32, 24);
	}
}
