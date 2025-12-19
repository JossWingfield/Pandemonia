package entity.buildings;

import java.awt.geom.Rectangle2D;

import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.TextureRegion;
import utility.DayPhase;

public class TipJar extends Building {
	
	private boolean firstDraw = true;
	
	public TipJar(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		
		isSolid = false;
		
		hitbox.width = 32;
		hitbox.height = 32;
		drawWidth = 48;
		drawHeight = 48;
		xDrawOffset = 8;
		yDrawOffset = 8;
		mustBePlacedOnTable = true;
        canBePlacedOnShelf = true;
		isDecor = true;
		importImages();
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*6, hitbox.height-3*6);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*3, hitbox.width-3*6, hitbox.height-3*6);
	}
	public Building clone() {
		TipJar building = new TipJar(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new TipJar(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new TextureRegion[1][1][3];
		
		name = "Tip Jar";
		animations[0][0][0] = importImage("/decor/kitchen props.png").getSubimage(0, 0, 16, 16);
		animations[0][0][1] = importImage("/decor/kitchen props.png").getSubimage(16, 0, 16, 16);
		animations[0][0][2] = importImage("/decor/kitchen props.png").getSubimage(32, 0, 16, 16);
	}
	public void destroy() {
		gp.progressM.tipJarPresent = false;
	}
	public void update(double dt) {
		super.update(dt);
		if(firstDraw) {
			gp.progressM.tipJarPresent = true;
			firstDraw = false;
		}
	}
	public void draw(Renderer renderer) {
		
		int a = 0;
		if(gp.world.getCurrentPhase() == DayPhase.SERVICE) {
			a = 1;
		} else if(gp.world.getCurrentPhase() == DayPhase.AFTER_HOURS) {
			a = 2;
		}
        
	    renderer.draw(animations[0][0][a], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
        
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int) hitbox.x , (int) (hitbox.y ), gp.tileSize, gp.tileSize);
		}
	        
	}
}
