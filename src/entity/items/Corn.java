package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Corn extends Food {
	
	public Corn(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Corn";
		importImages();
		foodLayer = 1;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		animations[0][0][0] = importImage("/food/crops/corn.png").getSubimage(16, 16, 16, 16);
	}
	
	
}
