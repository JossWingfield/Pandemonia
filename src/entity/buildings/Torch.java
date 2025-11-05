package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import main.GamePanel;
import map.LightSource;
import utility.DayPhase;

public class Torch extends Building{
	
	private LightSource light;
	private boolean firstUpdate = true;
	private boolean turnedOn = true;
	
	private boolean flickerEnabled = false;
	private float flickerTimer = 0;
	private float nextFlickerTime = 0;
	private Random random = new Random();
	
	public Torch(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48*2);
		
		description = "Light up your restaurant.";
		cost = 20;
		
		drawWidth = 48;
		isDecor = true;
        drawHeight = 96;
        hitbox.height = 80;
		isSolid = false;
		blueprint = false;
		importImages();
		mustBePlacedOnWall = true;
	}
	public Building clone() {
		Torch building = new Torch(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Torch(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new BufferedImage[1][1][2];
		
		name = "Lantern";
    	animations[0][0][0] = importImage("/decor/Torch.png");
    	animations[0][0][1] = importImage("/decor/Torch.png");
	}
	public void turnOff() {
		turnedOn = false;
		gp.lightingM.removeLight(light);
	}
	public void turnOn() {
		turnedOn = true;
		gp.lightingM.addLight(light);
	}
	public void destroy() {
		gp.lightingM.removeLight(light);
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

	public void draw(Graphics2D g2, int xDiff, int yDiff) {
		if (firstUpdate) {
			firstUpdate = false;
			light = new LightSource((int) (hitbox.x + hitbox.width / 2), (int) (hitbox.y + hitbox.height / 2),Color.GREEN, 40);
			light.setIntensity(0.4f);
			if (turnedOn) {
				gp.lightingM.addLight(light);
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
		
	     g2.drawImage(animations[0][0][i], (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
	    
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
	    
	}
	
	
}
