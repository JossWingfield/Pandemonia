package entity.items;

import java.awt.image.BufferedImage;

import main.GamePanel;

public class Rosemary extends Seasoning {
	
	public Rosemary(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Rosemary";
		importImages();
	}
		
	private void importImages() {
		animations = new BufferedImage[1][1][5];
		
		animations[0][0][0] = importImage("/food/seasoning/rosemary.png").getSubimage(0, 16, 16, 16);
	}

}
