package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Bread extends Food {
	
	public Bread(GamePanel gp) {
		super(gp);
		name = "Bread";
		importImages();
		foodLayer = 2;
		chopCount = 12;
		cutIntoNewItem = true;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/food.png").getSubimage(48, 0, 16, 16);
		choppedImage = importImage("/food/Bread.png").getSubimage(0, 0, 16, 16);
		generalPlated = importImage("/food/Bread.png").getSubimage(0, 0, 16, 16);
		ovenPlated = importImage("/food/egg/ScrambledEgg.png").getSubimage(0, 0, 16, 16);
	}
	
	
}