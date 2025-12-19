package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Cheese extends Food {
	
	public Cheese(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Cheese";
		importImages();
		foodLayer = 3;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		animations[0][0][0] = importImage("/food/cheese/Cheese.png").getSubimage(0, 0, 16, 16);
		animations[0][0][4] = importImage("/food/cheese/Cheese.png").getSubimage(0, 16, 16, 16);
		animations[0][0][2] = importImage("/food/cheese/Cheese.png").getSubimage(0, 32, 16, 16);
	}
	
	
}
