package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Garlic extends Food {
	
	public Garlic(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Garlic";
		importImages();
		foodLayer = 3;
		chopCount = 16;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/crops/garlic.png").getSubimage(16, 16, 16, 16);
		//choppedImage = importImage("/food/greens/Greens.png").toTextureRegion();;
		//generalPlated = importImage("/food/greens/Greens.png").toTextureRegion();;

	}
	
	
}
