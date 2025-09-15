package entity.items;

import java.awt.image.BufferedImage;

import main.GamePanel;

public class Egg extends Food {
	
	public Egg(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Egg";
		importImages();
		foodLayer = 1;
	}
	
	private void importImages() {
		animations = new BufferedImage[1][1][4];
		
		animations[0][0][0] = importImage("/food/egg/Egg.png");
		animations[0][0][1] = importImage("/food/egg/PlatedEgg.png");
		animations[0][0][2] = importImage("/food/egg/PlatedEgg.png");
		animations[0][0][3] = importImage("/food/food.png").getSubimage(32, 112, 16, 16);
	}
	
	
}