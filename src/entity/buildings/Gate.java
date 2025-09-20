package entity.buildings;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entity.PlayerMP;
import main.GamePanel;

public class Gate extends Building {
	
	private Rectangle2D.Float hitbox2;
	private boolean firstUpdate = true;
	private boolean imageChange = false;
	private int previousI = 0;
	
	public Gate(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		
		isSolid = true;
		blueprint = false;
		drawWidth = 32*3;
		drawHeight = 32*3;
		hitbox.height = 38;
		importImages();
		xDrawOffset = 0;
		yDrawOffset = 30;
		hitbox.width = 30;
		isKitchenBuilding = true;
		mustBePlacedOnFloor = true;
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
		animations = new BufferedImage[1][1][2];
		
		name = "Gate 1";
    	animations[0][0][0] = importImage("/decor/connected table 2.png").getSubimage(64, 64, 32, 32);
    	animations[0][0][1] = importImage("/decor/connected table 2.png").getSubimage(96, 64, 32, 32);
	}
	public void draw(Graphics2D g2) {
		
		if(firstUpdate) {
			hitbox2 = new Rectangle2D.Float(hitbox.x + hitbox.width, hitbox.y, 48, 38);
			firstUpdate = false;
		}
		
		if(previousI == 1) {
			imageChange = true;
		}
		int i = 0;
		previousI = 0;
		if(gp.multiplayer) {
			for(PlayerMP player: gp.playerList) {
				if(player.currentRoomIndex == 0) {
					if(hitbox2.intersects(player.hitbox) || !gp.npcM.entityCheck(hitbox2)) {
						i = 1;
						imageChange = true;
						previousI = 1;
					}
				}
			}
		} else {
			if(hitbox2.intersects(gp.player.hitbox) || !gp.npcM.entityCheck(hitbox2)) {
				i = 1;
				imageChange = true;
				previousI = 1;
			}
		}

		g2.drawImage(animations[0][0][i], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
       		 
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
	        
	}
}
