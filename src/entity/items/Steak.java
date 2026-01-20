package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Steak extends Food{
	
	public Steak(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Steak";
		importImages();
		foodLayer = 1;
		cutIntoNewItem = true;
		chopCount = 12;
		cookTime = 24;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/Steak.png").getSubimage(0, 0, 16, 16);
		panPlated = importImage("/food/Steak.png").getSubimage(16, 0, 16, 16);
	}
	
	
	
	
}
