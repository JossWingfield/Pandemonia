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
		chopCount = 16;
		cookTime = 24;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][4];
		
		rawImage = importImage("/food/drumstick/RawMeat.png").toTextureRegion();;
		ovenPlated = importImage("/food/drumstick/CookedMeat.png").toTextureRegion();;
		ovenPlated = importImage("/food/drumstick/CookedMeat.png").toTextureRegion();;
	}
	
}
