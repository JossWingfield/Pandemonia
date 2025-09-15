package entity.items;

import java.awt.image.BufferedImage;

import main.GamePanel;

public class Fish extends Food {
	
	public Fish(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Fish";
		importImages();
		foodLayer = 0;
	}
	
	private void importImages() {
		animations = new BufferedImage[1][1][4];
		
		animations[0][0][0] = importImage("/food/fish/Fish.png");
		//animations[0][0][1] = importImage("/food/drumstick/CookedMeat.png");
		animations[0][0][2] = importImage("/food/food.png").getSubimage(16, 96, 16, 16);
		animations[0][0][3] = importImage("/food/food.png").getSubimage(16, 96+16, 16, 16);
	}
	
	
}
