package entity.buildings;

import java.awt.geom.Rectangle2D;

import org.lwjgl.glfw.GLFW;

import entity.Player;
import entity.items.CookingItem;
import entity.items.Plate;
import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.TextureRegion;

public class Bin extends Building {
	
	private Rectangle2D.Float binHitbox;
	private boolean firstUpdate = true;
	
	public int preset;
	
	public Bin(GamePanel gp, float xPos, float yPos, int preset) {
		super(gp, xPos, yPos, 48, 48);
		this.preset = preset;
		
		isSolid = true;
		
		drawWidth = 16*3;
		drawHeight = 16*3;
		importImages();
		isSolid = false;
		isKitchenBuilding = true;
		canBePlaced = false;
		mustBePlacedOnTable = true;
		buildHitbox = new Rectangle2D.Float(hitbox.x + 3, hitbox.y+3, hitbox.width-6, hitbox.height-9);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x + 3, hitbox.y+3, hitbox.width-6, hitbox.height-9);
	}
	public Building clone() {
		Bin building = new Bin(gp, hitbox.x, hitbox.y, preset);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Bin(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ", " +this.preset + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new TextureRegion[1][1][2];
		
        name = "Bin 1";
 
    	
    	switch(preset) {
    	case 0:
    	  	animations[0][0][0] = importImage("/decor/trapdoor.png").getSubimage(0, 32, 32, 32);
        	animations[0][0][1] = importImage("/decor/trapdoor.png").getSubimage(32, 32, 32, 32);
    		break;
    	case 1:
    	  	animations[0][0][0] = importImage("/decor/trapdoor.png").getSubimage(0, 0, 32, 32);
        	animations[0][0][1] = importImage("/decor/trapdoor.png").getSubimage(32, 0, 32, 32);
    		break;
    	case 2:
    	  	animations[0][0][0] = importImage("/decor/trapdoor.png").getSubimage(0, 0, 32, 32);
        	animations[0][0][1] = importImage("/decor/trapdoor.png").getSubimage(32, 0, 32, 32);
        	
        	animations[0][0][0] = createHorizontalFlipped(animations[0][0][0]);
           	animations[0][0][1] = createHorizontalFlipped(animations[0][0][1]);
    		break;
    	}
    	
    	isSolid = false;
    	drawHeight = 48*2;
    	drawWidth = 48*2;
    	xDrawOffset = 24;
    	yDrawOffset = 24;
	}
	public void updateState(double dt) {
		super.updateState(dt);
	}
	public void inputUpdate(double dt) {
		super.inputUpdate(dt);
		
		if(binHitbox != null) {
			if(binHitbox.intersects(gp.player.interactHitbox)) {
				if(gp.keyL.keyBeginPress(GLFW.GLFW_KEY_E)) {
					if(gp.player.currentItem != null) {
						if(!(gp.player.currentItem instanceof CookingItem) && !(gp.player.currentItem instanceof Plate)) {
							gp.player.currentItem = null;
						} else if(gp.player.currentItem instanceof Plate plate) {
							if(!plate.isDirty()) {
								plate.clearIngredients();
							}
						} else if(gp.player.currentItem instanceof CookingItem pan) {
							pan.bin();
						}
					}
				}
			}
		}
	}
	public void draw(Renderer renderer) {
		
		if(firstUpdate) {
			firstUpdate = false;
			binHitbox = new Rectangle2D.Float(hitbox.x + 22, hitbox.y+10, 10, 20);
		}
		
		if(!binHitbox.intersects(gp.player.interactHitbox)) {
		     renderer.draw(animations[0][0][0], (int)(hitbox.x - xDrawOffset ), (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		} else {
			renderer.draw(animations[0][0][1], (int)(hitbox.x - xDrawOffset ), (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		}
		
		if(gp.multiplayer) {
			for(Player p: gp.getPlayerList()) {
				if(p.getUsername() != gp.player.getUsername()) {
					if(p.currentRoomIndex == gp.player.currentRoomIndex) {
						if(binHitbox.intersects(p.interactHitbox)) {
							renderer.draw(animations[0][0][1], (int)(hitbox.x - xDrawOffset ), (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
						}
					}
				}
			}
		}
	    
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int)(hitbox.x - xDrawOffset ), (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		}
	        
	}
}
