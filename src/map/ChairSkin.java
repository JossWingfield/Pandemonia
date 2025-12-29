package map;

import main.GamePanel;
import main.renderer.AssetPool;
import main.renderer.Texture;
import main.renderer.TextureRegion;

public class ChairSkin {

	GamePanel gp;
	
	public int preset;
	private TextureRegion img;
    public int cost = 0;
    public String name = "Chair";
    public String description = ""; 
	public TextureRegion catalogueIcon;

	public ChairSkin(GamePanel gp, int preset) {
		this.gp = gp;
		this.preset = preset;
		cost = 20;
		description = "New Seating for your restaurant.";
		importImages();
	}
	public void setCatalogueIcon(TextureRegion icon) {
		this.catalogueIcon = icon;
	}
	public void importImages() {
		
		switch(preset) {
		case 0://ROUND
	        img = importImage("/decor/chair.png").getSubimage(2, 73, 16, 16);
			break;
		case 1:
	        img = importImage("/decor/chair.png").getSubimage(2+32, 73, 16, 16);
			break;
		case 2:
	        img = importImage("/decor/chair.png").getSubimage(2+64, 73, 16, 16);
			break;
		case 3:
	        img = importImage("/decor/chair.png").getSubimage(2+96, 73, 16, 16);
			break;
		case 4:
	        img = importImage("/decor/chair.png").getSubimage(2+96+32, 73, 16, 16);
			break;
		case 5://SQUARE
	        img = importImage("/decor/chair.png").getSubimage(2, 73-32, 16, 16);
	        cost = 40;
			break;
		case 6:
	        img = importImage("/decor/chair.png").getSubimage(2+32, 73-32, 16, 16);
	        cost = 40;
			break;
		case 7:
	        img = importImage("/decor/chair.png").getSubimage(2+64, 73-32, 16, 16);
	        cost = 40;
			break;
		case 8:
	        img = importImage("/decor/chair.png").getSubimage(2+64+32, 73-32, 16, 16);
	        cost = 40;
			break;
		case 9:
	        img = importImage("/decor/chair.png").getSubimage(2+64+64, 73-32, 16, 16);
	        cost = 40;
			break;
		case 10: //BACK CHAIR
	        img = importImage("/decor/chair.png").getSubimage(2, 73-64, 16, 16);
	        cost = 50;
			break;
		case 11:
	        img = importImage("/decor/chair.png").getSubimage(2+32, 73-64, 16, 16);
	        cost = 50;
			break;
		case 12:
	        img = importImage("/decor/chair.png").getSubimage(2+64, 73-64, 16, 16);
	        cost = 50;
			break;
		case 13: //OTHER SHAPES
			name = "Cabin Chairs";
	        img = importImage("/decor/chair.png").getSubimage(2, 73+32, 16, 16);
	        cost = 40;
			break;
		case 14:
	        img = importImage("/decor/chair.png").getSubimage(2+32, 73+32, 16, 16);
	        cost = 40;
			break;
		case 15:
	        img = importImage("/decor/chair.png").getSubimage(2+96, 73+32, 16, 16);
	        cost = 40;
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
