package entity.buildings;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.TextureRegion;
import map.LightSource;

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
		
		importImages();
		mustBePlacedOnWall = true;
		turnedOn = false;
		currentAnimation = 0;
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*4, hitbox.width-3*6, hitbox.height-3*4);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*3, hitbox.y+3*4, hitbox.width-3*6, hitbox.height-3*4);
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
		animations = new TextureRegion[1][3][10];
		
		name = "Torch";
    	animations[0][0][0] = importImage("/decor/Torch.png").getSubimage(0, 0, 16, 48);
		importFromSpriteSheet("/decor/Torch.png", 4, 1, 1, 16, 0, 16, 48, 0);
		importFromSpriteSheet("/decor/TorchLight.png", 4, 1, 2, 16, 0, 16, 48, 0);
	}
	public void turnOff() {
		turnedOn = false;
		gp.world.lightingM.removeLight(light);
		currentAnimation = 0;
	}
	public void turnOn() {
		turnedOn = true;
		currentAnimation = 1;
		gp.world.lightingM.addLight(light);
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
			light = new LightSource((int) (hitbox.x + hitbox.width / 2), (int) (hitbox.y + 4),Colour.YELLOW, 32);
			light.setIntensity(1f);
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
		 	animationSpeed++; //Updating animation frame
	        if (animationSpeed == animationSpeedFactor) {
	            animationSpeed = 0;
	            animationCounter++;
	        }

	        if (animations[direction][currentAnimation][animationCounter] == null) { //If the next frame is empty
	            animationCounter = 0;
	        }
		
	     renderer.draw(animations[direction][currentAnimation][animationCounter], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
	    
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		}
	    
	}
	public void drawEmissive(Renderer renderer) {
		if(turnedOn) {
			renderer.draw(animations[direction][2][animationCounter], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		}
	}
	
	
}
