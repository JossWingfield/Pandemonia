package entity.buildings;

import java.awt.geom.Rectangle2D;

import org.lwjgl.glfw.GLFW;

import main.GamePanel;
import main.renderer.TextureRegion;
import utility.DayPhase;

public class Bed extends Building{
	
	private Rectangle2D.Float interactHitbox;
	
	private int cooldown = 0;
	
	public Bed(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48*2, 48*2);
		
		isSolid = true;
		drawWidth = 32*3;
		drawHeight = 48*3;
		yDrawOffset = 24;
		importImages();
		interactHitbox = new Rectangle2D.Float(hitbox.x+56, hitbox.y+48, 32, hitbox.height);
		isDecor = true;
		mustBePlacedOnFloor = true;
		npcHitbox = interactHitbox;
		buildHitbox = new Rectangle2D.Float(hitbox.x + 3, hitbox.y+3, hitbox.width-6, hitbox.height-6);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x + 3, hitbox.y+3, hitbox.width-6, hitbox.height-6);
	}
	public Building clone() {
		Bed building = new Bed(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Bed(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new TextureRegion[1][1][1];
		
		name = "Bed";
    	animations[0][0][0] = importImage("/decor/bed.png").getSubimage(160, 96, 32, 48);
	}
	public void update(double dt) {
		super.update(dt);
		if (cooldown > 0) {
		    cooldown -= dt;        // subtract elapsed time in seconds
		    if (cooldown < 0) {
		        cooldown = 0;      // clamp to zero
		    }
		}
	    if(hitbox.intersects(gp.player.interactHitbox)) {
	    	if(gp.world.getCurrentPhase() == DayPhase.AFTER_HOURS) {
	    		if(gp.keyL.isKeyPressed(GLFW.GLFW_KEY_E) && cooldown == 0) {
	    			gp.world.sleep();
	    			cooldown = 1;
	    		}
	    	}
	    }
	}
	
	
}