package entity.buildings;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import entity.buildings.outdoor.SeasonalDecoration;
import entity.items.Item;
import main.GamePanel;
import utility.CollisionMethods;

public class Window extends Building {
	
	public Window(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		
		isSolid = false;
		blueprint = false;
		drawWidth = 32*3;
		drawHeight = 32*3;
		xDrawOffset = 24;
		yDrawOffset = 24;
		importImages();
		isDecor = true;
		mustBePlacedOnWall = true;
		buildHitbox = new Rectangle2D.Float(hitbox.x + 3*2, hitbox.y+3, hitbox.width-3*2, hitbox.height-3*4);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x + 3*2, hitbox.y+3, hitbox.width-3*2, hitbox.height-3*4);
	}
	public Building clone() {
		Window calendar = new Window(gp, hitbox.x, hitbox.y);
		return calendar;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Window(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new BufferedImage[1][1][2];
		
		name = "Window";
     	animations[0][0][0] = importImage("/decor/window.png").getSubimage(32, 32, 32, 32);
     	animations[0][0][1] = importImage("/decor/window.png").getSubimage(0, 32, 32, 32);
		
	}
	public void draw(Graphics2D g2, int xDiff, int yDiff) {
		int state = 0;
		if(gp.world.getTimeOfDay() == 5) {
			state = 1;
		}
		
	     g2.drawImage(animations[0][0][state], (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
     
		 if(destructionUIOpen) {
		     g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		 }
	        
	}
	
}
