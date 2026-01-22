package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Cheese extends Food {
	
	public Cheese(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Cheese";
		importImages();
		foodLayer = 3;
		chopCount = 12;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/cheese/Cheese.png").getSubimage(0, 0, 16, 16);
		choppedImage = importImage("/food/cheese/Cheese.png").getSubimage(0, 16, 16, 16);
		generalPlated = importImage("/food/cheese/Cheese.png").getSubimage(0, 32, 16, 16);
		ovenPlated = importImage("/food/CheeseToast.png").getSubimage(0, 0, 16, 16);
	}
	
	
}
