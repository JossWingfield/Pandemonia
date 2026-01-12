package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Meatball extends Food{
	
	public Meatball(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Meatball";
		importImages();
		foodLayer = 2;
		notRawItem = true;
		cookTime = 22;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		animations[0][0][0] = importImage("/food/pasta/Meatball.png").getSubimage(0, 0, 16, 16);
		animations[0][0][1] = importImage("/food/pasta/Meatball.png").getSubimage(0, 0, 16, 16);
		animations[0][0][2] = importImage("/food/pasta/Meatball.png").getSubimage(16, 0, 16, 16);
		animations[0][0][3] = importImage("/food/pasta/Meatball.png").getSubimage(16, 0, 16, 16);
		animations[0][0][4] = importImage("/food/pasta/Meatball.png").getSubimage(0, 0, 16, 16);
	}
	
	
	
	
}
