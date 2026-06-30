package entity.buildings;

import java.awt.geom.Rectangle2D;

import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.TextureRegion;

public class ChefPortrait extends Building{
	
	
	public ChefPortrait(GamePanel gp, float xPos, float yPos, int preset) {
		super(gp, xPos, yPos, 48*2, 48*2);
		this.preset = preset;
		
		isSolid = true;
		
		drawWidth = 32*3;
		drawHeight = 32*3;
		importImages();
		canBePlaced = false;
		isDecor = true;
		mustBePlacedOnWall = true;
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*6, hitbox.height-3*6);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*6, hitbox.height-3*6);
	}
	public Building clone() {
		ChefPortrait building = new ChefPortrait(gp, hitbox.x, hitbox.y, preset);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new ChefPortrait(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ", " + preset + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new TextureRegion[1][1][2];
		
		name = "Chef Portrait";
    	animations[0][0][0] = importImage("/decor/EmptyPainting.png").toTextureRegion();
    	animations[0][0][1] = importImage("/decor/Portraits.png").toTextureRegion();
	}
	public void draw(Renderer renderer) {

		if(gp.world.progressM.currentPhase >= preset+2) {
	   	     renderer.draw(animations[0][0][1], (int)(hitbox.x - xDrawOffset ), (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
	    } else {
	   	     renderer.draw(animations[0][0][0], (int)(hitbox.x - xDrawOffset ), (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
	    }
	    
	}
	
}
