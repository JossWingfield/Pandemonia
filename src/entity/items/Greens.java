package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Greens extends Food {
	
	public Greens(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Greens";
		importImages();
		foodLayer = 2;
		chopCount = 12;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/greens/caisim.png").getSubimage(16, 16, 16, 16);
		choppedImage = importImage("/food/greens/Greens.png").toTextureRegion();;
		generalPlated = importImage("/food/greens/Greens.png").toTextureRegion();;

	}
	
	
}
