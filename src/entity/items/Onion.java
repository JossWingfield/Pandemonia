package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Onion extends Food {
	
	public Onion(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Onion";
		importImages();
		foodLayer = 1;
		cookTime = 18;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/crops/onion.png").getSubimage(16, 16, 16, 16);
	}
	
	
}