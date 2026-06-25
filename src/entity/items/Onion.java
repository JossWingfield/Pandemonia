package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Onion extends Food {
	
	public Onion(GamePanel gp) {
		super(gp);
		name = "Onion";
		importImages();
		foodLayer = 1;
		cookTime = 1.0f;;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/crops/onion.png").getSubimage(16, 16, 16, 16);
	}
	
	
}