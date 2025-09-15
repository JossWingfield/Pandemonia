package utility;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Animator {
	
	private BufferedImage[][][][] animations;
	private BufferedImage[][][][] originalAnimations;
	private BufferedImage lookUpSheet, normalLookUpSheet;
	private int type;
	
	public Animator(int type) {
		this.type = type;
		getImages();
	}
	
	private void getImages() {
		String prefix = "";
		switch(type) {
		case 0:
			prefix = "headgear";
			break;
		case 1:
			prefix = "chestplate";
			break;
		case 2:
			prefix = "trousers";
			break;
		}
		animations = new BufferedImage[2][4][20][15];
        importPlayerSpriteSheet("/player/" + prefix + "/idle", 4, 1, 0, 0, 0, 80, 80); //IDLE
        importPlayerSpriteSheet("/player/" + prefix + "/run", 8, 1, 1, 0, 0, 80, 80); //RUN
        originalAnimations = animations;
	}
	
	public BufferedImage[][][] reset() {
		animations = originalAnimations;
		lookUpSheet = null;
		return animations[0];
	}
	
	public void setLookupTable(String armourName) {
		lookUpSheet = importImage("/player/" + armourName + ".png");
		normalLookUpSheet = importImage("/player/" + armourName + "Normal.png");
	}
	public boolean isLookupTableNull() {
		return lookUpSheet == null;
	}
	
	public BufferedImage getFrame(int direction, int currentAnimation, int animationCounter, boolean normal) {
		
		BufferedImage base = animations[0][direction][currentAnimation][animationCounter];
		BufferedImage lSheet = lookUpSheet;
		if(normal) {
			lSheet = normalLookUpSheet;
		}
		
		if(base == null) { 
			return null;
		}
		//DECONSTRUCT IMAGE INTO COLOURS
		Color[][] colours = new Color[base.getWidth()][base.getHeight()];
		
		for(int i = 0; i < base.getWidth(); i++) {
			for(int j = 0; j < base.getHeight(); j++) {
				int rgba = base.getRGB(i, j);
				colours[i][j] = new Color(rgba, true);
				if(colours[i][j].getRed() == 0 && colours[i][j].getGreen() == 0 && colours[i][j].getBlue() == 0) {
					colours[i][j] = new Color(0, 0, 0);
				}
			}
		}
		
		//CHANGE ALL COLOURS
		for(int i = 0; i < base.getWidth(); i++) {
			for(int j = 0; j < base.getHeight(); j++) {
				int c = lSheet.getRGB(colours[i][j].getRed(), colours[i][j].getGreen());
				colours[i][j] = new Color(c, true);
				if(colours[i][j].getRed() == 0 && colours[i][j].getGreen() == 0 && colours[i][j].getBlue() == 0) {
					colours[i][j] = new Color(0,0,0,0);
 				}
			}
		}
		
		//RECONTRUCT IMAGE
		BufferedImage reColouredImage = new BufferedImage(base.getWidth(), base.getHeight(), 2);
		for(int i = 0; i < base.getWidth(); i++) {
			for(int j = 0; j < base.getHeight(); j++) {
				reColouredImage.setRGB(i, j, colours[i][j].getRGB());
			}
		}
		
		return reColouredImage;
	}
	
	public BufferedImage getFrame(BufferedImage base, BufferedImage lSheet) {
		
		//DECONSTRUCT IMAGE INTO COLOURS
		Color[][] colours = new Color[base.getWidth()][base.getHeight()];
		
		for(int i = 0; i < base.getWidth(); i++) {
			for(int j = 0; j < base.getHeight(); j++) {
				int rgba = base.getRGB(i, j);
				colours[i][j] = new Color(rgba, true);
				if(colours[i][j].getRed() == 0 && colours[i][j].getGreen() == 0 && colours[i][j].getBlue() == 0) {
					colours[i][j] = new Color(0, 0, 0);
				}
			}
		}
		
		//CHANGE ALL COLOURS
		for(int i = 0; i < base.getWidth(); i++) {
			for(int j = 0; j < base.getHeight(); j++) {
				int c = lSheet.getRGB(colours[i][j].getRed(), colours[i][j].getGreen());
				colours[i][j] = new Color(c, true);
				if(colours[i][j].getRed() == 0 && colours[i][j].getGreen() == 0 && colours[i][j].getBlue() == 0) {
					colours[i][j] = new Color(0,0,0,0);
 				}
			}
		}
		
		//RECONTRUCT IMAGE
		BufferedImage reColouredImage = new BufferedImage(base.getWidth(), base.getHeight(), 2);
		for(int i = 0; i < base.getWidth(); i++) {
			for(int j = 0; j < base.getHeight(); j++) {
				reColouredImage.setRGB(i, j, colours[i][j].getRGB());
			}
		}
		
		return reColouredImage;
	}
	
    protected void importPlayerSpriteSheet(String filePath, int columnNumber, int rowNumber, int currentAnimation, int startX, int startY, int width, int height) {

    	for(int k = 0; k < 4; k++) {
	        int arrayIndex = 0;
	
	        BufferedImage img = importImage(filePath + ".png");
	
	        for(int i = 0; i < columnNumber; i++) {
	            for(int j = 0; j < rowNumber; j++) {
	                animations[0][k][currentAnimation][arrayIndex] = img.getSubimage(i*width + startX, j*height + startY, width, height);
	                arrayIndex++;
	            }
	        }
	        if(k > 0) {
	        	startY += height;
	        }
    	}

    }
    private void importSingleRowSpriteSheet(String filePath, int columnNumber, int rowNumber, int currentAnimation, int startX, int startY, int width, int height) {
    	for(int k = 0; k < 4; k++) {
	        int arrayIndex = 0;
	
	        BufferedImage img = importImage(filePath + ".png");
	
	        for(int i = 0; i < columnNumber; i++) {
	            for(int j = 0; j < rowNumber; j++) {
	                animations[0][k][currentAnimation][arrayIndex] = img.getSubimage(i*width + startX, j*height + startY, width, height);
	                arrayIndex++;
	            }
	        }
    	}
    }
	
	
	private BufferedImage importImage(String filePath) { //Imports and stores the image
		BufferedImage importedImage = null;
	    try {
	    	importedImage = ImageIO.read(getClass().getResourceAsStream(filePath));
	    } catch(IOException e) {
	    	e.printStackTrace();
	    }
	    return importedImage;
	   }
	

}