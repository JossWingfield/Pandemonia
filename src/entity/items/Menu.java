package entity.items;

import main.GamePanel;
import main.renderer.TextureRegion;

public class Menu extends Item {
	
	public Menu(GamePanel gp) {
		super(gp);
		name = "Menu";
		importImages();
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		animations[0][0][0] = importImage("/decor/MenuBooks.png").getSubimage(0, 0, 16, 16);
	}
	
	
}