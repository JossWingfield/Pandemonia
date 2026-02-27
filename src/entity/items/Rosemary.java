package entity.items;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.TextureRegion;

public class Rosemary extends Seasoning {
	
	public Rosemary(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Rosemary";
		importImages();
		particleColours.add(new Colour("64a693"));
		particleColours.add(new Colour("85be8c"));
		particleColours.add(new Colour("518782"));
		isHerb = true;
	}
		
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/seasoning/rosemary.png").getSubimage(0, 16, 16, 16);
		choppedImage = importImage("/food/seasoning/Seasoning.png").toTextureRegion();;
		generalPlated = importImage("/food/seasoning/Seasoning.png").toTextureRegion();;
		choppedImage = importImage("/food/seasoning/Seasoning.png").toTextureRegion();;
	}

}
