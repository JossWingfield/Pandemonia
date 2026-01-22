package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Lasagna extends Food {
	
	public Lasagna(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Lasagna";
		importImages();
		foodLayer = 0;
		cookTime = 30;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/pasta/Lasagna.png").getSubimage(0, 0, 16, 16);
		potPlated = importImage("/food/pasta/Lasagna.png").getSubimage(16, 0, 16, 16);
	}
	
	
}
