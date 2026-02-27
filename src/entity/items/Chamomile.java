package entity.items;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.TextureRegion;

public class Chamomile extends Seasoning {
		
	public Chamomile(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Chamomile";
		importImages();
		isHerb = true;
		particleColours.add(new Colour("64a693"));
		particleColours.add(new Colour("85be8c"));
		particleColours.add(new Colour("518782"));
		
		particleColours.add(new Colour("e3eadf"));
		particleColours.add(new Colour("efce89"));
		particleColours.add(new Colour("c7dcd0"));
	}
		
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/seasoning/chamomile.png").getSubimage(0, 16, 16, 16);
		choppedImage = importImage("/food/seasoning/Seasoning.png").toTextureRegion();
		generalPlated = importImage("/food/seasoning/Seasoning.png").toTextureRegion();
	}

}
