package entity.items;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.TextureRegion;

public class FennelSeeds extends Seasoning {
		
	public FennelSeeds(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Fennel Seeds";
		importImages();
		isAromaticSpice = true;
		particleColours.add(new Colour("576a3d"));
		particleColours.add(new Colour("6d8343"));
		particleColours.add(new Colour("89a350"));
	}
		
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/seasoning/Spices.png").getSubimage(96, 0, 16, 16);
		//choppedImage = importImage("/food/seasoning/Seasoning.png").toTextureRegion();
		//generalPlated = importImage("/food/seasoning/Seasoning.png").toTextureRegion();
	}

}
