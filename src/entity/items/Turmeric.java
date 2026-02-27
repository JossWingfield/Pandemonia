package entity.items;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.TextureRegion;

public class Turmeric extends Seasoning {
		
	public Turmeric(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Turmeric";
		isSpice = true;
		importImages();
		particleColours.add(new Colour("d9965b"));
		particleColours.add(new Colour("e6b373"));
	}
		
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/seasoning/Spices.png").getSubimage(32, 0, 16, 16);
		//choppedImage = importImage("/food/seasoning/Seasoning.png").toTextureRegion();
		//generalPlated = importImage("/food/seasoning/Seasoning.png").toTextureRegion();
	}

}
