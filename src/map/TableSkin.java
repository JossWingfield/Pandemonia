package map;

import main.GamePanel;
import main.renderer.AssetPool;
import main.renderer.Texture;
import main.renderer.TextureRegion;

public class TableSkin {

	GamePanel gp;
	
	public int preset;
	private TextureRegion img;
    public int cost = 0;
    public String name = "Table";
    public String description = ""; 

	public TableSkin(GamePanel gp, int preset) {
		this.gp = gp;
		this.preset = preset;
		cost = 36;
		description = "Give your kitchen counters a fresh look.";
		importImages();
	}
	public void importImages() {
		
		switch(preset) {
		case 0:
	        img = importImage("/decor/connected table 2.png").toTextureRegion();
			break;
		case 1:
	        img = importImage("/decor/connected table.png").toTextureRegion();
			cost = 60;
			break;
		case 2:
	        img = importImage("/decor/TableCloth.png").toTextureRegion();
			cost = 100;
			break;
		case 3:
	        img = importImage("/decor/destroyed/OldTable.png").toTextureRegion();
			break;
		}
		
	}

	public TextureRegion getImage() {
		return img.texture.getSubimage(16, 10, 16, 32);
	}
	public TextureRegion getTableImage(int x, int y, int w, int h) {
		return img.texture.getSubimage(x, y, w, h);
	}
	protected Texture importImage(String filePath) {
		Texture texture = AssetPool.getTexture(filePath);
		return texture;
	}
	
}
