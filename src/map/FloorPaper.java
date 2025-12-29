package map;

import main.GamePanel;
import main.renderer.AssetPool;
import main.renderer.Texture;
import main.renderer.TextureRegion;

public class FloorPaper {
	
	GamePanel gp;
	
	public int preset;
    public Tile[] tiles;
    private int arrayIndex = 0;
    
    public int cost = 0;
    public String name = "Flooring";
    public String description = "";
	public TextureRegion catalogueIcon;
    
	public FloorPaper(GamePanel gp, int preset) {
		this.gp = gp;
		this.preset = preset;
	    tiles = new Tile[64];
	    cost = 35;
		description = "New Flooring for your restaurant.";
		importImages();
	}
	public void setCatalogueIcon(TextureRegion icon) {
		this.catalogueIcon = icon;
	}
	public void importImages() {
		
		switch(preset) {
		case 0:
	        importFloorFromSpriteSheet("/tiles/flooring/Floor1", 8, 7, false);
			break;
		case 1:
			importFloorFromSpriteSheet("/tiles/flooring/Floor5", 8, 7, false);
			break;
		case 2:
			importFloorFromSpriteSheet("/tiles/flooring/Floor12", 8, 7, false);
			break;
		case 3:
			importFloorFromSpriteSheet("/tiles/flooring/Floor8", 8, 7, false);
			break;
		case 4:
			name = "Cabin Floor";
			importFloorFromSpriteSheet("/tiles/flooring/Floor2", 8, 7, false);
			break;
		case 5:
			name = "Shack Floor";
			importFloorFromSpriteSheet("/tiles/flooring/Floor3", 8, 7, false);
			break;
		case 6:
			importFloorFromSpriteSheet("/tiles/flooring/Floor4", 8, 7, false);
			break;
		case 7:
			name = "Farm Floor";
			importFloorFromSpriteSheet("/tiles/flooring/Floor6", 8, 7, false);
			break;
		case 8:
			importFloorFromSpriteSheet("/tiles/flooring/Floor7", 8, 7, false);
			break;
		case 9:
			importFloorFromSpriteSheet("/tiles/flooring/Floor9", 8, 7, false);
			break;
		case 10:
			importFloorFromSpriteSheet("/tiles/flooring/Floor10", 8, 7, false);
			break;
		case 11:
			importFloorFromSpriteSheet("/tiles/flooring/Floor11", 8, 7, false);
			break;
		case 12:
			importFloorFromSpriteSheet("/decor/destroyed/OldFloor", 8, 7, false);
			break;
		}
		
	}
	public TextureRegion getBaseImage() {
		if(tiles[8].image == null) {
			importImages();
		}
		return tiles[8].image;
	}
	public TextureRegion getImage(int index) {
		return tiles[index - 1].image;
	}
	protected Texture importImage(String filePath) {
			Texture texture = AssetPool.getTexture(filePath);
		    return texture;
	}
    private void importFloorFromSpriteSheet(String filePath, int rows, int columns, boolean solid) {
        Texture img = importImage(filePath+".png");
        int tileSize = 16;
        for(int j = 0; j < rows; j++) {
            for(int i = 0; i < columns; i++) {
                tiles[arrayIndex] =  new Tile(gp, "", img.getSubimage(i*tileSize, j*tileSize, tileSize, tileSize));
                tiles[arrayIndex].solid = solid;
                tiles[arrayIndex].isFloor = true;
                arrayIndex++;
            }
        }
    }
	
}
