package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Lasagna extends Food {
	
	public Lasagna(GamePanel gp) {
		super(gp);
		name = "Lasagna";
		importImages();
		foodLayer = 0;
		cookTime = 1.0f;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/pasta/Lasagna.png").getSubimage(0, 0, 16, 16);
		potPlated = importImage("/food/pasta/Lasagna.png").getSubimage(16, 0, 16, 16);
	}
	
	
}
