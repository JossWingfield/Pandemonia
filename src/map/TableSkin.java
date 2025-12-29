package map;

import main.GamePanel;
import main.renderer.AssetPool;
import main.renderer.Texture;
import main.renderer.TextureRegion;

public class TableSkin {

	GamePanel gp;
	
	public int preset;
	private TextureRegion img1, img2;
    public int cost = 0;
    public String name = "Table Skin";
    public String description = ""; 
	public TextureRegion catalogueIcon;

	public TableSkin(GamePanel gp, int preset) {
		this.gp = gp;
		this.preset = preset;
		cost = 20;
		description = "New tables for your restaurant.";
		importImages();
	}
	public void setCatalogueIcon(TextureRegion icon) {
		this.catalogueIcon = icon;
	}
	public void importImages() {
		
		switch(preset) {
		case 0:
	        img1 = importImage("/decor/table.png").getSubimage(48, 24, 16, 40);
	        img2 = importImage("/decor/table.png").getSubimage(32, 0, 32, 24);
			break;
		case 1:
			name = "Cabin Benches";
		    img1 = importImage("/decor/table.png").getSubimage(16, 24+64, 16, 40);
		    img2 = importImage("/decor/table.png").getSubimage(0, 0+64, 32, 24);
			break;
		case 2:
			name = "Fishing Tables";
		    img1 = importImage("/decor/catalogue/fishingshack/BoatTable.png").getSubimage(16, 24+64, 16, 40);
		    img2 = importImage("/decor/catalogue/fishingshack/BoatTable.png").getSubimage(0, 0+64, 32, 24);
			break;
		case 3:
			name = "Farm Tables";
			img1 = importImage("/decor/table.png").getSubimage(48, 24+64, 16, 40);
	        img2 = importImage("/decor/table.png").getSubimage(32, 0+64, 32, 24);
			break;
		}
		
	}

	public TextureRegion getImage1() {
		return img1;
	}
	public TextureRegion getImage2() {
		return img2;
	}
	 public Texture importImage(String filePath) {
		Texture texture = AssetPool.getTexture(filePath);
		return texture;
	}
	
}
