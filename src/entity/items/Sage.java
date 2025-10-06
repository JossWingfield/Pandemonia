package entity.items;

import java.awt.image.BufferedImage;

import main.GamePanel;

public class Sage extends Seasoning {
	
	public Sage(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Sage";
		importImages();
	}
		
	private void importImages() {
		animations = new BufferedImage[1][1][5];
		
		animations[0][0][0] = importImage("/food/seasoning/sage.png").getSubimage(0, 16, 16, 16);
	}

}