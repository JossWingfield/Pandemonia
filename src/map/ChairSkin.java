package map;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class ChairSkin {

	GamePanel gp;
	
	public int preset;
	private BufferedImage img;
    public int cost = 0;
    public String name = "Chair";
    public String description = ""; 

	public ChairSkin(GamePanel gp, int preset) {
		this.gp = gp;
		this.preset = preset;
		importImages();
		cost = 0;
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
			break;
		case 6:
	        img = importImage("/decor/chair.png").getSubimage(2+32, 73-32, 16, 16);
			break;
		case 7:
	        img = importImage("/decor/chair.png").getSubimage(2+64, 73-32, 16, 16);
			break;
		case 8:
	        img = importImage("/decor/chair.png").getSubimage(2+64+32, 73-32, 16, 16);
			break;
		case 9:
	        img = importImage("/decor/chair.png").getSubimage(2+64+64, 73-32, 16, 16);
			break;
		case 10: //BACK CHAIR
	        img = importImage("/decor/chair.png").getSubimage(2, 73-64, 16, 16);
			break;
		case 11:
	        img = importImage("/decor/chair.png").getSubimage(2+32, 73-64, 16, 16);
			break;
		case 12:
	        img = importImage("/decor/chair.png").getSubimage(2+64, 73-64, 16, 16);
			break;
		case 13: //OTHER SHAPES
	        img = importImage("/decor/chair.png").getSubimage(2, 73+32, 16, 16);
			break;
		case 14:
	        img = importImage("/decor/chair.png").getSubimage(2+32, 73+32, 16, 16);
			break;
		case 15:
	        img = importImage("/decor/chair.png").getSubimage(2+96, 73+32, 16, 16);
			break;
		}
		
	}

	public BufferedImage getImage() {
		return img;
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
	
}
