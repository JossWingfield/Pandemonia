package entity.buildings;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.TextureRegion;
import map.LightSource;

public class SoulLantern extends Building{
	
	private LightSource light;
	private boolean firstUpdate = true;
	private Colour soulColour;
	
	public SoulLantern(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48*2);
		
		drawWidth = 48;
		isDecor = true;
		castsShadow = false;
        drawHeight = 96;
        hitbox.height = 80;
		isSolid = false;
		
		importImages();
		mustBePlacedOnWall = true;
		canBePlaced = false;
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*4, hitbox.y+3*2, hitbox.width-3*8, hitbox.height-3*6);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*4, hitbox.y+3*2, hitbox.width-3*8, hitbox.height-3*6);
	}
	public Building clone() {
		SoulLantern building = new SoulLantern(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new SoulLantern(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new TextureRegion[1][1][1];
		
		soulColour = new Colour(100, 166, 147);
		
		name = "Soul Lantern";
    	animations[0][0][0] = importImage("/decor/SoulLantern.png").toTextureRegion();
	}
	public void draw(Renderer renderer) {
		
		if(firstUpdate) {
			firstUpdate = false;
			light = new LightSource((int)(hitbox.x+ hitbox.width/2), (int)(hitbox.y + hitbox.height/2), soulColour, 64);
			light.setIntensity(0.8f);
			gp.world.lightingM.addLight(light);
		}
		

	     renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
	    
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		}
	    
	}
	
	
}
