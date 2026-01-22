package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Carrot extends Food {
	
	public Carrot(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Carrot";
		importImages();
		foodLayer = 1;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/crops/carrot.png").getSubimage(16, 16, 16, 16);
		choppedImage = importImage("/food/Carrot.png").getSubimage(16, 0, 16, 16);
	}
	
	
}