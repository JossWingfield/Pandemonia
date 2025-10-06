package entity.buildings;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class HerbBasket extends Building {
	
	private Rectangle2D.Float interactHitbox;
	private boolean firstUpdate = true;
	public int foodType, clickCooldown;
	private boolean openUI;
	private BufferedImage itemBorder, itemBorder2;
	
	public HerbBasket(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		
		isSolid = false;
		blueprint = false;
		drawWidth = 16*3;
		drawHeight = 32*3;
		isKitchenBuilding = true;
		mustBePlacedOnTable = true;
		importImages();
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
		animations = new BufferedImage[1][1][2];
		
		animations[0][0][1] = importImage("/decor/HerbTray.png").getSubimage(16, 0, 16, 32);;
		animations[0][0][0] = importImage("/decor/HerbTray.png").getSubimage(0, 0, 16, 32);
		
		itemBorder = importImage("/UI/ItemBorder.png").getSubimage(0, 0, 20, 20);
		itemBorder2 = importImage("/UI/ItemBorder.png").getSubimage(20, 0, 20, 20);

		yDrawOffset = 48;
	}
	public void draw(Graphics2D g2) {
		if(firstUpdate) {
			firstUpdate = false;
			interactHitbox = new Rectangle2D.Float(hitbox.x + 16, hitbox.y+hitbox.height - 48, 16, 32);
			importImages();
		}
		
		if(clickCooldown > 0) {
			clickCooldown--;
		}
				
		if(gp.player.interactHitbox.intersects(interactHitbox)) {
			g2.drawImage(animations[0][0][1], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
			if(gp.keyI.ePressed && clickCooldown == 0) {
				openUI = !openUI;
				clickCooldown = 6;
			}
		} else {
			openUI = false;
		     g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		}
		
		//g2.setColor(Color.YELLOW);
      	//g2.drawRect((int)interactHitbox.x, (int)interactHitbox.y, (int)interactHitbox.width, (int)interactHitbox.height);
      		
	    
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
	        
	}
	
	public void drawOverlayUI(Graphics2D g2) {
	    if (!openUI) return;

	    // Determine base position for UI (above the basket)
	    int basketScreenX = (int) (hitbox.x - xDrawOffset - gp.player.xDiff);
	    int basketScreenY = (int) (hitbox.y - gp.player.yDiff) - yDrawOffset + 20;

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
	    int mouseX = gp.mouseI.mouseX;
	    int mouseY = gp.mouseI.mouseY;

	    // Draw 4 borders horizontally with items inside
	    for (int i = 0; i < numSlots; i++) {
	        int x = startX + i * (slotSize + spacing);
	        int y = startY;

	        // Check hover
	        boolean hovering = mouseX >= x && mouseX <= x + slotSize &&
	                           mouseY >= y && mouseY <= y + slotSize;

	        // Choose which border to draw
	        BufferedImage currentBorder = hovering ? itemBorder2 : itemBorder;
	        g2.drawImage(currentBorder, x, y, slotSize, slotSize, null);

	        // Retrieve the herb item
	        var item = gp.itemRegistry.getItemFromName(herbNames[i], 0);
	        if (item != null && item.animations != null && item.animations[0][0][0] != null) {
	            int iconPadding = 6;
	            int iconSize = slotSize - iconPadding * 2;

	            g2.drawImage(
	                item.animations[0][0][0],
	                x + iconPadding,
	                y + iconPadding,
	                iconSize,
	                iconSize,
	                null
	            );
	        }

	        // Handle click (once per frame when hovering)
	        if (hovering && gp.mouseI.leftClickPressed) {
	            gp.player.currentItem = item.clone();
	            openUI = false;
	        }
	    }
	}
	
}
