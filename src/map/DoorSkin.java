package map;

import main.GamePanel;
import main.renderer.AssetPool;
import main.renderer.Texture;
import main.renderer.TextureRegion;

public class DoorSkin {

	GamePanel gp;
	
	public int preset;
	private TextureRegion img, openImg;
    public int cost = 0;
    public String name = "Door Skin";
    public String description = ""; 
	public TextureRegion catalogueIcon;

	public DoorSkin(GamePanel gp, int preset) {
		this.gp = gp;
		this.preset = preset;
		cost = 20;
		description = "A new door for your restaurant.";
		importImages();
	}
	public void setCatalogueIcon(TextureRegion icon) {
		this.catalogueIcon = icon;
	}
	public void importImages() {
		
		switch(preset) {
		case 0:
	        img = importImage("/decor/door.png").getSubimage(0, 48, 32, 48);
	        openImg = importImage("/decor/door.png").getSubimage(32, 0, 32, 48);
			break;
		case 1:
			name = "Cabin Door";
	        img = importImage("/decor/catalogue/cabin/CabinDoor.png").getSubimage(0, 0, 32, 48);
	        openImg = importImage("/decor/door.png").getSubimage(32, 0, 32, 48);
			break;
		case 2:
			name = "Fish Shack Door";
	        img = importImage("/decor/catalogue/fishingshack/FishDoor.png").getSubimage(0, 0, 32, 48);
	        openImg = importImage("/decor/catalogue/fishingshack/FishDoor.png").getSubimage(32, 0, 32, 48);
			break;
		case 3:
			name = "Barn Door";
	        img = importImage("/decor/catalogue/farm/BarnDoor.png").getSubimage(0, 0, 32, 48);
	        openImg = importImage("/decor/catalogue/farm/BarnDoor.png").getSubimage(32, 0, 32, 48);
			break;
		}
		
	}

	public TextureRegion getImage() {
		return img;
	}
	public TextureRegion getOpenImage() {
		return openImg;
	}
	 public Texture importImage(String filePath) {
			Texture texture = AssetPool.getTexture(filePath);
		    return texture;
		}
	
}
