package map;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class TableSkin {

	GamePanel gp;
	
	public int preset;
	private BufferedImage img;
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
	        img = importImage("/decor/connected table 2.png");
			break;
		case 1:
	        img = importImage("/decor/connected table.png");
			cost = 60;
			break;
		case 2:
	        img = importImage("/decor/TableCloth.png");
			cost = 100;
			break;
		}
		
	}

	public BufferedImage getImage() {
		return img.getSubimage(16, 10, 16, 32);
	}
	public BufferedImage getTableImage(int x, int y, int w, int h) {
		return img.getSubimage(x, y, w, h);
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
