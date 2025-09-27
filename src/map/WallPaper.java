package map;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class WallPaper {
	
	GamePanel gp;
	
	public int preset;
    public Tile[] tiles;
    private int arrayIndex = 0;
    public int cost = 0;
    public String name = "Wallpaper";
    public String description = "";

	public WallPaper(GamePanel gp, int preset) {
		this.gp = gp;
		this.preset = preset;
	    tiles = new Tile[64];
		importImages();
	}
	public void importImages() {
		
		switch(preset) {
		case 0:
			importWallFromSpriteSheet("/tiles/walls/Wall18", 6, 4, true);
			break;
		case 1:
	        importWallFromSpriteSheet("/tiles/walls/Wall19", 6, 4, true);
			break;
		case 2:
	        importWallFromSpriteSheet("/tiles/walls/Wall20", 6, 4, true);
			break;
		case 3:
		    importWallFromSpriteSheet("/tiles/walls/Wall2", 6, 4, true);
			break;
		case 4:
		    importWallFromSpriteSheet("/tiles/walls/Wall10", 6, 4, true);
			break;
		case 5:
		    importWallFromSpriteSheet("/tiles/walls/StoneWall", 6, 4, true);
			break;
		case 6:
		    importWallFromSpriteSheet("/tiles/walls/Wall1", 6, 4, true);
			break;
		case 7:
		    importWallFromSpriteSheet("/tiles/walls/Wall3", 6, 4, true);
			break;
		case 8:
		    importWallFromSpriteSheet("/tiles/walls/Wall4", 6, 4, true);
			break;
		case 9:
		    importWallFromSpriteSheet("/tiles/walls/Wall5", 6, 4, true);
			break;
		case 10:
		    importWallFromSpriteSheet("/tiles/walls/Wall6", 6, 4, true);
			break;
		case 11:
		    importWallFromSpriteSheet("/tiles/walls/Wall7", 6, 4, true);
			break;
		case 12:
		    importWallFromSpriteSheet("/tiles/walls/Wall8", 6, 4, true);
			break;
		case 13:
		    importWallFromSpriteSheet("/tiles/walls/Wall9", 6, 4, true);
			break;
		case 14:
		    importWallFromSpriteSheet("/tiles/walls/Wall11", 6, 4, true);
			break;
		case 15:
		    importWallFromSpriteSheet("/tiles/walls/Wall12", 6, 4, true);
			break;
		case 16:
		    importWallFromSpriteSheet("/tiles/walls/Wall13", 6, 4, true);
			break;
		case 17:
		    importWallFromSpriteSheet("/tiles/walls/Wall14", 6, 4, true);
			break;
		case 18:
		    importWallFromSpriteSheet("/tiles/walls/Wall15", 6, 4, true);
			break;
		case 19:
		    importWallFromSpriteSheet("/tiles/walls/Wall16", 6, 4, true);
			break;
		case 20:
		    importWallFromSpriteSheet("/tiles/walls/Wall17", 6, 4, true);
			break;
		}
		
	}
	public BufferedImage getBaseImage() {
		return tiles[5].image;
	}
	public BufferedImage getImage(int index) {
		return tiles[index - 57].image;
	}
	public BufferedImage getNormalImage(int index) {
		return tiles[index - 57].normalImage;
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
    private void importWallFromSpriteSheet(String filePath, int rows, int columns, boolean solid) {
        BufferedImage img = importImage(filePath+".png");
        BufferedImage normalImg = importImage(filePath+"Normal.png");
        int tileSize = 16;
        for(int j = 0; j < rows; j++) {
            for(int i = 0; i < columns; i++) {
                tiles[arrayIndex] =  new Tile(gp, "", img.getSubimage(i*tileSize, j*tileSize, tileSize, tileSize), normalImg.getSubimage(i*tileSize, j*tileSize, tileSize, tileSize));
                tiles[arrayIndex].solid = solid;
                tiles[arrayIndex].isWall = true;
                arrayIndex++;
            }
        }
    }
	
}
