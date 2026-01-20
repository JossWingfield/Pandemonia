package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class ChoppedTomatoes extends Food {
	
	public ChoppedTomatoes(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Chopped Tomatoes";
		importImages();
		foodLayer = 4;
		notRawItem = true;
		cookTime = 18;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/pasta/ChoppedTomatoes.png").toTextureRegion();
		panPlated = importImage("/food/pasta/CookedTomato.png").toTextureRegion();
		generalPlated = importImage("/food/pasta/CookedTomato.png").toTextureRegion();;
	}
	
	
	
	
}
