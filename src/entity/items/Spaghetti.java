package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Spaghetti extends Food {
	
	public Spaghetti(GamePanel gp) {
		super(gp);
		name = "Spaghetti";
		importImages();
		foodLayer = 0;
		cookTime = 1.0f;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/pasta/Spaghetti.png").getSubimage(16, 0, 16, 16);
		potPlated = importImage("/food/pasta/Spaghetti.png").getSubimage(32, 0, 16, 16);
	}
	
	
}