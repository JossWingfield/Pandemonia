package entity.items;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.TextureRegion;

public class CorianderSeeds extends Seasoning {
		
	public CorianderSeeds(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Coriander Seeds";
		importImages();
		isAromaticSpice = true;
		particleColours.add(new Colour("576a3d"));
		particleColours.add(new Colour("c69d68"));
		particleColours.add(new Colour("976c52"));
	}
		
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/seasoning/Spices.png").getSubimage(96+16, 0, 16, 16);
		//choppedImage = importImage("/food/seasoning/Seasoning.png").toTextureRegion();
		//generalPlated = importImage("/food/seasoning/Seasoning.png").toTextureRegion();
	}

}
