package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class ChickenPieces extends Food {
	
	public ChickenPieces(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Chicken Pieces";
		importImages();
		foodLayer = 2;
		notRawItem = true;
		cookTime = 24;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/pasta/Chicken.png").getSubimage(0, 0, 16, 16);
		choppedImage = importImage("/food/pasta/Chicken.png").getSubimage(16, 0, 16, 16);
		panPlated = importImage("/food/pasta/Chicken.png").getSubimage(16, 0, 16, 16);
		choppedImage = importImage("/food/pasta/Chicken.png").getSubimage(16, 0, 16, 16);
	}
	
	
	
	
}