package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entity.buildings.Building;
import main.GamePanel;
import utility.CollisionMethods;

public class WallDecor_Building extends Building {

	private int type;
	private boolean firstUpdate = true;
	private Rectangle2D.Float invisHitbox;
	
	public WallDecor_Building(GamePanel gp, float xPos, float yPos, int type) {
		super(gp, xPos, yPos, 48, 48);
		this.type = type;
		
		isSolid = false;
		blueprint = false;
		drawWidth = 16*3;
		drawHeight = 16*3;
		importImages();
		mustBePlacedOnWall = true;
	}
	public Building clone() {
		WallDecor_Building building = new WallDecor_Building(gp, hitbox.x, hitbox.y, type);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new WallDecor_Building(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ", " + type + ");");
		System.out.println("arrayCounter++;");
	}
	
	private void importImages() {
		animations = new BufferedImage[1][1][1];
		
		switch(type) {
		 case 0:
			 name = "Painting 1";
	         animations[0][0][0] = importImage("/decor/painting.png").getSubimage(64, 0, 16, 16);
	         isDecor = true;
	         break;
		 case 1:
			 name = "Painting 2";
	         animations[0][0][0] = importImage("/decor/painting.png").getSubimage(80, 0, 16, 16);
	         isDecor = true;
	         break;
		 case 2:
			 name = "Painting 3";
	         animations[0][0][0] = importImage("/decor/painting.png").getSubimage(0, 0, 16, 16);
	         isDecor = true;
	         break;
		 case 3:
			 name = "Painting 4";
	         animations[0][0][0] = importImage("/decor/painting.png").getSubimage(16, 0, 16, 16);
	         isDecor = true;
	         break;
		 case 4:
			 name = "Hanging Plant 1";
	         animations[0][0][0] = importImage("/decor/plants.png").getSubimage(16, 96, 16, 32);
	         isDecor = true;
	         drawHeight = 96;
	         hitbox.height = 80;
	         break;
		 case 5:
			 name = "Hanging Plant 2";
	         animations[0][0][0] = importImage("/decor/plants.png").getSubimage(32, 96, 16, 32);
	         isDecor = true;
	         drawHeight = 96;
	         hitbox.height = 80;
	         break;
		 case 6:
			 name = "Shelf Piece";
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(0, 0, 16, 16);
	         isDecor = true;
	         isFourthLayer = true;
	         break;
		 case 7:
			 name = "Shelf Piece";
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(16, 0, 16, 16);
	         isDecor = true;
	         isFourthLayer = true;
	         break;
		 case 8:
			 name = "Shelf Piece";
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(32, 0, 16, 16);
	         isDecor = true;
	         isFourthLayer = true;
	         break;
		 case 9:
			 name = "Shelf Piece";
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(48, 0, 16, 16);
	         isDecor = true;
	         isFourthLayer = true;
	         break;
		 case 10:
			 name = "Shelf Piece";
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(64, 0, 16, 16);
	         isDecor = true;
	         isFourthLayer = true;
	         break;
		 case 11:
			 name = "Shelf Piece";
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(0, 16, 16, 16);
	         isDecor = true;
	         isFourthLayer = true;
	         break;
		 case 12:
			 name = "Shelf Piece";
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(0, 32, 16, 16);
	         isDecor = true;
	         isFourthLayer = true;
	         break;
		 case 13:
			 name = "Shelf Piece";
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(0, 48, 16, 16);
	         isDecor = true;
	         isFourthLayer = true;
	         break;
		 case 14:
			 name = "Shelf Piece";
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(32, 16, 16, 16);
	         isDecor = true;
	         isFourthLayer = true;
	         break;
		 case 15:
			 name = "Shelf Piece";
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(32, 32, 16, 16);
	         isDecor = true;
	         isFourthLayer = true;
	         break;
		 case 16:
			 name = "Shelf Piece";
	         animations[0][0][0] = importImage("/decor/wall shelf.png").getSubimage(32, 48, 16, 16);
	         isDecor = true;
	         isFourthLayer = true;
	         break;
		 case 17:
	         name = "Deer Head";
	         animations[0][0][0] = importImage("/decor/animal wall decor.png").getSubimage(96, 0, 32, 32);
	         isSolid = true;
	         isFourthLayer = true;
	         isDecor = true;
	 		 drawWidth = 32*3;
			 drawHeight = 32*3;
			 xDrawOffset = 24;
			 yDrawOffset = 24;
	         break;
		 case 18:
	         name = "Notice Board";
	         animations[0][0][0] = importImage("/decor/chalkboard.png").getSubimage(96, 0, 32, 32);
	         isSolid = false;
	         isThirdLayer = true;
	         isDecor = true;
	 		 drawWidth = 32*3;
			 drawHeight = 32*3;
			 xDrawOffset = 24;
			 yDrawOffset = 24;
	         break;
		 case 19:
	         name = "Piping 1";
	         animations[0][0][0] = importImage("/decor/Piping.png").getSubimage(0, 0, 32, 32);
	         isSolid = false;
	         isFourthLayer = true;
	         isDecor = true;
	 		 drawWidth = 32*3;
			 drawHeight = 32*3;
	         break;
		 case 20:
	         name = "Piping 2";
	         animations[0][0][0] = importImage("/decor/Piping.png").getSubimage(32, 0, 32, 32);
	         isSolid = false;
	         isFourthLayer = true;
	         isDecor = true;
	 		 drawWidth = 32*3;
			 drawHeight = 32*3;
	         break;
		 case 21:
	         name = "Piping 3";
	         animations[0][0][0] = importImage("/decor/Piping.png").getSubimage(64, 0, 32, 32);
	         isSolid = false;
	         isFourthLayer = true;
	         isDecor = true;
	 		 drawWidth = 32*3;
			 drawHeight = 32*3;
	         break;
		 case 22:
	         name = "Painting 5";
	         animations[0][0][0] = importImage("/decor/painting.png").getSubimage(0, 16, 32, 16);
	         isSolid = false;
	         isFourthLayer = true;
	         isDecor = true;
	 		 drawWidth = 32*3;
	 		 xDrawOffset = 12;
	         break;
		 case 23:
			 name = "Hanging Plant 3";
	         animations[0][0][0] = importImage("/decor/plants.png").getSubimage(48, 96, 16, 32);
	         drawHeight = 96;
	         isDecor = true;
	         hitbox.height = 80;
	         break;
		 case 24:
			 name = "Hanging Plant 4";
	         animations[0][0][0] = importImage("/decor/plants.png").getSubimage(64, 96, 16, 32);
	         drawHeight = 96;
	         isDecor = true;
	         hitbox.height = 80;
	         break;
		 case 25:
			 name = "Toilet Paper";
			 isSolid = false;
	         animations[0][0][0] = importImage("/decor/bathroom props.png").getSubimage(208, 160, 16, 16);
	         isBathroomBuilding = true;
	         break;
        }
		
	}
	public void draw(Graphics2D g2) {
		if(firstUpdate) {
			firstUpdate = false;
			switch(type) {
			case 11, 12, 13:
				invisHitbox = new Rectangle2D.Float(hitbox.x, hitbox.y, 80, hitbox.height);
				break;
			}
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
