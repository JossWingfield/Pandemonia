package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Sage extends Seasoning {
	
	public Sage(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Sage";
		importImages();
	}
		
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/seasoning/sage.png").getSubimage(0, 16, 16, 16);
		choppedImage = importImage("/food/seasoning/Seasoning.png").toTextureRegion();;
		generalPlated = importImage("/food/seasoning/Seasoning.png").toTextureRegion();;
	}

}