package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Thyme extends Seasoning {
		
	public Thyme(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Thyme";
		importImages();
	}
		
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/seasoning/thyme.png").getSubimage(0, 16, 16, 16);
		choppedImage = importImage("/food/seasoning/Seasoning.png").toTextureRegion();
		generalPlated = importImage("/food/seasoning/Seasoning.png").toTextureRegion();
	}

}
