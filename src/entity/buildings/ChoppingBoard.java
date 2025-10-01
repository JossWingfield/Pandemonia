package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import entity.items.Food;
import entity.items.FoodState;
import entity.items.FryingPan;
import entity.items.Item;
import main.GamePanel;
import net.packets.Packet03PickupItem;
import net.packets.Packet12AddItemToChoppingBoard;
import net.packets.Packet13ClearPlayerHand;
import net.packets.Packet14UpdateChoppingProgress;
import net.packets.Packet15RemoveItemFromChoppingBoard;

public class ChoppingBoard extends Building {
	
    private List<String> rawIngredients;
    private List<String> choppedResults;
	
	private Food currentItem = null;
	private int clickCooldown = 0;
	private boolean chopping = false;
    private int chopCount = 0;
    private final int maxChopCount = 6;
	
	public ChoppingBoard(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		
		isSolid = true;
		blueprint = false;
		drawWidth = 16*3;
		drawHeight = 16*3;
		importImages();
		isSolid = false;
		setupRecipes();
		isKitchenBuilding = true;
		mustBePlacedOnTable = true;
		//isSecondLayer = true;
		//isThirdLayer = true;
	}
	public Building clone() {
		ChoppingBoard building = new ChoppingBoard(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new ChoppingBoard(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");
	}
	private void importImages() {
		animations = new BufferedImage[1][1][3];
		
		name = "Chopping Board 1";
    	animations[0][0][0] = importImage("/decor/ChoppingBoard.png").getSubimage(0, 0, 16, 16);
    	animations[0][0][1] = importImage("/decor/ChoppingBoard.png").getSubimage(0, 16, 16, 16);
    	animations[0][0][2] = importImage("/decor/ChoppingBoard.png").getSubimage(0, 32, 16, 16);
	}
	public void setCurrentItem(Food item) {
	    this.currentItem = item;
	    this.chopCount = 0;
	}
	public void draw(Graphics2D g2) {
		 
		g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
      		 
	    if(hitbox.intersects(gp.player.hitbox)) {
		    g2.drawImage(animations[0][0][1], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		    if(gp.keyI.ePressed) {
		    	if(clickCooldown == 0) {
			    	if(gp.player.currentItem != null) {
			    		if(canChop(gp.player.currentItem.getName())) {
			    			Food f = (Food)gp.player.currentItem;
			    			if(f.foodState == FoodState.RAW) {
					    		currentItem = (Food)gp.player.currentItem;
					    		gp.player.currentItem = null;
					    		clickCooldown = 7;
					    		if(gp.multiplayer) {
					    			Food foodItem = (Food)currentItem;
					    			Packet12AddItemToChoppingBoard packet = new Packet12AddItemToChoppingBoard(currentItem.getName(),getArrayCounter(), foodItem.getState());
					    			packet.writeData(gp.socketClient); 
					    			Packet13ClearPlayerHand packet2 = new Packet13ClearPlayerHand(gp.player.getUsername());
					    			packet2.writeData(gp.socketClient); 
					    		}
			    			}
			    		}
			    	} else {
			    		if(currentItem != null) {
				    		if(currentItem.foodState == FoodState.RAW && canChop(currentItem.getName())) {
					    		clickCooldown = 7;
					    		chopCount++;
					    		if(gp.multiplayer) {
					    			Packet14UpdateChoppingProgress packet = new Packet14UpdateChoppingProgress(gp.player.getUsername(), getArrayCounter(), chopCount);
					    			packet.writeData(gp.socketClient); 
					    		}
					    		if(chopCount == maxChopCount) {
					    			chopCount = 0;
					    			currentItem.foodState = FoodState.CHOPPED;
					    			if(currentItem.cutIntoNewItem) {
					    				Food newItem = (Food)gp.itemRegistry.getItemFromName(getChoppedResult(currentItem.getName()), 0);
					    				currentItem = newItem;
					    			}
					    		}
				    		} else {
				    			gp.player.currentItem = currentItem;
				    			gp.player.resetAnimation(4);
					    		currentItem = null;
					    		clickCooldown = 20;
					    		if(gp.multiplayer) {
					    			Packet15RemoveItemFromChoppingBoard packet = new Packet15RemoveItemFromChoppingBoard(getArrayCounter());
					    			packet.writeData(gp.socketClient); 
					    			Food foodItem = (Food)gp.player.currentItem;
					    			Packet03PickupItem packet2 = new Packet03PickupItem(gp.player.currentItem.getName(), gp.player.getUsername(), foodItem.getState());
					    			packet2.writeData(gp.socketClient); 
					    		}
				    		}
			    		} else {
				    		gp.player.currentItem = currentItem;
				    		currentItem = null;
				    		clickCooldown = 20;
			    		}
			    	}
		    	}
		    }
	    }
	    
	    if(currentItem != null) {
		    g2.drawImage(animations[0][0][2], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		    if(currentItem.foodState == FoodState.RAW) {
		    	g2.drawImage(currentItem.animations[0][0][0], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		    } else {
		    	g2.drawImage(currentItem.animations[0][0][4], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		    }
	    }
	    
	    if(clickCooldown > 0) {
	    	clickCooldown--;
	    }
	    
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
		if(currentItem != null) {
			if(currentItem.foodState == FoodState.RAW && canChop(currentItem.getName())) {
				drawChoppingBar(g2, (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, chopCount, maxChopCount);
			}
		}
	}
	public void clearCurrentItem() {
	    this.currentItem = null;
	    this.chopCount = 0;
	    this.chopping = false;
	}
	public void setChopCount(int chopCount) {
	    this.chopCount = chopCount;
	    if(chopCount == maxChopCount) {
			chopCount = 0;
			currentItem.foodState = FoodState.CHOPPED;
			if(currentItem.cutIntoNewItem) {
				Food newItem = (Food)gp.itemRegistry.getItemFromName(getChoppedResult(currentItem.getName()), 0);
				currentItem = newItem;
			}
			if(currentItem.notRawItem) {
				currentItem.foodState = FoodState.RAW;
			}
		}
	}

	private void drawChoppingBar(Graphics2D g2, float worldX, float worldY, int cookTime, int maxCookTime) {
	    float screenX = worldX - xDrawOffset - gp.player.xDiff;
	    float screenY = worldY - yDrawOffset - gp.player.yDiff;

	    int barWidth = 48;
	    int barHeight = 6;
	    int xOffset = 0;
	    int yOffset = -10; // raise the bar above the pan

	    float progress = Math.min(1.0f, cookTime / (float) maxCookTime);

	    // Interpolate from red to green
	    int r = (int) ((1 - progress) * 255);
	    int g = (int) (progress * 255);

	    // Optional: draw a border
	    g2.setColor(Color.BLACK);
	    g2.fillRect((int) screenX + xOffset, (int) screenY + yOffset, barWidth, barHeight);
	    
	    g2.setColor(new Color(r, g, 0));
	    g2.fillRect((int) screenX + xOffset, (int) screenY + yOffset, (int) (barWidth * progress), barHeight);

	}
	 private void setupRecipes() {
		 rawIngredients = new ArrayList<>();
	     choppedResults = new ArrayList<>();

	     rawIngredients.add("Cheese");
	     choppedResults.add("Chopped Cheese");
	     
	     rawIngredients.add("Bread");
	     choppedResults.add("Bread Slice");
	     
	     rawIngredients.add("Greens");
	     choppedResults.add("Chopped Greens");
	     
	     rawIngredients.add("Steak");
	     choppedResults.add("Meatball");
	     
	     rawIngredients.add("Tomato");
	     choppedResults.add("Chopped Tomatoes");
	     
	     rawIngredients.add("Chicken");
	     choppedResults.add("Chicken Pieces");
	     
	     rawIngredients.add("Cursed Greens");
	     choppedResults.add("Chopped Cursed Greens");
	}
	public boolean canChop(String itemName) {
		return rawIngredients.contains(itemName);
	}
	public String getChoppedResult(String rawItemName) {
	    int index = rawIngredients.indexOf(rawItemName);
	    if (index != -1) {
	    	return choppedResults.get(index);
	    }
	    return null;
	}
	public void setChopping() {
		//animations[0][0][0] = animations[0][0][1];
	    chopping = true;
	}
	public boolean isChopping() {
		return chopping;
	}
}