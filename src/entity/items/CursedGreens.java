package entity.items;

import java.awt.image.BufferedImage;

import main.GamePanel;

public class CursedGreens extends CursedFood {
	
	public CursedGreens(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Cursed Greens";
		importImages();
		foodLayer = 2;
	}
	
	private void importImages() {
		animations = new BufferedImage[1][1][5];
		
		animations[0][0][0] = importImage("/food/cursed/CursedGreens.png").getSubimage(0, 0, 16, 16);
		animations[0][0][4] = importImage("/food/cursed/CursedGreens.png").getSubimage(32, 0, 16, 16);;
		animations[0][0][2] = importImage("/food/cursed/CursedGreens.png").getSubimage(32, 0, 16, 16);;
	}
	
}
