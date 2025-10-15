package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.GamePanel;
import map.LightSource;
import utility.DayPhase;

public class Candle extends Building{
	
	private LightSource light;
	private boolean firstUpdate = true;
	private int type;
	
	public Candle(GamePanel gp, float xPos, float yPos, int type) {
		super(gp, xPos, yPos, 48, 48);
		this.type = type;
		
		drawWidth = 48;
        drawHeight = 48;
		isDecor = true;
		isSolid = true;
		blueprint = false;
		importImages();
		canBePlacedOnTable = true;
	}
	public Building clone() {
		Candle building = new Candle(gp, hitbox.x, hitbox.y, type);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Candle(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ", " + this.type + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new BufferedImage[1][1][1];
		
		switch(type) {
		case 0:
			name = "Candle 1";
	    	animations[0][0][0] = importImage("/decor/Candles.png").getSubimage(0, 0, 16, 16);
			break;
		case 1:
			name = "Candle 2";
	    	animations[0][0][0] = importImage("/decor/Candles.png").getSubimage(16, 0, 16, 16);
			break;
		}
	}
	public void destroy() {
		gp.lightingM.removeLight(light);
	}
	public void draw(Graphics2D g2, int xDiff, int yDiff) {
		
		if(firstUpdate) {
			firstUpdate = false;
			light = new LightSource((int)(hitbox.x+ hitbox.width/2), (int)(hitbox.y + hitbox.height/2), Color.ORANGE, 48);
			gp.lightingM.addLight(light);
		}
		

	     g2.drawImage(animations[0][0][0], (int)(hitbox.x - xDrawOffset - xDiff), (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
	    
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int)(hitbox.x - xDrawOffset - xDiff), (int) (hitbox.y - yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
	    
	}
	
	
}
