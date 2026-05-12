package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class BreadSlice extends Food {
	
	public BreadSlice(GamePanel gp) {
		super(gp);
		name = "Bread Slice";
		importImages();
		foodLayer = 2;
		chopCount = 12;
		notRawItem = true;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/Bread.png").getSubimage(0, 0, 16, 16);
		choppedImage = importImage("/food/Bread.png").getSubimage(0, 0, 16, 16);
		generalPlated = importImage("/food/Bread.png").getSubimage(0, 0, 16, 16);
		ovenPlated = importImage("/food/egg/ScrambledEgg.png").getSubimage(0, 0, 16, 16);
	}
	
	
}