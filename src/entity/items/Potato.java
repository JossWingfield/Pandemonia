package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Potato extends Food {
	
	public Potato(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Potato";
		importImages();
		foodLayer = 1;
		cookTime = 26;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/crops/potato.png").getSubimage(16, 16, 16, 16);
		ovenPlated = importImage("/food/food.png").getSubimage(0, 0, 16, 16);
	}
	
	
}
