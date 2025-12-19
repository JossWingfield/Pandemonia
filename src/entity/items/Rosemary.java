package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Rosemary extends Seasoning {
	
	public Rosemary(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Rosemary";
		importImages();
	}
		
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		animations[0][0][0] = importImage("/food/seasoning/rosemary.png").getSubimage(0, 16, 16, 16);
		animations[0][0][1] = importImage("/food/seasoning/Seasoning.png").toTextureRegion();;
		animations[0][0][2] = importImage("/food/seasoning/Seasoning.png").toTextureRegion();;
		animations[0][0][3] = importImage("/food/seasoning/Seasoning.png").toTextureRegion();;
		animations[0][0][4] = importImage("/food/seasoning/Seasoning.png").toTextureRegion();;
	}

}
