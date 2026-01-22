package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Tomato extends Food {
	
	public Tomato(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Tomato";
		importImages();
		foodLayer = 1;
		cutIntoNewItem = true;
		chopCount = 7;
		cookTime = 20;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/crops/tomato.png").getSubimage(16, 16, 16, 16);
		choppedImage = importImage("/food/pasta/CookedTomato.png").toTextureRegion();
		panPlated = importImage("/food/pasta/CookedTomato.png").toTextureRegion();
		ovenPlated = importImage("/food/pasta/CookedTomato.png").toTextureRegion();
	}
	
	
}
