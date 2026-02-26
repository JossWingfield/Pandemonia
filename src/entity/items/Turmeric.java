package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Turmeric extends Seasoning {
		
	public Turmeric(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Turmeric";
		importImages();
	}
		
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/seasoning/chamomile.png").getSubimage(0, 16, 16, 16);
		//choppedImage = importImage("/food/seasoning/Seasoning.png").toTextureRegion();
		//generalPlated = importImage("/food/seasoning/Seasoning.png").toTextureRegion();
	}

}
