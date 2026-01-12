package entity.buildings;

import java.awt.geom.Rectangle2D;

import org.lwjgl.glfw.GLFW;

import entity.items.Item;
import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.TextureRegion;

public class CookStation extends Building {
	
	private Item heldItem;
	
    private Rectangle2D.Float interactHitbox;
		
	public CookStation(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		
		isSolid = true;
		
		drawWidth = 48;
		drawHeight = 48;
		//yDrawOffset = 48+48 + 8;
		//xDrawOffset = 48 + 8;
		animationSpeedFactor = 0.04;
		canBePlaced = false;
		importImages();
		buildHitbox = new Rectangle2D.Float(hitbox.x + 3*2, hitbox.y+3*2, 48-3*4, 48-3*4);
		npcHitbox = new Rectangle2D.Float(hitbox.x, hitbox.y+54, 48, 48);
		updateHitboxes();
	}
	private void updateHitboxes() {
	    interactHitbox = new Rectangle2D.Float(
	        hitbox.x,
	        hitbox.y,
	        hitbox.width,
	        hitbox.height
	    );
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x + 3*2, hitbox.y+3*2, 48-3*4, 48-3*4);
	}
	public Building clone() {
		CookStation building = new CookStation(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new CookStation(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	public Item peekItem() {
	    return heldItem;
	}
	private void importImages() {
		animations = new TextureRegion[1][1][10];
		
		name = "Cook Station";
        animations[0][0][0] = importImage("/decor/FoodStation.png").toTextureRegion();
	}

	    public boolean hasItem() {
	        return heldItem != null;
	    }

	    public Item takeItem() {
	        Item i = heldItem;
	        heldItem = null;
	        return i;
	    }

	    public void placeItem(Item item) {
	        heldItem = item;
	    }
	    private void handleItemInteraction() {
	        if (gp.player.clickCounter > 0) return;
	        if (!gp.keyL.keyBeginPress(GLFW.GLFW_KEY_E)) return;
	        if (!interactHitbox.intersects(gp.player.interactHitbox)) return;

	        // PLAYER PICKS UP ITEM
	        if (heldItem != null && gp.player.currentItem == null) {
	            gp.player.currentItem = heldItem;
	            heldItem = null;
	            gp.player.resetAnimation(4);
	            gp.player.clickCounter = 12;
	            return;
	        }

	        // PLAYER PLACES ITEM
	        if (heldItem == null && gp.player.currentItem != null) {
	            heldItem = gp.player.currentItem;
	            gp.player.currentItem = null;
	            gp.player.clickCounter = 12;
	            return;
	        }
	    }
	public void update(double dt) {
		super.update(dt);
		
		handleItemInteraction();
		
		animationSpeed+=dt; //Updating animation frame
        if (animationSpeed >= animationSpeedFactor) {
            animationSpeed = 0;
            animationCounter++;
        }

        if (animations[direction][currentAnimation][animationCounter] == null) {
            animationCounter = 0;
        }	
	}
	public void draw(Renderer renderer) {	
        
	    renderer.draw(animations[direction][currentAnimation][animationCounter], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
        
	    if (heldItem != null) {
	        renderer.draw(
	            heldItem.animations[0][0][0],
	            (int) hitbox.x - xDrawOffset,
	            (int) hitbox.y - yDrawOffset - 8,
	            48,
	            48
	        );
	    }
	    
		//int goalCol = (int)((npcHitbox.x + npcHitbox.width/2)/gp.tileSize);
        //int goalRow = (int)((npcHitbox.y + npcHitbox.height/2 - 1)/gp.tileSize); 
        
        //renderer.fillRect(goalCol*48, goalRow*48, 48, 48);
	    
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int) hitbox.x , (int) (hitbox.y ), gp.tileSize, gp.tileSize);
		}
	        
	}
}
