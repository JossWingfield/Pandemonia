package entity.buildings;

import java.awt.geom.Rectangle2D;

import org.lwjgl.glfw.GLFW;

import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;

public class HerbBasket extends Building {
	
	private Rectangle2D.Float interactHitbox;
	private boolean firstUpdate = true;
	public int foodType;
	public double clickCooldown;
	private boolean openUI;
	private TextureRegion itemBorder, itemBorder2;
	
	public HerbBasket(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		
		isSolid = false;
		
		drawWidth = 16*3;
		drawHeight = 32*3;
		isKitchenBuilding = true;
		mustBePlacedOnTable = true;
		importImages();
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*4, hitbox.width-3*5, hitbox.height-3*6);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*2, hitbox.y+3*4, hitbox.width-3*5, hitbox.height-3*6);
	}
	public Building clone() {
		HerbBasket building = new HerbBasket(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new FoodStore(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ", " + this.foodType + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		name = "Herb Basket";
		animations = new TextureRegion[1][1][2];
		
		animations[0][0][1] = importImage("/decor/HerbTray.png").getSubimage(16, 0, 16, 32);;
		animations[0][0][0] = importImage("/decor/HerbTray.png").getSubimage(0, 0, 16, 32);
		
		itemBorder = importImage("/UI/ItemBorder.png").getSubimage(0, 0, 20, 20);
		itemBorder2 = importImage("/UI/ItemBorder.png").getSubimage(20, 0, 20, 20);

		yDrawOffset = 48;
	}
	public void update(double dt) {
		super.update(dt);
		if (clickCooldown > 0) {
	    	clickCooldown -= dt;        // subtract elapsed time in seconds
			if (clickCooldown < 0) {
				clickCooldown = 0;      // clamp to zero
			}
		}
	}
	public void draw(Renderer renderer) {
		if(firstUpdate) {
			firstUpdate = false;
			interactHitbox = new Rectangle2D.Float(hitbox.x + 16, hitbox.y+hitbox.height - 48, 16, 32);
			importImages();
		}
				
		if(gp.player.interactHitbox.intersects(interactHitbox)) {
			renderer.draw(animations[0][0][1], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
			if(gp.keyL.isKeyPressed(GLFW.GLFW_KEY_E) && clickCooldown == 0) {
				if(gp.player.currentItem == null) {
					openUI = !openUI;
					clickCooldown = 0.06;
				}
			}
		} else {
			openUI = false;
		     renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		}
		
		//g2.setColor(Color.YELLOW);
      	//g2.drawRect((int)interactHitbox.x, (int)interactHitbox.y, (int)interactHitbox.width, (int)interactHitbox.height);
      		
	    
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		}
	        
	}
	
	public void drawOverlayUI(Renderer renderer) {
	    if (!openUI) return;

	    // Determine base position for UI (above the basket)
	    int basketScreenX = (int) (hitbox.x - xDrawOffset );
	    int basketScreenY = (int) (hitbox.y ) - yDrawOffset + 20;

	    // Dimensions for each slot
	    int slotSize = 20 * 2;
	    int numSlots = 4;
	    int spacing = 4; // space between each slot

	    // Total width including spacing between slots
	    int totalWidth = numSlots * slotSize + (numSlots - 1) * spacing;

	    // Center the row above the basket
	    int startX = basketScreenX + (drawWidth / 2) - (totalWidth / 2);
	    int startY = basketScreenY - slotSize - 10; // 10px gap above the basket

	    // Herb names for each slot
	    String[] herbNames = { "Basil", "Rosemary", "Thyme", "Sage" };


	    // Mouse position (screen space)
	    int mouseX = (int)gp.mouseL.getWorldX();
	    int mouseY = (int)gp.mouseL.getWorldY();

	    // Draw 4 borders horizontally with items inside
	    for (int i = 0; i < numSlots; i++) {
	        int x = startX + i * (slotSize + spacing);
	        int y = startY;

	        // Check hover
	        boolean hovering = mouseX >= x && mouseX <= x + slotSize &&
	                           mouseY >= y && mouseY <= y + slotSize;

	        // Choose which border to draw
	        TextureRegion currentBorder = hovering ? itemBorder2 : itemBorder;
	        renderer.draw(currentBorder, x, y, slotSize, slotSize);

	        // Retrieve the herb item
	        var item = gp.itemRegistry.getItemFromName(herbNames[i], 0);
	        if (item != null && item.animations != null && item.animations[0][0][0] != null) {
	            int iconPadding = 6;
	            int iconSize = slotSize - iconPadding * 2;

	            renderer.draw(
	                item.animations[0][0][0],
	                x + iconPadding,
	                y + iconPadding,
	                iconSize,
	                iconSize
	                
	            );
	        }

	        // Handle click (once per frame when hovering)
	        if (hovering && gp.mouseL.mouseButtonDown(0)) {
	            gp.player.currentItem = item.clone();
	            openUI = false;
	        }
	    }
	}
	
}
