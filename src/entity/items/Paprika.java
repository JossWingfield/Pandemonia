package entity.items;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.TextureRegion;

public class Paprika extends Seasoning {
		
	public Paprika(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Paprika";
		isSpice = true;
		importImages();
		particleColours.add(new Colour("ca5d67"));
		particleColours.add(new Colour("a94d60"));
	}
		
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/seasoning/Spices.png").getSubimage(16, 0, 16, 16);
		//choppedImage = importImage("/food/seasoning/Seasoning.png").toTextureRegion();
		//generalPlated = importImage("/food/seasoning/Seasoning.png").toTextureRegion();
	}

}
