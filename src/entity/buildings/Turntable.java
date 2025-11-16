package entity.buildings;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class Turntable extends Building {
	
	private boolean firstDraw = true;
	
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
		animationSpeedFactor = 2;
		mustBePlacedOnTable = true;
        canBePlacedOnShelf = true;
		isDecor = true;
		importImages();
		buildHitbox = new Rectangle2D.Float(hitbox.x + 3*2, hitbox.y+3, 48-3*6, 48-3*8);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x + 3*2, hitbox.y+3, 48-3*6, 48-3*8);
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
		gp.progressM.turntablePresent = false;
	}
	public void update() {
		super.update();
		if(firstDraw) {
			gp.progressM.turntablePresent = true;
			firstDraw = false;
		}
	}
	public void draw(Graphics2D g2, int xDiff, int yDiff) {
		
		animationSpeed++; //Updating animation frame
        if (animationSpeed == animationSpeedFactor) {
            animationSpeed = 0;
            animationCounter++;
        }

        if (animations[direction][currentAnimation][animationCounter] == null) {
            animationCounter = 0;
        }		
        
	    g2.drawImage(animations[direction][currentAnimation][animationCounter], (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
        
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - xDiff, (int) (hitbox.y - yDiff), gp.tileSize, gp.tileSize, null);
		}
	        
	}
}
