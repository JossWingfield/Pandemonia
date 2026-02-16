package entity.buildings;

import java.awt.geom.Rectangle2D;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.TextureRegion;
import map.LightSource;

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
		
		importImages();
		canBePlacedOnTable = true;
		canBePlacedOnShelf = true;
		castsShadow = false;
		buildHitbox = new Rectangle2D.Float(hitbox.x + 3*4, hitbox.y+3*4, hitbox.width-3*8, hitbox.height-3*8);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x + 3*4, hitbox.y+3*4, hitbox.width-3*8, hitbox.height-3*8);
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
		animations = new TextureRegion[1][1][1];
		
		switch(type) {
		case 0:
			name = "Candle 1";
	    	animations[0][0][0] = importImage("/decor/Candles.png").getSubimage(0, 0, 16, 16);
			cost = 8;
			break;
		case 1:
			name = "Candle 2";
	    	animations[0][0][0] = importImage("/decor/Candles.png").getSubimage(16, 0, 16, 16);
			cost = 20;
			break;
		}
		
		description = "Cosy lights for a candlelit dinner.";
		
	}
	public void destroy() {
		gp.world.lightingM.removeLight(light);
	}
	public void draw(Renderer renderer) {
		
		if(firstUpdate) {
			firstUpdate = false;
			light = new LightSource((int)(hitbox.x+ hitbox.width/2), (int)(hitbox.y + hitbox.height/2), Colour.YELLOW, 32);
	    	light.setIntensity(0.6f);
			gp.world.lightingM.addLight(light);
		}

	    renderer.draw(animations[0][0][0], (int)(hitbox.x - xDrawOffset ), (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
	    
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int)(hitbox.x - xDrawOffset ), (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		}
	    
	}
	
	
}
