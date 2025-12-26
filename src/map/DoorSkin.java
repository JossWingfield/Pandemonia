package map;

import main.GamePanel;
import main.renderer.AssetPool;
import main.renderer.Texture;
import main.renderer.TextureRegion;

public class DoorSkin {

	GamePanel gp;
	
	public int preset;
	private TextureRegion img;
    public int cost = 0;
    public String name = "Door Skin";
    public String description = ""; 

	public DoorSkin(GamePanel gp, int preset) {
		this.gp = gp;
		this.preset = preset;
		cost = 20;
		description = "A new door for your restaurant.";
		importImages();
	}
	public void importImages() {
		
		switch(preset) {
		case 0:
	        img = importImage("/decor/door.png").getSubimage(0, 48, 32, 48);
			break;
		case 1:
	        img = importImage("/decor/door.png").getSubimage(32, 48, 32, 48);
			break;
		}
		
	}

	public TextureRegion getImage() {
		return img;
	}
	 public Texture importImage(String filePath) {
			Texture texture = AssetPool.getTexture(filePath);
		    return texture;
		}
	
}
