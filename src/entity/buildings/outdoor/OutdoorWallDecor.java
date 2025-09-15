package entity.buildings.outdoor;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entity.buildings.Building;
import entity.buildings.WallDecor_Building;
import main.GamePanel;
import utility.CollisionMethods;

public class OutdoorWallDecor  extends Building {

	private int type;
	private boolean firstUpdate = true;
	private Rectangle2D.Float invisHitbox;
	
	public OutdoorWallDecor(GamePanel gp, float xPos, float yPos, int type) {
		super(gp, xPos, yPos, 48, 48);
		this.type = type;
		
		isSolid = false;
		blueprint = false;
		drawWidth = 16*3;
		drawHeight = 16*3;
		importImages();
	}
	public Building clone() {
		OutdoorWallDecor building = new OutdoorWallDecor(gp, hitbox.x, hitbox.y, type);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new OutdoorWallDecor(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ", " + type + ");");
		System.out.println("arrayCounter++;");
	}
	
	private void importImages() {
		animations = new BufferedImage[1][1][1];
		
		switch(type) {
		 case 0:
			 name = "Window 1";
	         animations[0][0][0] = importImage("/buildings/House.png").getSubimage(64, 192, 16, 16);
	         break;
		 case 1:
			 name = "Cover";
	         animations[0][0][0] = importImage("/buildings/House.png").getSubimage(0, 224, 32, 16);
	         drawWidth = 32*3;
	         isThirdLayer = true;
	         break;
		 case 2:
			 name = "House Trim 1";
	         animations[0][0][0] = importImage("/buildings/House.png").getSubimage(224, 0, 48, 16);
	         drawWidth = 48*3;
	         break;
		 case 3:
			 name = "House Trim 2";
	         animations[0][0][0] = importImage("/buildings/House.png").getSubimage(224, 16, 48, 16);
	         drawWidth = 48*3;
	         break;
		 case 4:
			 name = "House Trim 3";
	         animations[0][0][0] = importImage("/buildings/House.png").getSubimage(224, 32, 48, 16);
	         drawWidth = 48*3;
	         break;
		 case 5:
			 name = "Side Chimney";
	         animations[0][0][0] = importImage("/buildings/House1.png").getSubimage(272, 16, 16, 32);
	         drawHeight = 32*3;
	         isThirdLayer = true;
	         break;
		 case 6:
			 name = "Window 2";
	         animations[0][0][0] = importImage("/buildings/House1.png").getSubimage(336, 48, 16, 16);
	         break;
        }
		
	}
	public void draw(Graphics2D g2) {
		if(firstUpdate) {
			firstUpdate = false;
		} 
		
		if(invisHitbox == null) {
		     g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		} else {
			if(gp.player.hitbox.intersects(invisHitbox)) {
				BufferedImage img = animations[0][0][0];
				img = CollisionMethods.reduceImageAlpha(img, 0.25f);
				g2.drawImage(img, (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
			} else {
			    g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
			}
		}
	     
		 if(destructionUIOpen) {
		     g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		 }
	        
	}
}
