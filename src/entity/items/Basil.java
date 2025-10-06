package entity.items;

import java.awt.image.BufferedImage;

import main.GamePanel;

public class Basil extends Seasoning {
	
	public Basil(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Basil";
		importImages();
	}
		
	private void importImages() {
		animations = new BufferedImage[1][1][5];
		
		animations[0][0][0] = importImage("/food/seasoning/basil.png").getSubimage(0, 16, 16, 16);
	}

}
