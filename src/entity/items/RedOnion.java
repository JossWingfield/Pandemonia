package entity.items;

import java.awt.image.BufferedImage;

import main.GamePanel;

public class RedOnion extends Food {
	
	public RedOnion(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Red Onion";
		importImages();
		foodLayer = 1;
	}
	
	private void importImages() {
		animations = new BufferedImage[1][1][5];
		
		animations[0][0][0] = importImage("/food/crops/RedOnion.png").getSubimage(16, 16, 16, 16);
	}
	
	
}