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
		description = "Give your restaurant a new look.";
		cost = 35;
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
		    cost = 40;
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
		case 21: //START OF COLOURED WALLS
		    importWallFromSpriteSheet("/tiles/walls/ColoredWalls", 6, 4, 0, 0, true);
			description = "Add some colour to your restaurant.";
		    cost = 40;
			break;
		case 22:
		    importWallFromSpriteSheet("/tiles/walls/ColoredWalls", 6, 4, 64*1, 0, true);
			description = "Add some colour to your restaurant.";
		    cost = 40;
			break;
		case 23:
		    importWallFromSpriteSheet("/tiles/walls/ColoredWalls", 6, 4, 64*2, 0, true);
			description = "Add some colour to your restaurant.";
		    cost = 40;
			break;
		case 24:
		    importWallFromSpriteSheet("/tiles/walls/ColoredWalls", 6, 4, 64*3, 0, true);
			description = "Add some colour to your restaurant.";
		    cost = 40;
			break;
		case 25:
		    importWallFromSpriteSheet("/tiles/walls/ColoredWalls", 6, 4, 64*4, 0, true);
			description = "Add some colour to your restaurant.";
		    cost = 40;
			break;
		case 26:
		    importWallFromSpriteSheet("/tiles/walls/ColoredWalls", 6, 4, 64*5, 0, true);
			description = "Add some colour to your restaurant.";
		    cost = 40;
			break;
		case 27:
		    importWallFromSpriteSheet("/tiles/walls/ColoredWalls", 6, 4, 64*6, 0, true);
			description = "Add some colour to your restaurant.";
		    cost = 40;
			break;
		case 28:
		    importWallFromSpriteSheet("/tiles/walls/ColoredWalls", 6, 4, 64*7, 0, true);
			description = "Add some colour to your restaurant.";
		    cost = 40;
			break;
		case 29:
		    importWallFromSpriteSheet("/tiles/walls/ColoredWalls", 6, 4, 64*8, 0, true);
		    cost = 40;
			description = "Add some colour to your restaurant.";
			break;
		case 30:
			importWallFromSpriteSheet("/decor/destroyed/OldWallpaper", 6, 4, true);
			break;
		}
		
	}
	public BufferedImage getBaseImage() {
		return tiles[5].image;
	}
	public BufferedImage getImage(int index) {
		return tiles[index - 57].image;
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
        int tileSize = 16;
        for(int j = 0; j < rows; j++) {
            for(int i = 0; i < columns; i++) {
                tiles[arrayIndex] =  new Tile(gp, "", img.getSubimage(i*tileSize, j*tileSize, tileSize, tileSize));
                tiles[arrayIndex].solid = solid;
                tiles[arrayIndex].isWall = true;
                arrayIndex++;
            }
        }
    }
    private void importWallFromSpriteSheet(String filePath, int rows, int columns, int startX, int startY, boolean solid) {
        BufferedImage img = importImage(filePath+".png");
        int tileSize = 16;
        for(int j = 0; j < rows; j++) {
            for(int i = 0; i < columns; i++) {
                tiles[arrayIndex] =  new Tile(gp, "", img.getSubimage(startX + i*tileSize, startY + j*tileSize, tileSize, tileSize));
                tiles[arrayIndex].solid = solid;
                tiles[arrayIndex].isWall = true;
                arrayIndex++;
            }
        }
    }
	
}
