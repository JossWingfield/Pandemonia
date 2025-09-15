package entity.items;

import java.awt.image.BufferedImage;

import main.GamePanel;

public class Corn extends Food {
	
	public Corn(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Corn";
		importImages();
		foodLayer = 1;
	}
	
	private void importImages() {
		animations = new BufferedImage[1][1][5];
		
		animations[0][0][0] = importImage("/food/crops/corn.png").getSubimage(16, 16, 16, 16);
	}
	
	
}
