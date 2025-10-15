package entity.items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import main.GamePanel;

public class CookingItem extends Item {
	
    protected boolean cooking = false;
    protected List<String> rawIngredients;
    protected List<String> cookedResults;
    public Food cookingItem = null;
    protected int cookTime = 0;
    protected  int maxCookTime = 60*24; //60*14
    protected int flickerThreshold = 60*30;
    protected int maxBurnTime = 60*38; //60*28
    
    //BURN WARNING
	private BufferedImage orderSign, warningOrderSign, completeSign;
	private int flickerCounter = 0;
	private int flickerSpeed = 30; // frames per toggle
	
    public CookingItem(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		setupRecipes();
		orderSign = importImage("/UI/Warning.png").getSubimage(16, 0, 16, 16);
		warningOrderSign = importImage("/UI/Warning.png").getSubimage(0, 0, 16, 16);
		completeSign = importImage("/UI/Tick.png");
	}
	protected void setupRecipes() {
		
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
	public void setCooking(Item i) {
	    cooking = true;
	    cookTime = 0;
	    cookingItem = (Food)i;
	}
	public boolean isCooking() {
		return cooking;
	}
	public void updateCooking() {
		if(gp.progressM.stoveUpgradeI) {
			maxCookTime = 60*18;
			flickerThreshold = 60*24;
			maxBurnTime = 60*28;
		}
	    if (cooking) {
	        cookTime++;
	        if (cookTime >= maxCookTime) {
	            //cooking = false;
	        	if(cookingItem != null) {
		            cookingItem.foodState = FoodState.COOKED;
		            if(cookTime >= maxBurnTime) {
			            cookTime = maxBurnTime;
			            cookingItem.foodState = FoodState.BURNT;
			            cooking = false;
		            }
	        	}
	        }
	    }
	}
	public void resetImages() {
		
	}
	public void setCookingImage() {
		
	}
	public void stopCooking() {
		cooking = false;
	}
	public void setCookTime(int time) {
	    cookTime = time;
	}
	public int getCookTime() {
	    return cookTime;
	}
	public int getMaxCookTime() {
	    return maxCookTime;
	}
	public void drawCookingWarning(Graphics2D g2, int x, int xDiff, int yDiff) {
		
		 flickerCounter++;
		 if (flickerCounter >= flickerSpeed) {
			 flickerCounter = 0;
		 }
		    
		 BufferedImage currentSign = orderSign;

		 if(cookTime >= flickerThreshold) {
	 	     if (flickerCounter < flickerSpeed / 2) {
	 	    	 currentSign = orderSign;
	 	     } else {
	 	         currentSign = warningOrderSign;
	 	     }
	
	 	    g2.drawImage(currentSign,(int)(x - xDiff),(int)(hitbox.y - yDiff - 48),48, 48,null);
		} else {
	 	    g2.drawImage(completeSign,(int)(x - xDiff),(int)(hitbox.y - yDiff - 48),48, 48,null);
		}
	}
}
