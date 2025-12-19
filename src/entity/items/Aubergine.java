package entity.items;


import main.GamePanel;
import main.renderer.TextureRegion;

public class Aubergine extends Food {
	
	public Aubergine(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Aubergine";
		importImages();
		foodLayer = 1;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		animations[0][0][0] = importImage("/food/crops/eggplant.png").getSubimage(16, 16, 16, 16);
	}
	
	
}
