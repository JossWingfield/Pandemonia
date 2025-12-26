package entity.buildings;

import java.awt.geom.Rectangle2D;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Table extends Building {
	
	public Table(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48*2);
		
		isSolid = true;
		
		drawWidth = 16*3;
		drawHeight = 40*3;
		//xDrawOffset = 16;
		importImages();
		isSolid = true;
		isSecondLayer = true;
		//isBottomLayer = true;
		isKitchenBuilding = true;
		mustBePlacedOnFloor = true;
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*3, hitbox.width-3*3, hitbox.height-3*4);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*3, hitbox.width-3*3, hitbox.height-3*4);
	}
	public Building clone() {
		Table building = new Table(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Table(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	public void refreshImages() {
		animations[0][0][0] = gp.mapM.getRooms()[roomNum].getTableSkin().getImage1();
	}
	private void importImages() {
		animations = new TextureRegion[1][1][1];
		
		name = "Table 1";
    	animations[0][0][0] = importImage("/decor/table.png").getSubimage(48, 24, 16, 40);

	}
}
