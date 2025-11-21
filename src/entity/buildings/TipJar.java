package entity.buildings;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.GamePanel;
import utility.DayPhase;

public class TipJar extends Building {
	
	private boolean firstDraw = true;
	
	public TipJar(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		
		isSolid = false;
		blueprint = false;
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
		animations = new BufferedImage[1][1][3];
		
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
	public void draw(Graphics2D g2, int xDiff, int yDiff) {
		
		int a = 0;
		if(gp.world.getCurrentPhase() == DayPhase.SERVICE) {
			a = 1;
		} else if(gp.world.getCurrentPhase() == DayPhase.AFTER_HOURS) {
			a = 2;
		}
        
	    g2.drawImage(animations[0][0][a], (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
        
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - xDiff, (int) (hitbox.y - yDiff), gp.tileSize, gp.tileSize, null);
		}
	        
	}
}
