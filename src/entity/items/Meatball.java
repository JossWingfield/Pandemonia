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
		
		rawImage = importImage("/food/pasta/Meatball.png").getSubimage(0, 0, 16, 16);
		panPlated = importImage("/food/pasta/Meatball.png").getSubimage(0, 0, 16, 16);
		choppedImage = importImage("/food/pasta/Meatball.png").getSubimage(0, 0, 16, 16);
	}
	
	
	
	
}
