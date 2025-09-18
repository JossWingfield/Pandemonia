package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import entity.items.Food;
import entity.items.FoodState;
import entity.items.Plate;
import main.GamePanel;

public class Oven extends Building {
	
	public Food currentItem;
    private List<String> rawIngredients;
    private List<String> cookedResults;
	
	public Rectangle2D.Float ovenHitbox;

	private int clickCooldown = 0;
	private boolean cooking = false;
    private int cookCount = 0;
    private final int maxCookCount = 1000;
	
	public Oven(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48);
		
		isSolid = true;
		blueprint = false;
		drawWidth = 32*3;
		drawHeight = 48*3;
		xDrawOffset = 24;
		yDrawOffset = 24 + 48;
		importImages();
		setupRecipes();
		isKitchenBuilding = true;
		mustBePlacedOnFloor = true;
	}
	public Building clone() {
		Oven building = new Oven(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Oven(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");
	}
	private void importImages() {
		animations = new BufferedImage[1][1][5];
		
		name = "Oven";
    	animations[0][0][1] = importImage("/decor/kitchen props.png").getSubimage(48, 128, 32, 48);
    	animations[0][0][0] = importImage("/decor/kitchen props.png").getSubimage(80, 128, 32, 48);
    	animations[0][0][2] = importImage("/decor/kitchen props.png").getSubimage(48, 128, 32, 48);
    	animations[0][0][3] = importImage("/decor/OvenHighlight.png").getSubimage(0, 0, 32, 48);
       	animations[0][0][4] = importImage("/decor/OvenHighlight.png").getSubimage(32, 0, 32, 48);
	
	}
	public void draw(Graphics2D g2) {
		 
	    
	    if(hitbox.intersects(gp.player.interactHitbox)) {
	    	if(currentItem == null) {
			    g2.drawImage(animations[0][0][4], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
			} else {
			    g2.drawImage(animations[0][0][3], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
			}
		    if(gp.keyI.ePressed) {
		    	if(clickCooldown == 0) {
			    	if(gp.player.currentItem != null) {
			    		if(currentItem == null) { //IF EMTPY
				    		if(canCook(gp.player.currentItem.getName())) {
				    			Food f = (Food)gp.player.currentItem;
				    			if(f.foodState == FoodState.RAW) {
						    		currentItem = (Food)gp.player.currentItem;
						    		cooking = true;
						    		gp.player.currentItem = null;
						    		clickCooldown = 7;
				    			}
				    		}
			    		} else {
			    			if(gp.player.currentItem.getName().equals("Plate")) {
			    				if(currentItem.foodState == FoodState.COOKED) {
			    					currentItem.foodState = FoodState.PLATED;
									Plate p = (Plate)gp.player.currentItem;
									p.addIngredient(currentItem);
									currentItem = null;
					    		}
			    		    }
			    		}
			    	}
		    	}
		    }
	    } else {
			if(currentItem == null) {
			    g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
			} else {
			    g2.drawImage(animations[0][0][1], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
			}
	    }
	    
	    if(clickCooldown > 0) {
	    	clickCooldown--;
	    }
	    
	    if(cooking) {
	    	if(gp.world.isPowerOn()) {
	    		updateCooking();
	    	}
	    }
	    
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
		if(currentItem != null) {
			if(currentItem.foodState == FoodState.RAW) {
				drawCookingBar(g2, (int) hitbox.x - gp.player.xDiff + 24, (int) (hitbox.y - gp.player.yDiff) + 24+48, cookCount, maxCookCount);
			}
		}
	}
	private void updateCooking() {
		if(currentItem.foodState == FoodState.RAW) {
			cookCount++;
			if(cookCount >= maxCookCount) {
				cookCount = 0;
				currentItem.foodState = FoodState.COOKED;
				cooking = false;
			}
		}
	}
	private void drawCookingBar(Graphics2D g2, float worldX, float worldY, int cookTime, int maxCookTime) {
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
		 cookedResults = new ArrayList<>();

	     rawIngredients.add("Potato");
	     cookedResults.add("Jacket Potato");
	     
		 rawIngredients.add("Chicken");
		 cookedResults.add("Cooked Chicken");
	}
	public boolean canCook(String itemName) {
		return rawIngredients.contains(itemName);
	}
	public String getCookedResult(String rawItemName) {
	    int index = rawIngredients.indexOf(rawItemName);
	    if (index != -1) {
	    	return cookedResults.get(index);
	    }
	    return null;
	}
	public void setCooking() {
		//animations[0][0][0] = animations[0][0][2];
	    cooking = true;
	}
	public boolean isCooking() {
		return cooking;
	}
}