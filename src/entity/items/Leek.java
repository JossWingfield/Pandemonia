package entity.items;

import java.awt.image.BufferedImage;

import main.GamePanel;

public class Leek extends Food {
	
	public Leek(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Leek";
		importImages();
		foodLayer = 1;
	}
	
	private void importImages() {
		animations = new BufferedImage[1][1][5];
		
		animations[0][0][0] = importImage("/food/crops/leek.png").getSubimage(16, 16, 16, 16);
	}
	
	
}