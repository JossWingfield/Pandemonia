package entity.buildings;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class Turntable extends Building {
	
	public Turntable(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		
		isSolid = false;
		blueprint = false;
		hitbox.width = 32;
		hitbox.height = 32;
		drawWidth = 48*3;
		drawHeight = 48*3;
		yDrawOffset = 48+48 + 8;
		xDrawOffset = 48 + 8;
		animationSpeedFactor = 6;
		mustBePlacedOnTable = true;
		gp.world.turntablePresent = true;
		isDecor = true;
		importImages();
	}
	public Building clone() {
		Turntable building = new Turntable(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Turntable(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new BufferedImage[1][1][10];
		
		name = "Turntable";
		importFromSpriteSheet("/decor/turntable.png", 8, 1, 0, 0, 0, 48, 48, 0);

	}
	public void destroy() {
		gp.world.turntablePresent = false;
	}
	public void draw(Graphics2D g2) {
		
		animationSpeed++; //Updating animation frame
        if (animationSpeed == animationSpeedFactor) {
            animationSpeed = 0;
            animationCounter++;
        }

        if (animations[direction][currentAnimation][animationCounter] == null) {
            animationCounter = 0;
        }		
        
	    g2.drawImage(animations[direction][currentAnimation][animationCounter], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
        
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff), gp.tileSize, gp.tileSize, null);
		}
	        
	}
}
