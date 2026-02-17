package entity.buildings;

import java.awt.geom.Rectangle2D;

import org.lwjgl.glfw.GLFW;

import entity.items.Food;
import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;
import map.LightSource;

public class Freezer extends Building {
	
	private Rectangle2D.Float interactHitbox;
	private boolean firstUpdate = true;
	private LightSource light;
	
	public Freezer(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 80, 40);
		
		isSolid = true;
		
		drawWidth = 32*3;
		drawHeight = 32*3;
		xDrawOffset = 8;
		yDrawOffset = 48;
		castsShadow = false;
		light = new LightSource((int) (hitbox.x + hitbox.width / 2), (int) (hitbox.y + 25),Colour.BLUE, 100);
		isStoreBuilding = true;
		mustBePlacedOnFloor = true;
		importImages();
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*1, hitbox.width-3*5, hitbox.height-3*3);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*1, hitbox.width-3*5, hitbox.height-3*3);
	}
	public Building clone() {
		Freezer building = new Freezer(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Freezer(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new TextureRegion[1][1][3];
				
		name = "Freezer";
		animations[0][0][0] = importImage("/decor/FreezerBox.png").toTextureRegion();
		animations[0][0][1] = importImage("/decor/FreezerBoxHighlight.png").toTextureRegion();
		animations[0][0][2] = importImage("/decor/FreezerBoxEmissive.png").toTextureRegion();
	}
	public void inputUpdate(double dt) {
		super.inputUpdate(dt);
		if(firstUpdate) {
			firstUpdate = false;
			interactHitbox = new Rectangle2D.Float(hitbox.x + 16, hitbox.y+hitbox.height - 48, 16, 32);
			gp.world.lightingM.addLight(light);
			importImages();
		}
		
		if(interactHitbox != null) {
		if(gp.player.interactHitbox.intersects(interactHitbox)) {
			if(gp.keyL.keyBeginPress(GLFW.GLFW_KEY_E)) {
				if (gp.player.currentItem == null) {
				    String itemName = null;

				    if (itemName != null) {
				        //gp.player.currentItem = (Food) gp.world.itemRegistry.getItemFromName(itemName, 0);
				    	//gp.player.resetAnimation(4);
				    }
				}
			}
		}
		}
		
	}
	public void draw(Renderer renderer) {
		if(interactHitbox == null) {
			interactHitbox = new Rectangle2D.Float(hitbox.x + 16, hitbox.y+hitbox.height - 48, 16, 32);
		}
		
		if(gp.player.interactHitbox.intersects(interactHitbox)) {
			renderer.draw(animations[0][0][1], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		} else {
		     renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		}
		
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		}
	        
	}
	public void drawEmissive(Renderer renderer) {
		if(animations[0][0][2] != null) {
			renderer.draw(animations[0][0][2], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);		
		}
	}
}

