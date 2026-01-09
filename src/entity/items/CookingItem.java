package entity.items;

import java.util.List;

import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;

public class CookingItem extends Item {
	
    protected boolean cooking = false;
    protected List<String> rawIngredients;
    protected List<String> cookedResults;
    public Food cookingItem = null;
    protected int cookTime = 0;
    protected  int maxCookTime = 60*5;// 60*24;
    protected int flickerThreshold = 60*30;
    protected int maxBurnTime = 60*38; //60*28
    
    //BURN WARNING
	private TextureRegion orderSign, warningOrderSign, completeSign;
	private int flickerCounter = 0;
	private int flickerSpeed = 30; // frames per toggle
	
    public CookingItem(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		setupRecipes();
		orderSign = importImage("/UI/Warning.png").getSubimage(16, 0, 16, 16);
		warningOrderSign = importImage("/UI/Warning.png").getSubimage(0, 0, 16, 16);
		completeSign = importImage("/UI/Tick.png").toTextureRegion();
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
	public void drawCookingWarning(Renderer renderer, int x) {
		
		 flickerCounter++;
		 if (flickerCounter >= flickerSpeed) {
			 flickerCounter = 0;
		 }
		    
		 TextureRegion currentSign = orderSign;

		 if(cookTime >= flickerThreshold) {
	 	     if (flickerCounter < flickerSpeed / 2) {
	 	    	 currentSign = orderSign;
	 	     } else {
	 	         currentSign = warningOrderSign;
	 	     }
	
	 	    renderer.draw(currentSign,(int)(x ),(int)(hitbox.y  - 48),48, 48);
		} else {
	 	    renderer.draw(completeSign,(int)(x ),(int)(hitbox.y  - 48),48, 48);
		}
	}
}
