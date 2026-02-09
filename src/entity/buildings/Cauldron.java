package entity.buildings;

import java.awt.geom.Rectangle2D;

import org.lwjgl.glfw.GLFW;

import entity.items.Item;
import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.TextureRegion;
import map.LightSource;
import utility.RecipeManager;

public class Cauldron extends Building {
	
	private LightSource light;
	private boolean isActive = false;
	private double activeCounter = 0;
	private double activeTime = 2.0;
	
	public Cauldron(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		
		isSolid = true;
		
		drawWidth = 32*3;
		drawHeight = 32*3;
		importImages();
		xDrawOffset = 24;
		yDrawOffset = 24;
		mustBePlacedOnFloor = true;
		canBePlaced = false;
		buildHitbox = new Rectangle2D.Float(hitbox.x, hitbox.y+3*2, hitbox.width, hitbox.height-3);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x, hitbox.y+3*2, hitbox.width, hitbox.height-3);
	}
	public Building clone() {
		Cauldron building = new Cauldron(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Cauldron(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new TextureRegion[1][1][2];
		
		name = "Cauldron";
    	animations[0][0][0] = importImage("/decor/Cauldron.png").getSubimage(0, 0, 32, 32);
    	animations[0][0][1] = importImage("/decor/Cauldron.png").getSubimage(32, 0, 32, 32);
	}
	public void updateState(double dt) {
		super.updateState(dt);
		if(isActive) {
			activeCounter+=dt;
			if(activeCounter >= activeTime) {
				activeCounter = 0;
				isActive = false;
				Item item = gp.world.itemRegistry.getItemFromName(RecipeManager.getCurrentHauntedIngredient(), 0);
				gp.player.currentItem = item;
				gp.world.lightingM.removeLight(light);
			}
		}
		
	}
	public void inputUpdate(double dt) {
		super.inputUpdate(dt);
		if(!isActive) {
			if(gp.player.interactHitbox.intersects(hitbox)) {
				if(gp.keyL.isKeyPressed(GLFW.GLFW_KEY_E)) {
					isActive = true;
					activeCounter = 0;
					light = new LightSource((int)(hitbox.x+ hitbox.width/2), (int)(hitbox.y + hitbox.height/2), Colour.GREEN, 48*4);
					light.setIntensity(0.5f);
					gp.world.lightingM.addLight(light);
				}
			}
		}
	}
	public void draw(Renderer renderer) {

		if(isActive) {
			renderer.draw(animations[0][0][1], (int)(hitbox.x - xDrawOffset ), (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		} else {
			renderer.draw(animations[0][0][0], (int)(hitbox.x - xDrawOffset ), (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		}
		
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int)(hitbox.x - xDrawOffset ), (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		}
	        
	}
}
