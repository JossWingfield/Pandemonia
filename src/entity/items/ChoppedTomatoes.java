package entity.items;

import java.awt.image.BufferedImage;

import main.GamePanel;

public class ChoppedTomatoes extends Food {
	
	public ChoppedTomatoes(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Chopped Tomatoes";
		importImages();
		foodLayer = 1;
		notRawItem = true;
	}
	
	private void importImages() {
		animations = new BufferedImage[1][1][5];
		
		animations[0][0][0] = importImage("/food/pasta/ChoppedTomatoes.png");
		animations[0][0][1] = importImage("/food/pasta/CookedTomato.png");
		animations[0][0][2] = importImage("/food/pasta/CookedTomato.png");
		animations[0][0][3] = importImage("/food/pasta/CookedTomato.png");
		animations[0][0][4] = importImage("/food/pasta/CookedTomato.png");
	}
	
	
	
	
}
