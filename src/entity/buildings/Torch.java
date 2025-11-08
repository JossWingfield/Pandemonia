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
		
		animationSpeedFactor = 1;
		
		drawWidth = 48;
		isDecor = true;
        drawHeight = 48*3;
		currentAnimation = 1;
        hitbox.height = 80;
        yDrawOffset = 48;
		isSolid = true;
		blueprint = false;
		importImages();
		mustBePlacedOnWall = true;
		turnedOn = false;
		currentAnimation = 0;
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
		animations = new BufferedImage[1][3][10];
		
		name = "Torch";
    	animations[0][0][0] = importImage("/decor/Torch.png").getSubimage(0, 0, 16, 48);
		importFromSpriteSheet("/decor/Torch.png", 4, 1, 1, 16, 0, 16, 48, 0);
		importFromSpriteSheet("/decor/TorchLight.png", 4, 1, 2, 16, 0, 16, 48, 0);
	}
	public void turnOff() {
		turnedOn = false;
		gp.lightingM.removeLight(light);
		currentAnimation = 0;
	}
	public void turnOn() {
		turnedOn = true;
		currentAnimation = 1;
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
			light = new LightSource((int) (hitbox.x + hitbox.width / 2), (int) (hitbox.y + 4),Color.ORANGE, 32);
			light.setIntensity(1f);
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
		 	animationSpeed++; //Updating animation frame
	        if (animationSpeed == animationSpeedFactor) {
	            animationSpeed = 0;
	            animationCounter++;
	        }

	        if (animations[direction][currentAnimation][animationCounter] == null) { //If the next frame is empty
	            animationCounter = 0;
	        }
		
	     g2.drawImage(animations[direction][currentAnimation][animationCounter], (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
	    
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
	    
	}
	public void drawEmissive(Graphics2D g2, int xDiff, int yDiff) {
		if(turnedOn) {
			g2.drawImage(animations[direction][2][animationCounter], (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		}
	}
	
	
}
