package entity.buildings;

import java.awt.geom.Rectangle2D;

import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.TextureRegion;

public class EscapeHole extends Building {
	
	private boolean firstUpdate = true;
	
	public EscapeHole(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		
		isSolid = false;
		
		drawWidth = 16*3;
		drawHeight = 16*3;
		importImages();
		canBePlaced = false;
		npcHitbox = new Rectangle2D.Float(hitbox.x + 24, hitbox.y+hitbox.height - 48, 48, 80);
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*4, hitbox.y+3*4, hitbox.width-3*4, hitbox.height-3*8);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*4, hitbox.y+3*4, hitbox.width-3*4, hitbox.height-3*8);
	}
	public Building clone() {
		EscapeHole building = new EscapeHole(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new EscapeHole(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new TextureRegion[1][1][1];
		
		name = "Escape Hole";
    	animations[0][0][0] = importImage("/decor/EscapeHole.png").toTextureRegion();
	}
	public void draw(Renderer renderer) {
		
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		}
	        
	}
}
