package entity.buildings;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import org.lwjgl.glfw.GLFW;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.TextureRegion;
import map.LightSource;

public class Fireplace extends Building{
	
	private LightSource light;
	private boolean firstUpdate = true;
	private boolean turnedOn = true;
	private Rectangle2D.Float interactHitbox;
	
	private boolean flickerEnabled = false;
	private float flickerTimer = 0;
	private float nextFlickerTime = 0;
	private Random random = new Random();
	
	public Fireplace(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48*2);
		
		description = "Cosy cabin lighting.";
		cost = 20;
		
		drawWidth = 48*2;
		isDecor = true;
        drawHeight = 48*3;
        hitbox.width = (48*2) - 16;
        hitbox.height = 48;
		isSolid = true;
		xDrawOffset = 8;
		yDrawOffset = 24+24+24;
		currentAnimation = 1;
		
		importImages();
		mustBePlacedOnFloor = true;
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*4, hitbox.y+3*2, hitbox.width-3*8, hitbox.height-3*6);
		interactHitbox = new Rectangle2D.Float(hitbox.x, hitbox.y, hitbox.width, hitbox.height+32);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*4, hitbox.y+3*2, hitbox.width-3*8, hitbox.height-3*6);
	}
	public Building clone() {
		Fireplace building = new Fireplace(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Fireplace(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new TextureRegion[1][3][10];
		
		name = "Fireplace";
    	animations[0][0][0] = importImage("/decor/fireplace 1.png").getSubimage(0, 0, 32, 48);
		importFromSpriteSheet("/decor/fireplace 1.png", 4, 1, 1, 0, 48, 32, 48, 0);
		importFromSpriteSheet("/decor/fireplace 1.png", 4, 1, 2, 0, 96, 32, 48, 0);
    	animations[0][0][1] = importImage("/decor/fireplace 1.png").getSubimage(32, 0, 32, 48);
	}
	public void turnOff() {
		turnedOn = false;
		gp.lightingM.removeLight(light);
		resetAnimation(0);
	}
	public void turnOn() {
		turnedOn = true;
		gp.lightingM.addLight(light);
		currentAnimation = 1;
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
	public void update(double dt) {
		super.update(dt);
		animationSpeed+=dt; //Updating animation frame
        if (animationSpeed >= animationSpeedFactor) {
            animationSpeed = 0;
            animationCounter++;
        }
        
        if (animations[0][currentAnimation][animationCounter] == null) {
            animationCounter = 0;
        }	
        
        if(gp.player.hitbox.intersects(interactHitbox)) {
        	if(gp.keyL.keyBeginPress(GLFW.GLFW_KEY_E) && gp.player.clickCounter == 0) {
	        	gp.player.clickCounter = 0.33;
	        	if(turnedOn) {
	        		turnedOn = false;
	        		turnOff();
	        	} else {
	        		turnedOn = true;
	        		turnOn();
	        	}
        	}
        }
	}
	public void draw(Renderer renderer) {
		if (firstUpdate) {
			firstUpdate = false;
			light = new LightSource((int) (hitbox.x + hitbox.width / 2), (int) (hitbox.y + hitbox.height / 2),
					Colour.RED, 110);
			light.setIntensity(0.2f);
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
		
		if(turnedOn) {
			if(gp.player.hitbox.intersects(interactHitbox)) {
				currentAnimation = 2;
	        } else {
	        	currentAnimation = 1;
	        }
			
			if (animations[0][currentAnimation][animationCounter] == null) {
				animationCounter = 0;
		    }	
	  		renderer.draw(animations[0][currentAnimation][animationCounter], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		} else {
			currentAnimation = 0;
			if(gp.player.hitbox.intersects(interactHitbox)) {
		  		renderer.draw(animations[0][0][1], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
	        } else {
		  		renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
	        }
		}

        	    
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		}
	    
	}
	
	
}
