package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Chicken extends Food {
	
	public Chicken(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Chicken";
		importImages();
		foodLayer = 0;
		cutIntoNewItem = true;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][4];
		
		animations[0][0][0] = importImage("/food/drumstick/RawMeat.png").toTextureRegion();;
		animations[0][0][1] = importImage("/food/drumstick/CookedMeat.png").toTextureRegion();;
		animations[0][0][2] = importImage("/food/drumstick/CookedMeat.png").toTextureRegion();;
		animations[0][0][3] = importImage("/food/food.png").getSubimage(0, 96+16, 16, 16);
	}
	
}
