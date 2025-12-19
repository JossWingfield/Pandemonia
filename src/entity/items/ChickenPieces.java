package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class ChickenPieces extends Food {
	
	public ChickenPieces(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Chicken Pieces";
		importImages();
		foodLayer = 2;
		notRawItem = true;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		animations[0][0][0] = importImage("/food/pasta/Chicken.png").getSubimage(0, 0, 16, 16);
		animations[0][0][1] = importImage("/food/pasta/Chicken.png").getSubimage(16, 0, 16, 16);
		animations[0][0][2] = importImage("/food/pasta/Chicken.png").getSubimage(16, 0, 16, 16);
		animations[0][0][3] = importImage("/food/pasta/Chicken.png").getSubimage(16, 0, 16, 16);
		animations[0][0][4] = importImage("/food/pasta/Chicken.png").getSubimage(16, 0, 16, 16);
	}
	
	
	
	
}