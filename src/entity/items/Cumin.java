package entity.items;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.TextureRegion;

public class Cumin extends Seasoning {
		
	public Cumin(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Cumin";
		isSpice = true;
		importImages();
		particleColours.add(new Colour("bb724b"));
		particleColours.add(new Colour("97543c"));
	}
		
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/seasoning/Spices.png").getSubimage(48, 0, 16, 16);
		//choppedImage = importImage("/food/seasoning/Seasoning.png").toTextureRegion();
		//generalPlated = importImage("/food/seasoning/Seasoning.png").toTextureRegion();
	}

}
