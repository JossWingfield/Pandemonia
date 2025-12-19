package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class BurntFood extends Food {
	
	public BurntFood(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Burnt Food";
		importImages();
		foodLayer = 4;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		animations[0][0][0] = importImage("/food/BurntFood.png").toTextureRegion();;
		animations[0][0][1] = importImage("/food/BurntFood.png").toTextureRegion();;
		animations[0][0][2] = importImage("/food/BurntFood.png").toTextureRegion();;
		animations[0][0][3] = importImage("/food/BurntFood.png").toTextureRegion();;
		animations[0][0][4] = importImage("/food/BurntFood.png").toTextureRegion();;
	}
	
	

}
