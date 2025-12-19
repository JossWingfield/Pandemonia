package entity.buildings;

import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.TextureRegion;

public class RoomSpawn extends Building {
	
	public RoomSpawn(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		
		isSolid = false;
		
		drawWidth = 16*3;
		drawHeight = 16*3;
		importImages();
		canBePlaced = false;
	}
	public Building clone() {
		RoomSpawn building = new RoomSpawn(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new RoomSpawn(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new TextureRegion[1][1][1];
		
		name = "Room Spawn";
    	animations[0][0][0] = importImage("/decor/EscapeHole.png").toTextureRegion();

	}
	public void draw(Renderer renderer) {
		
		if(gp.currentState == gp.mapBuildState) {
			renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
			if(destructionUIOpen) {
			    renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
			}
		}

	        
	}
}
