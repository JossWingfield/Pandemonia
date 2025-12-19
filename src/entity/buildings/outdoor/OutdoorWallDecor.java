package entity.buildings.outdoor;

import java.awt.geom.Rectangle2D;

import entity.buildings.Building;
import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;
import utility.CollisionMethods;

public class OutdoorWallDecor  extends Building {

	private int type;
	private boolean firstUpdate = true;
	private Rectangle2D.Float invisHitbox;
	
	public OutdoorWallDecor(GamePanel gp, float xPos, float yPos, int type) {
		super(gp, xPos, yPos, 48, 48);
		this.type = type;
		
		isSolid = false;
		
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
		animations = new TextureRegion[1][1][1];
		
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
	public void draw(Renderer renderer) {
		if(firstUpdate) {
			firstUpdate = false;
		} 
		
		if(invisHitbox == null) {
		     renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		} else {
			if(gp.player.hitbox.intersects(invisHitbox)) {
				TextureRegion img = animations[0][0][0];
				//img = CollisionMethods.reduceImageAlpha(img, 0.25f);
				renderer.draw(img, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
			} else {
			    renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
			}
		}
	     
		 if(destructionUIOpen) {
		     renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		 }
	        
	}
}
