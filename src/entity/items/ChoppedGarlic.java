package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class ChoppedGarlic extends Food {
	
	public ChoppedGarlic(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Chopped Garlic";
		importImages();
		foodLayer = 3;
		notRawItem = true;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/GarlicBread.png").getSubimage(32, 0, 16, 16);
		choppedImage = importImage("/food/GarlicBread.png").getSubimage(32, 0, 16, 16);
		//generalPlated = importImage("/food/Bread.png").getSubimage(0, 0, 16, 16);
		ovenPlated = importImage("/food/egg/ScrambledEgg.png").getSubimage(0, 0, 16, 16);
	}
	
	
}