package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Fish extends Food {
	
	public Fish(GamePanel gp) {
		super(gp);
		name = "Fish";
		importImages();
		foodLayer = 0;
		cookTime = 1.0f;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][4];
		
		rawImage = importImage("/food/fish/Fish.png").toTextureRegion();;
		potPlated = importImage("/food/food.png").getSubimage(16, 96, 16, 16);
		//animations[0][0][3] = importImage("/food/food.png").getSubimage(16, 96+16, 16, 16);
	}
	
	
}
