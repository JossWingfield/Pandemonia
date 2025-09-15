package entity.items;

import java.awt.image.BufferedImage;

import main.GamePanel;

public class Greens extends Food {
	
	public Greens(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Greens";
		importImages();
		foodLayer = 2;
	}
	
	private void importImages() {
		animations = new BufferedImage[1][1][5];
		
		animations[0][0][0] = importImage("/food/greens/caisim.png").getSubimage(16, 16, 16, 16);
		animations[0][0][4] = importImage("/food/greens/Greens.png");
		animations[0][0][2] = importImage("/food/greens/Greens.png");

	}
	
	
}
