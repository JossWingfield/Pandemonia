package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Egg extends Food {
	
	public Egg(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Egg";
		importImages();
		foodLayer = 1;
		cookTime = 18;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][4];
		
		rawImage = importImage("/food/egg/Egg.png").toTextureRegion();
		panPlated = importImage("/food/egg/PlatedEgg.png").toTextureRegion();
		potPlated = importImage("/food/egg/ScrambledEgg.png").getSubimage(16, 0, 16, 16);
	}
	
	
}