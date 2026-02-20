package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class IceBlock extends Food {
	
	public Food enclosedItem = null;
	
	public IceBlock(GamePanel gp, Food enclosedItem) {
		super(gp, 0, 0);
		this.enclosedItem = enclosedItem;
		name = "Ice Block";
		importImages();
		foodLayer = 0;
		chopCount = 20;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		rawImage = importImage("/food/IceBlock.png").getSubimage(0, 0, 16, 16);
	}
	
	
}