package entity.buildings;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import entity.buildings.outdoor.SeasonalDecoration;
import entity.items.Item;
import main.GamePanel;
import utility.CollisionMethods;

public class Calendar extends Building {
	
	public Calendar(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		
		isSolid = false;
		blueprint = false;
		drawWidth = 16*3;
		drawHeight = 32*3;
		importImages();
		mustBePlacedOnWall = true;
		yDrawOffset = 24;
		buildHitbox = new Rectangle2D.Float(hitbox.x + 3, hitbox.y+3, hitbox.width-3*4, hitbox.height-3*4);

	}
	public Building clone() {
		Calendar calendar = new Calendar(gp, hitbox.x, hitbox.y);
		return calendar;
    }
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x + 3, hitbox.y+3, hitbox.width-3*4, hitbox.height-3*4);
	}
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Calendar(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new BufferedImage[1][1][5];
		
		name = "Calendar";
     	animations[0][0][0] = importImage("/decor/calendar.png").getSubimage(0, 0, 16, 32);
     	animations[0][0][1] = importImage("/decor/calendar.png").getSubimage(16, 0, 16, 32);
     	animations[0][0][2] = importImage("/decor/calendar.png").getSubimage(32, 0, 16, 32);
     	animations[0][0][3] = importImage("/decor/calendar.png").getSubimage(48, 0, 16, 32);
	}
	public void draw(Graphics2D g2, int xDiff, int yDiff) {
		int season = -1;
		switch(gp.world.getCurrentSeason()) {
		case SPRING:
			season = 0;
			break;
		case SUMMER:
			season = 1;
			break;
		case AUTUMN:
			season = 2;
			break;
		case WINTER:
			season = 3;
			break;
		}
	     g2.drawImage(animations[0][0][season], (int)(hitbox.x - xDrawOffset - xDiff), (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
	     
		 if(destructionUIOpen) {
		     g2.drawImage(destructionImage, (int)(hitbox.x - xDrawOffset - xDiff), (int) (hitbox.y - yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		 }
	        
	}
	
}
