package entity.items;

import java.util.ArrayList;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.TextureRegion;

public class Thyme extends Seasoning {
		
	public Thyme(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Thyme";
		importImages();
		isHerb = true;
		particleColours.add(new Colour("64a693"));
		particleColours.add(new Colour("85be8c"));
		particleColours.add(new Colour("518782"));
		particleColours.add(new Colour("c29979"));
		particleColours.add(new Colour("a2786a"));
	}
		
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/seasoning/thyme.png").getSubimage(0, 16, 16, 16);
		choppedImage = importImage("/food/seasoning/Seasoning.png").toTextureRegion();
		generalPlated = importImage("/food/seasoning/Seasoning.png").toTextureRegion();
	}

}
