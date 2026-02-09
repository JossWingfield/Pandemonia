package entity.buildings;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.glfw.GLFW;

import entity.items.Food;
import entity.items.Item;
import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;

public class Fridge extends Building {
	
	Random random;

    private static int MAX_CAPACITY = 5;

    private Rectangle2D.Float fridgeHitbox;
    private boolean firstUpdate = true;
    private double clickCooldown = 0;
    private boolean uiOpen = false;
    private Texture ui1, ui2, ui3;
    public List<Food> contents = new ArrayList<>();
    private List<Rectangle2D.Float> itemHitboxes = new ArrayList<>();

    public Fridge(GamePanel gp, float xPos, float yPos) {
        super(gp, xPos, yPos, 48, 96);

        isSolid = true;
        
        drawWidth = 16*3;
        drawHeight = 32*3;
        importImages();
        random = new Random();
		npcHitbox = new Rectangle2D.Float(hitbox.x-8, hitbox.y+hitbox.height, hitbox.width+16, 10);
		isKitchenBuilding = true;
		mustBePlacedOnFloor = true;
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*4, hitbox.width-3*3, hitbox.height-3*6);
    }
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*4, hitbox.width-3*3, hitbox.height-3*6);
	}
    public Building clone() {
        return new Fridge(gp, hitbox.x, hitbox.y);
    }
    public void printOutput() {
        System.out.println("buildings[arrayCounter] = new Fridge(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
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
    private void importImages() {
        animations = new TextureRegion[1][1][2];

        name = "Fridge";
        animations[0][0][0] = importImage("/decor/kitchen props.png").getSubimage(112, 136, 8*2, 8*4);
        animations[0][0][1] = importImage("/decor/kitchen props.png").getSubimage(112+16, 136, 8*2, 8*4);
        
        ui1 = importImage("/UI/fridge/2.png");
        ui2 = importImage("/UI/fridge/5.png");
        ui3 = importImage("/UI/fridge/Hover.png");
    }
    public Food getRandomItem() {
        if (contents.isEmpty()) return null;
        
        int index = random.nextInt(contents.size());
        return contents.remove(index);  // removes and returns the actual stored item
    }
    public void addItem(Food food) {
    	 if (contents.size() < MAX_CAPACITY) {
             contents.add(food);
         } 
    }
    public List<Food> getContents() {
        return contents;
    }
    public void updateState(double dt) {
    	super.updateState(dt);
    }
    public void inputUpdate(double dt) {
    	
    	if (clickCooldown > 0) {
    		clickCooldown -= dt;        // subtract elapsed time in seconds
		    if (clickCooldown < 0) {
		    	clickCooldown = 0;      // clamp to zero
		    }
		}
    	
        if(gp.world.progressM.fridgeUpgradeI) {
        	MAX_CAPACITY = 10;
        	if(gp.world.progressM.fridgeUpgradeII) {
        		MAX_CAPACITY = 15;
        	}
        }
        
        if(fridgeHitbox.intersects(gp.player.hitbox)) {
            if(gp.keyL.keyBeginPress(GLFW.GLFW_KEY_E) && clickCooldown == 0) {
                // If player is holding food, try to put it straight in the fridge
                if (gp.player.currentItem instanceof Food f) {
                    if (contents.size() < MAX_CAPACITY) {
                        contents.add((Food) f.clone());
                        gp.player.currentItem = null;
                        clickCooldown = 0.333; 
                        uiOpen = false; // keep fridge closed
                    } else {
                    	clickCooldown = 0.1; 
                    }
                } else {
                    uiOpen = !uiOpen;
                   	clickCooldown = 0.1; 
                }
            }
        } 
        
        if (uiOpen) {
            int baseX = (int)(hitbox.x - (112 * 1.5));
            int baseY = (int)hitbox.y;

            itemHitboxes.clear();

            float mouseX = gp.mouseL.getWorldX();
            float mouseY = gp.mouseL.getWorldY();

            int slotSize = 16 * 3;
            int padding = 1 * 3;
            int itemsPerRow = 5;
            int startX = baseX + 14 * 3;
            int startY = baseY + 16 * 3;

            for (int i = 0; i < contents.size(); i++) {
                int row = i / itemsPerRow;
                int col = i % itemsPerRow;

                int x = startX + col * (slotSize + padding);
                int y = startY + row * (slotSize + padding);

                Rectangle2D.Float hitbox = new Rectangle2D.Float(x, y, slotSize, slotSize);
                itemHitboxes.add(hitbox);
                
                if (hitbox.contains(mouseX, mouseY)) {

                    if(gp.mouseL.mouseButtonDown(0) && clickCooldown == 0) {
                        if(gp.player.currentItem == null) {
                            // TAKE OUT ITEM
                            Food food = contents.remove(i);
                            gp.player.currentItem = (Food) gp.world.itemRegistry.getItemFromName(food.getName(), food.getState());
                            clickCooldown = 0.1;
                            uiOpen = false;
                        	gp.player.resetAnimation(4);
                        } else {
                            // PUT ITEM IN (only if fridge not full)
                            if (gp.player.currentItem instanceof Food f) {
                                if (contents.size() < MAX_CAPACITY) {
                                    contents.add(f);
                                    gp.player.currentItem = null;
                                    clickCooldown = 0.1;
                                    uiOpen = false;
                                } else {
                                    clickCooldown = 0.1;
                                }
                            }
                        }
                    }
                }
            }
        }
        
    }
    public void draw(Renderer renderer) {
        if(firstUpdate) {
            firstUpdate = false;
            fridgeHitbox = new Rectangle2D.Float(hitbox.x + 18, hitbox.y + hitbox.height, 14, 16);
        }
        
		renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight); 

        if(fridgeHitbox.intersects(gp.player.hitbox)) {
            renderer.draw(animations[0][0][1], 
                (int) hitbox.x - xDrawOffset , 
                (int) (hitbox.y ) - yDrawOffset, 
                drawWidth, drawHeight);
        } else {
            uiOpen = false;
        }


        if(destructionUIOpen) {
            renderer.draw(destructionImage, 
                (int) hitbox.x - xDrawOffset , 
                (int) (hitbox.y ) - yDrawOffset, 
                gp.tileSize, gp.tileSize);
        }
    }

    public void drawOverlayUI(Renderer renderer) {
        if (uiOpen) {
            int baseX = (int)(hitbox.x - (112 * 1.5));
            int baseY = (int)hitbox.y;

            renderer.draw(ui1, baseX, baseY, 112 * 3, 112 * 3);

            itemHitboxes.clear();

            float mouseX = gp.mouseL.getWorldX();
            float mouseY = gp.mouseL.getWorldY();

            int slotSize = 16 * 3;
            int padding = 1 * 3;
            int itemsPerRow = 5;
            int startX = baseX + 14 * 3;
            int startY = baseY + 16 * 3;

            for (int i = 0; i < contents.size(); i++) {
                int row = i / itemsPerRow;
                int col = i % itemsPerRow;

                int x = startX + col * (slotSize + padding);
                int y = startY + row * (slotSize + padding);

                Rectangle2D.Float hitbox = new Rectangle2D.Float(x, y, slotSize, slotSize);
                itemHitboxes.add(hitbox);

                renderer.draw(ui2, x, y, slotSize, slotSize);

                TextureRegion itemImage = contents.get(i).getImage();
                if(contents.get(i) instanceof Food food) {
                	itemImage = food.getImage();
                }
                int iconSize = 16 * 3;
                int offset = (slotSize - iconSize) / 2;
                renderer.draw(itemImage, x + offset, y + offset, iconSize, iconSize);

                if (hitbox.contains(mouseX, mouseY)) {
                    renderer.draw(ui3, x, y, slotSize, slotSize);
                }
            }
        }
    }
}