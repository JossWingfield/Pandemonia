package entity.items;

import java.awt.image.BufferedImage;

import main.GamePanel;

public class Steak extends Food{
	
	public Steak(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Steak";
		importImages();
		foodLayer = 1;
		cutIntoNewItem = true;
	}
	
	private void importImages() {
		animations = new BufferedImage[1][1][5];
		
		animations[0][0][0] = importImage("/food/Steak.png").getSubimage(0, 0, 16, 16);
		animations[0][0][1] = importImage("/food/Steak.png").getSubimage(16, 0, 16, 16);
		animations[0][0][3] = importImage("/food/Steak.png").getSubimage(16, 0, 16, 16);
	}
	
	
	
	
}
