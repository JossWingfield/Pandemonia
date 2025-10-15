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
import entity.items.Food;
import entity.items.Item;
import entity.items.Steak;
import main.GamePanel;
import net.packets.Packet03PickupItem;
import net.packets.Packet12AddItemToChoppingBoard;
import net.packets.Packet13ClearPlayerHand;
import net.packets.Packet17AddItemToFridge;
import net.packets.Packet18RemoveItemFromFridge;

public class Fridge extends Building {
	
	Random random;

    private static int MAX_CAPACITY = 5;

    private Rectangle2D.Float fridgeHitbox;
    private boolean firstUpdate = true;
    private int clickCooldown = 0;
    private boolean uiOpen = false;
    private BufferedImage ui1, ui2, ui3;
    public List<Food> contents = new ArrayList<>();
    private List<Rectangle2D.Float> itemHitboxes = new ArrayList<>();

    public Fridge(GamePanel gp, float xPos, float yPos) {
        super(gp, xPos, yPos, 48, 96);

        isSolid = true;
        blueprint = false;
        drawWidth = 16*3;
        drawHeight = 32*3;
        importImages();
        random = new Random();
		npcHitbox = new Rectangle2D.Float(hitbox.x-8, hitbox.y+hitbox.height, hitbox.width+16, 10);
		isKitchenBuilding = true;
		mustBePlacedOnFloor = true;
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
    		Item i = gp.itemRegistry.getItemFromName(name, fridgeContentsStates.get(counter));
    		contents.add((Food)i);
    		counter++;
    	}
    }
    private void importImages() {
        animations = new BufferedImage[1][1][2];

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
    public void draw(Graphics2D g2, int xDiff, int yDiff) {
        if(firstUpdate) {
            firstUpdate = false;
            fridgeHitbox = new Rectangle2D.Float(hitbox.x + 18, hitbox.y + hitbox.height, 14, 16);
        }
        
        if(gp.progressM.fridgeUpgradeI) {
        	MAX_CAPACITY = 10;
        	if(gp.progressM.fridgeUpgradeII) {
        		MAX_CAPACITY = 15;
        	}
        }

		g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null); 

        if(fridgeHitbox.intersects(gp.player.hitbox)) {
            g2.drawImage(animations[0][0][1], 
                (int) hitbox.x - xDrawOffset - xDiff, 
                (int) (hitbox.y - yDiff) - yDrawOffset, 
                drawWidth, drawHeight, null);

            if(gp.keyI.ePressed && clickCooldown == 0) {
                // If player is holding food, try to put it straight in the fridge
                if (gp.player.currentItem instanceof Food f) {
                    if (contents.size() < MAX_CAPACITY) {
                        contents.add((Food) f.clone());
                        gp.player.currentItem = null;
                        clickCooldown = 20; 
                        if(gp.multiplayer) {
			    			Packet17AddItemToFridge packet = new Packet17AddItemToFridge(gp.player.getUsername(), getArrayCounter(), f.getName(), f.getState());
			    			packet.writeData(gp.socketClient); 
			    			Packet13ClearPlayerHand packet2 = new Packet13ClearPlayerHand(gp.player.getUsername());
			    			packet2.writeData(gp.socketClient); 
			    		}
                        uiOpen = false; // keep fridge closed
                    } else {
                        clickCooldown = 10;
                    }
                } else {
                    // Otherwise toggle fridge UI
                    uiOpen = !uiOpen;
                    clickCooldown = 10;
                }
            }
        } else {
            uiOpen = false;
        }

        if(clickCooldown > 0) clickCooldown--;

        if(destructionUIOpen) {
            g2.drawImage(destructionImage, 
                (int) hitbox.x - xDrawOffset - xDiff, 
                (int) (hitbox.y - yDiff) - yDrawOffset, 
                gp.tileSize, gp.tileSize, null);
        }
    }

    public void drawOverlayUI(Graphics2D g2) {
        if (uiOpen) {
            int baseX = (int)(hitbox.x - (112 * 1.5));
            int baseY = (int)hitbox.y;

            g2.drawImage(ui1, baseX, baseY, 112 * 3, 112 * 3, null);

            itemHitboxes.clear();

            float mouseX = gp.mouseI.mouseX;
            float mouseY = gp.mouseI.mouseY;

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

                g2.drawImage(ui2, x, y, slotSize, slotSize, null);

                BufferedImage itemImage = contents.get(i).animations[0][0][0];
                if(contents.get(i) instanceof Food food) {
                	itemImage = food.getImage();
                }
                int iconSize = 16 * 3;
                int offset = (slotSize - iconSize) / 2;
                g2.drawImage(itemImage, x + offset, y + offset, iconSize, iconSize, null);

                if (hitbox.contains(mouseX, mouseY)) {
                    g2.drawImage(ui3, x, y, slotSize, slotSize, null);

                    if(gp.mouseI.leftClickPressed && clickCooldown == 0) {
                        if(gp.player.currentItem == null) {
                            // TAKE OUT ITEM
                            Food food = contents.remove(i);
                            gp.player.currentItem = (Food) gp.itemRegistry.getItemFromName(food.getName(), food.getState());
                            clickCooldown = 8;
                            uiOpen = false;
                        	gp.player.resetAnimation(4);

                            if(gp.multiplayer) {
                                int state = 0;
                                if(gp.player.currentItem instanceof Food f) {
                                    state = f.getState();
                                }
                                Packet03PickupItem packet = new Packet03PickupItem(
                                    gp.player.currentItem.getName(), 
                                    gp.player.getUsername(), state);
                                packet.writeData(gp.socketClient);
                                Packet18RemoveItemFromFridge packet2 = new Packet18RemoveItemFromFridge(gp.player.getUsername(), getArrayCounter(), i);
				    			packet2.writeData(gp.socketClient); 
                            }
                        } else {
                            // PUT ITEM IN (only if fridge not full)
                            if (gp.player.currentItem instanceof Food f) {
                                if (contents.size() < MAX_CAPACITY) {
                                    contents.add(f);
                                    gp.player.currentItem = null;
                                    clickCooldown = 8; 
                                    uiOpen = false;
                                    if(gp.multiplayer) {
    					    			Packet17AddItemToFridge packet = new Packet17AddItemToFridge(gp.player.getUsername(), getArrayCounter(), f.getName(), f.getState());
    					    			packet.writeData(gp.socketClient); 
    					    			Packet13ClearPlayerHand packet2 = new Packet13ClearPlayerHand(gp.player.getUsername());
    					    			packet2.writeData(gp.socketClient); 
    					    		}
                                } else {
                                	clickCooldown = 8;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}