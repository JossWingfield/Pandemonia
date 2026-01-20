package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class RedOnion extends Food {
	
	public RedOnion(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Red Onion";
		importImages();
		foodLayer = 1;
		cookTime = 18;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/crops/RedOnion.png").getSubimage(16, 16, 16, 16);
	}
	
	
}