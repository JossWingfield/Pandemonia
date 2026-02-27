package entity.items;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.TextureRegion;

public class ChilliFlakes extends Seasoning {
		
	public ChilliFlakes(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Chilli Flakes";
		importImages();
		isSpice = true;
		particleColours.add(new Colour("844461"));
		particleColours.add(new Colour("ca5d67"));
		particleColours.add(new Colour("a94d60"));
		particleColours.add(new Colour("d9816b"));
		particleColours.add(new Colour("e6b373"));
		particleColours.add(new Colour("efce89"));
	}
		
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/seasoning/Spices.png").getSubimage(64, 0, 16, 16);
		//choppedImage = importImage("/food/seasoning/Seasoning.png").toTextureRegion();
		//generalPlated = importImage("/food/seasoning/Seasoning.png").toTextureRegion();
	}

}
