package entity.items;

import java.awt.image.BufferedImage;

import main.GamePanel;

public class Carrot extends Food {
	
	public Carrot(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Carrot";
		importImages();
		foodLayer = 1;
	}
	
	private void importImages() {
		animations = new BufferedImage[1][1][5];
		
		animations[0][0][0] = importImage("/food/crops/carrot.png").getSubimage(16, 16, 16, 16);
	}
	
	
}