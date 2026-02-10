package entity.buildings;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.glfw.GLFW;

import entity.items.Bread;
import entity.items.Cheese;
import entity.items.Chicken;
import entity.items.Egg;
import entity.items.Fish;
import entity.items.Food;
import entity.items.Garlic;
import entity.items.Greens;
import entity.items.Item;
import entity.items.Penne;
import entity.items.Spaghetti;
import entity.items.Steak;
import entity.items.Tomato;
import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;
import net.packets.Packet09PickFridgeItem;

public class StorageFridge extends Building {
	
	Random random;
	
	private Rectangle2D.Float fridgeHitbox;
	private boolean firstUpdate = true;
	private double clickCooldown = 0;
	private boolean uiOpen = false;
	private Texture ui1, ui2, ui3;
	public List<Food> contents = new ArrayList<>();
	private List<Rectangle2D.Float> itemHitboxes = new ArrayList<>();
	public boolean starterFridge = false;
	
	public StorageFridge(GamePanel gp, float xPos, float yPos, boolean isStarterFridge) {
		super(gp, xPos, yPos, 48, 96);
		this.starterFridge = isStarterFridge;
		
		isSolid = true;
		
		drawWidth = 16*3;
		drawHeight = 32*3;
		importImages();
		addItems();
		random = new Random();
		npcHitbox = new Rectangle2D.Float(hitbox.x-8, hitbox.y+hitbox.height, hitbox.width+16, 24);
		isStoreBuilding = true;
		mustBePlacedOnFloor = true;
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*4, hitbox.width-3*3, hitbox.height-3*6);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*4, hitbox.width-3*3, hitbox.height-3*6);
	}
	public Building clone() {
		StorageFridge building = new StorageFridge(gp, hitbox.x, hitbox.y, starterFridge);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new StorageFridge(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + "," +starterFridge+ ");");
		System.out.println("arrayCounter++;");	
	}
	public void setContents(List<String> fridgeContents, List<Integer> fridgeContentsStates) {
		contents.clear();
		int counter = 0;
	    for(String name: fridgeContents) {
	    	Item i = gp.world.itemRegistry.getItemFromName(name, fridgeContentsStates.get(counter));
	    	contents.add((Food)i);
	    	counter++;
	    }
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
		if(starterFridge) {
			contents.add(new Tomato(gp, 0, 0));
			contents.add(new Greens(gp, 0, 0));
		} else {
			contents.add(new Garlic(gp, 0, 0));
		}

	}
	public Food getRandomItem() {
		return (Food)gp.world.itemRegistry.getItemFromName(contents.get(random.nextInt(contents.size())).getName(), 0);
	}
	private void importImages() {
		animations = new TextureRegion[1][1][2];
		
		name = "Storage Fridge";
		animations[0][0][0] = importImage("/decor/StorageFridge.png").getSubimage(0, 8, 8*2, 8*4);
		animations[0][0][1] = importImage("/decor/StorageFridge.png").getSubimage(16, 8, 8*2, 8*4);
		
		if(starterFridge) {
			animations[0][0][0] = importImage("/decor/kitchen props.png").getSubimage(112, 136, 8*2, 8*4);
		    animations[0][0][1] = importImage("/decor/kitchen props.png").getSubimage(112+16, 136, 8*2, 8*4);
		}
		ui1 = importImage("/UI/fridge/2.png");
    	ui2 = importImage("/UI/fridge/5.png");
    	ui3 = importImage("/UI/fridge/Hover.png");
	}
	public void inputUpdate(double dt) {
    	super.inputUpdate(dt);
    	if (clickCooldown > 0) {
    		clickCooldown -= dt;        // subtract elapsed time in seconds
		    if (clickCooldown < 0) {
		    	clickCooldown = 0;      // clamp to zero
		    }
		}
    	if(fridgeHitbox != null) {
	    	if(fridgeHitbox.intersects(gp.player.hitbox)) {
			    if(gp.keyL.keyBeginPress(GLFW.GLFW_KEY_E) && clickCooldown == 0) {
			    	uiOpen = !uiOpen;
			    	if(gp.player.currentItem != null) {
			    		uiOpen = false;
			    	}
			    	clickCooldown = 0.16;
			    }
			} else {
				uiOpen = false;
			}
    	}
    	
	    if (uiOpen) {
	        int baseX = (int)(hitbox.x - (112 * 1.5));
	        int baseY = (int)hitbox.y;

	        // Clear old hitboxes
	        itemHitboxes.clear();

	        // Mouse position relative to screen
	        float mouseX = gp.mouseL.getWorldX();
	        float mouseY = gp.mouseL.getWorldY();

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

	            // If hovering, draw highlight
	            if (hitbox.contains(mouseX, mouseY)) {
	                if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
	                	 if (gp.player.currentItem == null) {
	                         Food food = contents.get(i);
	                         if (gp.multiplayer) {
	                             gp.socketClient.send(new Packet09PickFridgeItem(gp.player.getUsername(), food.getName(),food.getState()));
	                             clickCooldown = 0.3;
	                             uiOpen = false;
	                         } else {
	                             applyPickup(food);
	                         }
	                     }
	                }
	            }
	        }
	    }
    }
	private void applyPickup(Food food) {
	    gp.player.currentItem =
	        gp.world.itemRegistry.getItemFromName(
	            food.getName(),
	            food.getState()
	        );

	    clickCooldown = 0.3;
	    uiOpen = false;
	    gp.player.resetAnimation(4);
	}
	public void draw(Renderer renderer) {
		
		if(firstUpdate) {
			firstUpdate = false;
			fridgeHitbox = new Rectangle2D.Float(hitbox.x + 18, hitbox.y+hitbox.height, 14, 16);
		}
		
		renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
   		 
		if(fridgeHitbox.intersects(gp.player.hitbox)) {
		    renderer.draw(animations[0][0][1], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
		}
		
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		}
	        
	}
	
	public void drawOverlayUI(Renderer renderer) {
	    if (uiOpen) {
	        int baseX = (int)(hitbox.x - (112 * 1.5));
	        int baseY = (int)hitbox.y;

	        // Draw UI background
	        renderer.draw(ui1, baseX, baseY, 112 * 3, 112 * 3);

	        // Clear old hitboxes
	        itemHitboxes.clear();

	        // Mouse position relative to screen
	        float mouseX = gp.mouseL.getWorldX();
	        float mouseY = gp.mouseL.getWorldY();

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
	            renderer.draw(ui2, x, y, slotSize, slotSize);

	            // Draw food item icon
	            TextureRegion itemImage = contents.get(i).getImage();
	            int iconSize = 16 * 3;
	            int offset = (slotSize - iconSize) / 2;
	            renderer.draw(itemImage, x + offset, y + offset, iconSize, iconSize);

	            // If hovering, draw highlight
	            if (hitbox.contains(mouseX, mouseY)) {
	                renderer.draw(ui3, x, y, slotSize, slotSize);
	            }
	        }
	    }
	}
}