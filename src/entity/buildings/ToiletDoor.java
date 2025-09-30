package entity.buildings;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class ToiletDoor extends Building {
	
	private Rectangle2D.Float doorHitbox, npcVisualHitbox;
	private boolean firstUpdate = true;
	public int preset = 0;
	public int cooldown = 0;
	
	public ToiletDoor(GamePanel gp, float xPos, float yPos, int presetNum) {
		super(gp, xPos, yPos, 48*2, 48*2);
		this.preset = presetNum;
		
		isSolid = false;
		blueprint = false;
		drawWidth = 32*3;
		drawHeight = 48*3;
		yDrawOffset = 24+8;
		isThirdLayer = true;
		canBePlaced = false;
		importImages();
	}
	public Building clone() {
		ToiletDoor building = new ToiletDoor(gp, hitbox.x, hitbox.y, preset);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new ToiletDoor(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ", " + preset + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new BufferedImage[1][1][2];
		
		name = "Toilet Door 1";
		switch(preset) {
		case 0:
			animations[0][0][0] = importImage("/decor/door.png").getSubimage(96, 48, 32, 48);
        	animations[0][0][1] = importImage("/decor/OpenDoor.png");
	    	break;
		}
	}
	public void draw(Graphics2D g2) {
		if(firstUpdate) {
			firstUpdate = false;
			doorHitbox = new Rectangle2D.Float(hitbox.x + 24, hitbox.y+hitbox.height - 48+28, 48, 64);
			npcVisualHitbox = new Rectangle2D.Float(hitbox.x + 24, hitbox.y+hitbox.height - 48+28, 48, 80);
			importImages();
		}  
		
		if(cooldown > 0) {
			cooldown--;
		}
		
		if(!gp.player.interactHitbox.intersects(doorHitbox) && gp.npcM.entityCheck(npcVisualHitbox)) {
			g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		} else {
			g2.drawImage(animations[0][0][1], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		}
		
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
	        
	}
}
