package entity.buildings;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class Computer extends Building {
	
	public Computer(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		
		blueprint = false;
		drawWidth = 16*3;
		drawHeight = 32*3;
		yDrawOffset = 24;
		//xDrawOffset = 16;
		importImages();
		isSolid = true;
		//isBottomLayer = true;
		isDecor = true;
		mustBePlacedOnFloor = true;
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*5, hitbox.width-3*3, hitbox.height-3*6);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*5, hitbox.width-3*3, hitbox.height-3*6);
	}
	public Building clone() {
		Computer building = new Computer(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Computer(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new BufferedImage[1][1][1];
		
		name = "Computer";
    	animations[0][0][0] = importImage("/decor/Computer.png");

	}
	public void update(double dt) {
		super.update(dt);
		if(hitbox.intersects(gp.player.interactHitbox)) {
			if(gp.keyI.ePressed) {
            	gp.currentState = gp.catalogueState;
            	gp.gui.resetComputerAnimations();
			}
		}
	}
}
