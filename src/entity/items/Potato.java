package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Potato extends Food {
	
	public Potato(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Potato";
		importImages();
		foodLayer = 1;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		animations[0][0][0] = importImage("/food/crops/potato.png").getSubimage(16, 16, 16, 16);
		animations[0][0][1] = importImage("/food/food.png").getSubimage(0, 0, 16, 16);
		animations[0][0][2] = importImage("/food/food.png").getSubimage(0, 16, 16, 16);
	}
	
	
}
