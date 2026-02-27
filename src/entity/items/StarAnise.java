package entity.items;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.TextureRegion;

public class StarAnise extends Seasoning {
		
	public StarAnise(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Star Anise";
		importImages();
		isAromaticSpice = true;
		particleColours.add(new Colour("664f4d"));
		particleColours.add(new Colour("7f615a"));
		particleColours.add(new Colour("b08671"));
	}
		
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/seasoning/Spices.png").getSubimage(80, 0, 16, 16);
		//choppedImage = importImage("/food/seasoning/Seasoning.png").toTextureRegion();
		//generalPlated = importImage("/food/seasoning/Seasoning.png").toTextureRegion();
	}

}
