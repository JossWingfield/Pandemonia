package entity.buildings;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entity.items.Bread;
import entity.items.Cheese;
import entity.items.Chicken;
import entity.items.Egg;
import entity.items.Fish;
import entity.items.Food;
import entity.items.Greens;
import entity.items.Penne;
import entity.items.Spaghetti;
import entity.items.Steak;
import main.GamePanel;
import net.packets.Packet03PickupItem;

public class StorageFridge extends Building {
	
	Random random;
	
	private Rectangle2D.Float fridgeHitbox;
	public Rectangle2D.Float npcHitbox;
	private boolean firstUpdate = true;
	private int clickCooldown = 0;
	private boolean uiOpen = false;
	private BufferedImage ui1, ui2, ui3;
	private List<Food> contents = new ArrayList<>();
	private List<Rectangle2D.Float> itemHitboxes = new ArrayList<>();
	
	public StorageFridge(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 96);
		
		isSolid = true;
		blueprint = false;
		drawWidth = 16*3;
		drawHeight = 32*3;
		importImages();
		addItems();
		random = new Random();
		npcHitbox = new Rectangle2D.Float(hitbox.x-8, hitbox.y+hitbox.height, hitbox.width+16, 24);
		isStoreBuilding = true;
		mustBePlacedOnFloor = true;
	}
	public Building clone() {
		StorageFridge building = new StorageFridge(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new StorageFridge(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void addItems() {
		contents.add(new Chicken(gp, 0, 0));
		contents.add(new Cheese(gp, 0, 0));
		contents.add(new Egg(gp, 0, 0));
		contents.add(new Fish(gp, 0, 0));
		contents.add(new Bread(gp, 0, 0));
		contents.add(new Steak(gp, 0, 0));
		contents.add(new Spaghetti(gp, 0, 0));
		contents.add(new Penne(gp, 0, 0));
	}
	public Food getRandomItem() {
		return (Food)gp.itemRegistry.getItemFromName(contents.get(random.nextInt(contents.size())).getName(), 0);
	}
	private void importImages() {
		animations = new BufferedImage[1][1][2];
		
		name = "Storage Fridge";
		animations[0][0][0] = importImage("/decor/StorageFridge.png").getSubimage(0, 8, 8*2, 8*4);
		animations[0][0][1] = importImage("/decor/StorageFridge.png").getSubimage(16, 8, 8*2, 8*4);
		ui1 = importImage("/UI/fridge/2.png");
    	ui2 = importImage("/UI/fridge/5.png");
    	ui3 = importImage("/UI/fridge/Hover.png");
	}
	public void draw(Graphics2D g2) {
		
		if(firstUpdate) {
			firstUpdate = false;
			fridgeHitbox = new Rectangle2D.Float(hitbox.x + 18, hitbox.y+hitbox.height, 14, 16);
		}
		
		g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
   		 
		if(fridgeHitbox.intersects(gp.player.hitbox)) {
		    g2.drawImage(animations[0][0][1], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		    if(gp.keyI.ePressed && clickCooldown == 0) {
		    	uiOpen = !uiOpen;
		    	if(gp.player.currentItem != null) {
		    		uiOpen = false;
		    	}
		    	clickCooldown = 10;
		    }
		} else {
			uiOpen = false;
		}
		
		//g2.setColor(Color.YELLOW);
		//g2.drawRect((int)fridgeHitbox.x, (int)fridgeHitbox.y, (int)fridgeHitbox.width, (int)fridgeHitbox.height);
		
		if(clickCooldown>0) {
			clickCooldown--;
		}
	    
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
	        
	}
	
	public void drawOverlayUI(Graphics2D g2) {
	    if (uiOpen) {
	        int baseX = (int)(hitbox.x - (112 * 1.5));
	        int baseY = (int)hitbox.y;

	        // Draw UI background
	        g2.drawImage(ui1, baseX, baseY, 112 * 3, 112 * 3, null);

	        // Clear old hitboxes
	        itemHitboxes.clear();

	        // Mouse position relative to screen
	        float mouseX = gp.mouseI.mouseX;
	        float mouseY = gp.mouseI.mouseY;

	        // Draw each item slot (ui2) and item icon
	        int slotSize = 16 * 3;      // Tripled size
	        int padding = 1 * 3;        // Tripled padding
	        int itemsPerRow = 5;
	        int startX = baseX + 14 * 3;
	        int startY = baseY + 16 * 3;

	        for (int i = 0; i < contents.size(); i++) {
	            int row = i / itemsPerRow;
	            int col = i % itemsPerRow;

	            int x = startX + col * (slotSize + padding);
	            int y = startY + row * (slotSize + padding);

	            // Create and store hitbox
	            Rectangle2D.Float hitbox = new Rectangle2D.Float(x, y, slotSize, slotSize);
	            itemHitboxes.add(hitbox);

	            // Draw border slot
	            g2.drawImage(ui2, x, y, slotSize, slotSize, null);

	            // Draw food item icon
	            BufferedImage itemImage = contents.get(i).animations[0][0][0];
	            int iconSize = 16 * 3;
	            int offset = (slotSize - iconSize) / 2;
	            g2.drawImage(itemImage, x + offset, y + offset, iconSize, iconSize, null);

	            // If hovering, draw highlight
	            if (hitbox.contains(mouseX, mouseY)) {
	                g2.drawImage(ui3, x, y, slotSize, slotSize, null);
	                if(gp.mouseI.leftClickPressed && clickCooldown == 0) {
	                	if(gp.player.currentItem == null) {
	                		Food food = contents.get(i);
	                		gp.player.currentItem = gp.itemRegistry.getItemFromName(food.getName(), (food instanceof Food f) ? f.getState() : 0);
	    			    	clickCooldown = 20;
	    			    	uiOpen = false;
	    			    	gp.player.resetAnimation(4);
	    			    	if(gp.multiplayer) {
	    		    			int state = 0;
	    		    			if(gp.player.currentItem instanceof Food f) {
	    		    				state = f.getState();
	    		    			}
	    			            Packet03PickupItem packet = new Packet03PickupItem(gp.player.currentItem.getName(), gp.player.getUsername(), state);
	    			            packet.writeData(gp.socketClient);
	    		            }
	    		    	}
	                }
	            }
	        }
	    }
	}
}