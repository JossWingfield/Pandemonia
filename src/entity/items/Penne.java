package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Penne extends Food {
	
	public Penne(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Penne";
		importImages();
		foodLayer = 0;
		cookTime = 30;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/pasta/Penne.png").getSubimage(0, 0, 16, 16);
		potPlated = importImage("/food/pasta/Penne.png").getSubimage(16, 0, 16, 16);
		ovenPlated = importImage("/food/pasta/Penne.png").getSubimage(16, 0, 16, 16);
	}
	
	
}
