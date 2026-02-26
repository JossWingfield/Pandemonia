package entity.buildings;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import org.lwjgl.glfw.GLFW;

import entity.items.Seasoning;
import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.TextureRegion;
import map.LightSource;

public class SpiceTable extends Building{
	
	private LightSource light;
	private boolean firstUpdate = true;
	private boolean turnedOn = true;
	
	private boolean flickerEnabled = false;
	private float flickerTimer = 0;
	private float nextFlickerTime = 0;
	private Random random = new Random();
	private Rectangle2D.Float interactHitbox;
	
	public SpiceTable(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48*2);
		
		description = "Ancient table used to blend spices.";
		cost = 20;
		
     	name = "Spice Table";
    	drawWidth = 32*3;
    	drawHeight = 48*3;
    	hitbox.width = 32*3;
    	hitbox.height = 24*3;
    	yDrawOffset = 48;
    	xDrawOffset = 0;
    	canBePlaced = false;
    	mustBePlacedOnFloor = true;
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*2, hitbox.width-3*4, hitbox.height-3*4);
		castsShadow = false;
		
		importImages();
		mustBePlacedOnWall = true;
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*4, hitbox.y+3*2, hitbox.width-3*8, hitbox.height-3*6);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*4, hitbox.y+3*2, hitbox.width-3*8, hitbox.height-3*6);
	}
	public Building clone() {
		SpiceTable building = new SpiceTable(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new SpiceTable(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new TextureRegion[1][1][2];
		
    	animations[0][0][0] = importImage("/decor/SpiceTable.png").getSubimage(0, 0, 32, 48);
       	animations[0][0][1] = importImage("/decor/SpiceTable.png").getSubimage(32, 0, 32, 48);
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
	public void addSeasoning(Seasoning seasoning, float quality) {
		
	}
	public void inputUpdate(double dt) {
		super.inputUpdate(dt);
		
		if(interactHitbox != null) {
			if(gp.player.interactHitbox.intersects(interactHitbox)) {
				if(gp.player.currentItem != null) {
					if(gp.player.currentItem instanceof Seasoning) {
						if(gp.keyL.keyBeginPress(GLFW.GLFW_KEY_E)) {
							  gp.world.minigameM.startGrindingMiniGame(this, (Seasoning)gp.player.currentItem);
			                    gp.player.currentItem = null;
			                    gp.player.clickCounter = 0.1;
						}
					}
				}
			}
		}
		
	}
	public void draw(Renderer renderer) {
		if (firstUpdate) {
			firstUpdate = false;
			light = new LightSource((int) (hitbox.x + hitbox.width / 2), (int) (hitbox.y + hitbox.height / 2),
					Colour.WHITE, 80);
			light.setIntensity(0.3f);
			if (turnedOn) {
				gp.world.lightingM.addLight(light);
			}
			interactHitbox = new Rectangle2D.Float(hitbox.x + 40-8, hitbox.y-8, 32, 32);
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
		if(interactHitbox != null) {
			if(gp.player.interactHitbox.intersects(interactHitbox)) {
				i = 1;
			}
		}
		
		renderer.draw(animations[0][0][i], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);

	    //renderer.setColour(Colour.YELLOW);
	    //renderer.fillRect((int)interactHitbox.x, (int)interactHitbox.y, (int)interactHitbox.width, (int)interactHitbox.height);
		 
	    
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		}
	    
	}
	
	
}
