package entity.buildings;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.TextureRegion;
import map.LightSource;

public class Lantern extends Building{
	
	private LightSource light;
	private boolean firstUpdate = true;
	private boolean turnedOn = true;
	
	private boolean flickerEnabled = false;
	private float flickerTimer = 0;
	private float nextFlickerTime = 0;
	private Random random = new Random();
	
	public Lantern(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48*2);
		
		description = "Light up your restaurant.";
		cost = 20;
		
		drawWidth = 48;
		isDecor = true;
        drawHeight = 96;
        hitbox.height = 80;
		isSolid = false;
		
		importImages();
		mustBePlacedOnWall = true;
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*4, hitbox.y+3*2, hitbox.width-3*8, hitbox.height-3*6);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*4, hitbox.y+3*2, hitbox.width-3*8, hitbox.height-3*6);
	}
	public Building clone() {
		Lantern building = new Lantern(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Lantern(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new TextureRegion[1][1][2];
		
		name = "Lantern";
    	animations[0][0][0] = importImage("/decor/Lantern.png").getSubimage(0, 0, 16, 32);
    	animations[0][0][1] = importImage("/decor/Lantern.png").getSubimage(16, 0, 16, 32);
	}
	public void turnOff() {
		turnedOn = false;
		gp.world.lightingM.removeLight(light);
	}
	public void turnOn() {
		if(!turnedOn) {
			gp.world.lightingM.addLight(light);
		}
		turnedOn = true;
	}
	public void destroy() {
		gp.world.lightingM.removeLight(light);
	}
	public void setFlicker(boolean enabled) {
		this.flickerEnabled = enabled;
		if (enabled) {
			flickerTimer = 0;
			nextFlickerTime = random.nextFloat() * 0.5f + 0.1f; // random time before first flicker
		}
		if(!flickerEnabled) {
			light.setIntensity(0.4f);
		}
	}

	public void draw(Renderer renderer) {
		if (firstUpdate) {
			firstUpdate = false;
			light = new LightSource((int) (hitbox.x + hitbox.width / 2), (int) (hitbox.y + hitbox.height / 2),
					Colour.ORANGE, 240);
			light.setIntensity(0.4f);
			if (turnedOn) {
				gp.world.lightingM.addLight(light);
			}
		}
		
		if (flickerEnabled && turnedOn) {
			flickerTimer += 0.02f;
			
			if (flickerTimer >= nextFlickerTime) {
				// briefly turn off or dim the light
				if (random.nextFloat() < 0.5f) {
					light.setIntensity(0.0f + random.nextFloat() * 0.3f); // dim randomly
				} else {
					light.setIntensity(0.4f); // restore normal brightness
				}
				
				// reset flicker timer
				flickerTimer = 0;
				nextFlickerTime = random.nextFloat() * 0.05f + 0.01f; // next flicker between 0.05s–0.45s
			}
		}
		
		int i = 0;
		if(!turnedOn) {
			i = 1;
		}
		if(light.getIntensity() < 0.4f) {
			i = 1;
		}
		
	     renderer.draw(animations[0][0][i], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
	    
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		}
	    
	}
	
	
}
