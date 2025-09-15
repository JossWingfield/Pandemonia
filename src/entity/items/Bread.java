package entity.items;

import java.awt.image.BufferedImage;

import main.GamePanel;

public class Bread extends Food {
	
	public Bread(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Bread";
		importImages();
		foodLayer = 3;
	}
	
	private void importImages() {
		animations = new BufferedImage[1][1][5];
		
		animations[0][0][0] = importImage("/food/food.png").getSubimage(48, 0, 16, 16);
		animations[0][0][4] = importImage("/food/Bread.png").getSubimage(0, 0, 16, 16);
		animations[0][0][2] = importImage("/food/Bread.png").getSubimage(0, 0, 16, 16);
	}
	
	
}