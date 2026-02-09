package entity.buildings;

import java.awt.geom.Rectangle2D;

import org.lwjgl.glfw.GLFW;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Computer extends Building {
	
	public Computer(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		
		
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
		animations = new TextureRegion[1][1][1];
		
		name = "Computer";
    	animations[0][0][0] = importImage("/decor/Computer.png").toTextureRegion();

	}
	public void updateState(double dt) {
		super.updateState(dt);
	}
	public void inputUpdate(double dt) {
		super.inputUpdate(dt);
		if(hitbox.intersects(gp.player.interactHitbox)) {
			if(gp.keyL.isKeyPressed(GLFW.GLFW_KEY_E)) {
            	gp.currentState = gp.catalogueState;
            	gp.gui.resetComputerAnimations();
			}
		}
	}
}
