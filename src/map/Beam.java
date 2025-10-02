package map;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Beam {
	
	GamePanel gp;
	
	public int preset;
    public Tile[] tiles;
    private int arrayIndex = 0;
    public int cost = 0;
    public String name = "Beam";
    public String description = ""; 

	public Beam(GamePanel gp, int preset) {
		this.gp = gp;
		this.preset = preset;
	    tiles = new Tile[64];
		importImages();
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
		}
		
	}
	public BufferedImage getBaseImage() {
		if(tiles[17].image == null) {
			importImages();
		}
		return tiles[17].image;
	}
	public BufferedImage getImage(int index) {
		return tiles[index - 81].image;
	}
    protected BufferedImage importImage(String filePath) { //Imports and stores the image
        BufferedImage importedImage = null;
        try {
            importedImage = ImageIO.read(getClass().getResourceAsStream(filePath));
            //BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        } catch(IOException e) {
            e.printStackTrace();
        }
        return importedImage;
    }
    private void importBeamFromSpriteSheet(String filePath, int rows, int columns, boolean solid) {
    	BufferedImage img = importImage(filePath+".png");
        BufferedImage normalImg = importImage(filePath+"Normal.png");
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
