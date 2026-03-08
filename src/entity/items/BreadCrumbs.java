package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class BreadCrumbs extends Coating {
	
	public BreadCrumbs(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Bread Crumbs";
		importImages();
		foodLayer = 1;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/coating/BreadCrumbs.png").getSubimage(0, 0, 16, 16);
	}
	
}