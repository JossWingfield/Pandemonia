package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class CursedGreens extends CursedFood {
	
	public CursedGreens(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Cursed Greens";
		importImages();
		foodLayer = 2;
		chopCount = 20;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/cursed/CursedGreens.png").getSubimage(0, 0, 16, 16);
		choppedImage = importImage("/food/cursed/CursedGreens.png").getSubimage(32, 0, 16, 16);;
		generalPlated = importImage("/food/cursed/CursedGreens.png").getSubimage(32, 0, 16, 16);;
	}
	
}
