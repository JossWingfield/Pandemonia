package entity.buildings;

import java.awt.geom.Rectangle2D;

import entity.PlayerMP;
import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.TextureRegion;

public class Gate extends Building {
	
	private Rectangle2D.Float hitbox2;
	private boolean firstUpdate = true;
	
	public Gate(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		
		isSolid = true;
		
		drawWidth = 32*3;
		drawHeight = 32*3;
		hitbox.height = 38;
		importImages();
		xDrawOffset = 0;
		yDrawOffset = 30;
		hitbox.width = 30;
		isKitchenBuilding = true;
		mustBePlacedOnFloor = true;
		canBePlaced = false;
	}
	public void onPlaced() {
		buildHitbox = hitbox;
	}
	public Building clone() {
		Gate building = new Gate(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Gate(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new TextureRegion[1][1][2];
		
		name = "Gate 1";
    	animations[0][0][0] = importImage("/decor/connected table 2.png").getSubimage(64, 64, 32, 32);
    	animations[0][0][1] = importImage("/decor/connected table 2.png").getSubimage(96, 64, 32, 32);
	}
	public void refreshImages() {
       	animations[0][0][0] = gp.mapM.getRooms()[roomNum].getTableSkin().getTableImage(64, 64, 32, 32);
       	animations[0][0][1] = gp.mapM.getRooms()[roomNum].getTableSkin().getTableImage(96, 64, 32, 32);
	}
	public void draw(Renderer renderer) {
		
		if(firstUpdate) {
			hitbox2 = new Rectangle2D.Float(hitbox.x + hitbox.width, hitbox.y, 48, 38);
			firstUpdate = false;
		}

		int i = 0;
		if(gp.multiplayer) {
			for(PlayerMP player: gp.playerList) {
				if(player.currentRoomIndex == 0) {
					if(hitbox2.intersects(player.hitbox) || !gp.npcM.entityCheck(hitbox2)) {
						i = 1;
					}
				}
			}
		} else {
			if(hitbox2.intersects(gp.player.hitbox) || !gp.npcM.entityCheck(hitbox2)) {
				i = 1;
			}
		}

		renderer.draw(animations[0][0][i], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
       		 
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		}
	        
	}
}
