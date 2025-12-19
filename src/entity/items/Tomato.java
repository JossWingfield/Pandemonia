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
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		animations[0][0][0] = importImage("/food/crops/tomato.png").getSubimage(16, 16, 16, 16);
		animations[0][0][1] = importImage("/food/pasta/CookedTomato.png").toTextureRegion();
		animations[0][0][2] = importImage("/food/pasta/CookedTomato.png").toTextureRegion();
		animations[0][0][3] = importImage("/food/pasta/CookedTomato.png").toTextureRegion();
		animations[0][0][4] = importImage("/food/pasta/CookedTomato.png").toTextureRegion();
	}
	
	
}
