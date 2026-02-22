package entity.buildings;

import java.awt.geom.Rectangle2D;

import entity.PlayerMP;
import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.TextureRegion;

public class Stairs extends Building {
	
	public Stairs(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);

		importImages();

		isSolid = true;
		hitbox.width = 48*3;
		drawWidth = 48*3;
		drawHeight = 48*3;
		hitbox.width = 48*3;
		mustBePlacedOnFloor = true;
	 	canBuildingBePlacedOn = true;
		canBePlaced = false;
		castsShadow = false;
		isBottomLayer = true;
		
		hitbox.width = 12;
		hitbox.height = 24*3;
		yDrawOffset = 24 + 4*3;
	}
	public void onPlaced() {
		buildHitbox = hitbox;
	}
	public Building clone() {
		Stairs building = new Stairs(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Stairs(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new TextureRegion[1][1][2];
		
		name = "Stairs";
    	animations[0][0][0] = importImage("/tiles/beams/Multilevel.png").getSubimage(128, 0, 48, 48);
	}
	public void refreshImages() {
       	//animations[0][0][0] = gp.world.mapM.getRooms()[roomNum].getCounterSkin().getTableImage(64, 64, 32, 32);
       	//animations[0][0][1] = gp.world.mapM.getRooms()[roomNum].getCounterSkin().getTableImage(96, 64, 32, 32);
	}
	public void draw(Renderer renderer) {

		renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
       		 
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		}
	        
	}
}
