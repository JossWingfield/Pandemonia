package map;

import main.GamePanel;
import main.renderer.AssetPool;
import main.renderer.Texture;
import main.renderer.TextureRegion;

public class Beam {
	
	GamePanel gp;
	
	public int preset;
    public Tile[] tiles;
    private int arrayIndex = 0;
    public int cost = 0;
    public String name = "Beam";
    public String description = ""; 
	public TextureRegion catalogueIcon;

	public Beam(GamePanel gp, int preset) {
		this.gp = gp;
		this.preset = preset;
	    tiles = new Tile[64];
		cost = 35;
		description = "A new set of beams for the room.";
		importImages();
	}
	public void setCatalogueIcon(TextureRegion icon) {
		this.catalogueIcon = icon;
	}
	public void importImages() {
		
		switch(preset) {
		case 0:
	        importBeamFromSpriteSheet("/tiles/beams/Beam 1", 3, 6, true);
			break;
		case 1:
	        importBeamFromSpriteSheet("/tiles/beams/Beam 2", 3, 6, true);
			break;
		case 2:
	        importBeamFromSpriteSheet("/tiles/beams/Beam 3", 3, 6, true);
			break;
		case 3:
	        importBeamFromSpriteSheet("/tiles/beams/Beam 4", 3, 6, true);
			break;
		case 4:
	        importBeamFromSpriteSheet("/tiles/beams/Beam 5", 3, 6, true);
			break;
		case 5:
	        importBeamFromSpriteSheet("/decor/destroyed/OldBeam", 3, 6, true);
			break;
		case 6:
			name = "Farm Beams";
	        importBeamFromSpriteSheet("/decor/catalogue/farm/FarmBeam", 3, 6, true);
			break;
		}
		
	}
	public TextureRegion getBaseImage() {
		if(tiles[17].image == null) {
			importImages();
		}
		return tiles[17].image;
	}
	public TextureRegion getImage(int index) {
		return tiles[index - 81].image;
	}
	public Texture importImage(String filePath) {
			Texture texture = AssetPool.getTexture(filePath);
		    return texture;
	}
    private void importBeamFromSpriteSheet(String filePath, int rows, int columns, boolean solid) {
    	Texture img = importImage(filePath+".png");
        int tileSize = 16;
        for(int j = 0; j < rows; j++) {
            for(int i = 0; i < columns; i++) {
                tiles[arrayIndex] =  new Tile(gp, "", img.getSubimage(i*tileSize, j*tileSize, tileSize, tileSize));
                tiles[arrayIndex].solid = solid;
                tiles[arrayIndex].isBeam = true;
                arrayIndex++;
            }
        }
    }
	
}
