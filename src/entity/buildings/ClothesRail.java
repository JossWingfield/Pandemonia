package entity.buildings;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import org.lwjgl.glfw.GLFW;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.TextureRegion;
import map.LightSource;

public class ClothesRail extends Building{
	
	public ClothesRail(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48*2);
		
		description = "Change your outfit.";
		cost = 20;
		
		isSolid = true;
		
		importImages();
		mustBePlacedOnFloor = true;
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*4, hitbox.y+3*2, hitbox.width-3*8, hitbox.height-3*6);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*4, hitbox.y+3*2, hitbox.width-3*8, hitbox.height-3*6);
	}
	public Building clone() {
		ClothesRail building = new ClothesRail(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new ClothesRail(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new TextureRegion[1][1][2];
		
    	name = "Clothes Rail";
    	animations[0][0][0] = importImage("/decor/clothes.png").getSubimage(32, 128, 16, 40);
    	animations[0][0][1] = importImage("/decor/ClothesRailHighlight.png").getSubimage(32, 128, 16, 40);
    	drawWidth = 16*3;
    	drawHeight = 40*3;
    	hitbox.width = 16*3;
    	hitbox.height = 32*3;
    	yDrawOffset = 24;
    	isDecor = true;
    	mustBePlacedOnFloor = true;
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*6, hitbox.height-3*6);
	}
	public void inputUpdate(double dt) {
        
        if(gp.player.interactHitbox.intersects(buildHitbox)) {
        	if(gp.keyL.keyBeginPress(GLFW.GLFW_KEY_E) && gp.player.clickCounter == 0) {
	        	gp.player.clickCounter = 0.33;
	        	gp.gui.clickCooldown = 0.33;
	        	//ENTER CUSTOMISATION STATE
	        	gp.currentState = gp.customiseOutfitScreen;
        	}
        }
        	
	}
	public void draw(Renderer renderer) {
		
		if(gp.player.interactHitbox.intersects(buildHitbox)) {
		  	renderer.draw(animations[0][0][1], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
	    } else {
		  	renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
	    }
	    
	}
	
	
}
