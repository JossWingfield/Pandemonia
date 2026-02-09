package entity.npc;

import java.awt.geom.Rectangle2D;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Employee extends NPC {
	
	private Rectangle2D.Float doorHitbox;

	public Employee(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		drawWidth = 48;
		drawHeight = 48*2;
		speed = 2*60;
		
		doorHitbox = new Rectangle2D.Float(9*48, 11*48, 48, 48);
		
		importImages();
	}
	
	private void importImages() {
		animations = new TextureRegion[1][2][2];
		//animations[0][0][0] = importImage("/npcs/Chicken 3.png").getSubimage(0, 0, 16, 32);
	}
}
