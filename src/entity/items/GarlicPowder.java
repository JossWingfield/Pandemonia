package entity.items;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.TextureRegion;

public class GarlicPowder extends Seasoning {
		
	public GarlicPowder(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Garlic Powder";
		importImages();
		isAromaticSpice = true;
		particleColours.add(new Colour("caaf8b"));
		particleColours.add(new Colour("d5bf9c"));
		particleColours.add(new Colour("decdaa"));
	}
		
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/seasoning/Spices.png").getSubimage(96+32, 0, 16, 16);
		//choppedImage = importImage("/food/seasoning/Seasoning.png").toTextureRegion();
		//generalPlated = importImage("/food/seasoning/Seasoning.png").toTextureRegion();
	}

}
