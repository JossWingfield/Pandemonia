package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class BurntFood extends Food {
	
	public BurntFood(GamePanel gp) {
		super(gp);
		name = "Burnt Food";
		importImages();
		foodLayer = 4;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/BurntFood.png").toTextureRegion();
		choppedImage = importImage("/food/BurntFood.png").toTextureRegion();
		generalPlated = importImage("/food/BurntFood.png").toTextureRegion();
		potPlated = importImage("/food/BurntFood.png").toTextureRegion();
		panPlated = importImage("/food/BurntFood.png").toTextureRegion();
	}
	
	

}
