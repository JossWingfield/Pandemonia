package entity.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;

public class Fryer extends Item {
	
    protected boolean cooking = false;
    protected List<String> rawIngredients;
    protected List<String> cookedResults;
    public Food cookingItem = null;
    protected double cookTime = 0;
    protected int maxCookTime = 24;// 60*24;
    protected int flickerThreshold = 30;
    protected int maxBurnTime = 38; //60*28
    
    private int BASE_COOK_TIME = 24;
    private int BASE_FLICKER   = 30;
    private int BASE_BURN      = 38;
    protected float flickerFraction = (float) BASE_FLICKER / BASE_COOK_TIME;
    private float burnFraction    = (float) BASE_BURN / BASE_COOK_TIME;
    
    //BURN WARNING
	private TextureRegion orderSign, warningOrderSign, completeSign, burntSign;
	private int flickerCounter = 0;
	private int flickerSpeed = 30; // frames per toggle
	
	private int index = 4, animationCounter = 0;
	
	//COOKSTYLES
    protected CookStyle cookStyle = CookStyle.PASSIVE;
    
	private Map<String, Set<FoodState>> canBeFried = new HashMap<>();
	
    public Fryer(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Fryer";
		orderSign = importImage("/UI/Warning.png").getSubimage(16, 0, 16, 16);
		warningOrderSign = importImage("/UI/Warning.png").getSubimage(0, 0, 16, 16);
		completeSign = importImage("/UI/Tick.png").getSubimage(0, 0, 16, 16);
		burntSign = importImage("/UI/Tick.png").getSubimage(16, 0, 16, 16);
		importImages();
		setUp();
	}
	private void importImages() {
		animations = new TextureRegion[1][1][12];
		
		animations[0][0][0] = importImage("/decor/SingleFryer.png").getSubimage(0, 0, 16, 16);
		animations[0][0][1] = importImage("/decor/Fryer.png").getSubimage(48, 0, 48, 48);
		animations[0][0][2] = importImage("/decor/Fryer.png").getSubimage(48*2, 0, 48, 48);
		animations[0][0][3] = importImage("/decor/SingleFryer.png").getSubimage(16, 0, 16, 16);
		animations[0][0][4] = importImage("/decor/SingleFryer.png").getSubimage(16, 0, 16, 16);
		animations[0][0][5] = importImage("/decor/SingleFryer.png").getSubimage(16, 0, 16, 16);
		animations[0][0][6] = importImage("/decor/SingleFryer.png").getSubimage(16, 0, 16, 16);
		animations[0][0][8] = importImage("/decor/SingleFryer.png").getSubimage(0, 0, 16, 16);
		animations[0][0][9] = importImage("/decor/SingleFryer.png").getSubimage(0, 0, 16, 16);
		animations[0][0][10] = importImage("/decor/SingleFryer.png").getSubimage(32, 0, 16, 16);
		animations[0][0][11] = importImage("/decor/SingleFryer.png").getSubimage(32, 0, 16, 16);
	}
	private void setUp() {
		allowInFryer("Potato", FoodState.CHOPPED);
	}
	private void allowInFryer(String ingredientName, FoodState... allowedStates) {
		canBeFried.putIfAbsent(ingredientName, new HashSet<>());
	    for (FoodState state : allowedStates) {
	    	canBeFried.get(ingredientName).add(state);
	    }
	}
	public boolean canCook(Item item) {
		if(item instanceof Food food) {
			
		    // Ingredient not supported at all
		    if (!canBeFried.containsKey(food.getName())) {
		        return false;
		    }

		    // Food is in the wrong state
		    Set<FoodState> allowedStates = canBeFried.get(food.getName());
		    if (!allowedStates.contains(food.foodState)) {
		        return false;
		    }
		    
		    return true;
		}
		return false;
	}
	public void bin() {
		cookingItem = null;
		cooking = false;
		resetImages();
	}

	public String getCookedResult(String rawItemName) {
		int index = rawIngredients.indexOf(rawItemName);
	    if (index != -1) {
	    	return cookedResults.get(index);
	    }
	    return null;
	} 
	public CookStyle getCookStyle() {
		return cookStyle;
	}
	public List<String> getRawIngredients() {
		return rawIngredients;
	}
	public List<String> getCookedResults() {
		return cookedResults;
	}
	protected void animatePan(double dt) {
		if (cooking) {
	        animationCounter++;
	        if(animationCounter >= 6) {
	        	index++;
	        	animationCounter = 0;
	        	if(index == 7) {
	        		index = 3;
	        	}
	        	animations[0][0][3] = animations[0][0][index];
	        }
		}
	}
	public void setCooking(Item i) {
	    cooking = true;
	    cookTime = 0;
	    cookingItem = (Food)i;
		animations[0][0][0] = animations[0][0][4];
		maxCookTime = cookingItem.getMaxCookTime();
		flickerThreshold = Math.round(maxCookTime * flickerFraction);
		maxBurnTime = Math.round(maxCookTime * burnFraction);
		cookStyle = CookStyle.FRY;
		cookingItem.addCookMethod(name);
	}
	public boolean isCooking() {
		return cooking;
	}
	public void updateCooking(double dt) {
	    if (cooking) {
	    	
	    	updatePassive(dt);
	
	    }
	}
	private void updatePassive(double dt) {
		animatePan(dt);
        cookTime+= dt;
        if (cookTime >= maxCookTime) {
            //cooking = false;
        	if(cookingItem != null) {
	            cookingItem.foodState = FoodState.COOKED;
	            if(cookTime >= maxBurnTime) {
		            cookTime = maxBurnTime;
		            setBurntImage();
		            cookingItem.foodState = FoodState.BURNT;
		            cooking = false;
	            }
        	}
        }
	}
	protected void setBurntImage() {
    	animations[0][0][0] = animations[0][0][10];
	}
	public void resetImages() {
		animations[0][0][0] = animations[0][0][8];
	}
	public void setCookingImage() {
		animations[0][0][0] = animations[0][0][4];
	}
	public void stopCooking() {
		cooking = false;
	}
	public void setCookTime(int time) {
	    cookTime = time;
	}
	public double getCookTime() {
	    return cookTime;
	}
	public int getMaxCookTime() {
	    return cookingItem.getMaxCookTime();
	}
	public void drawCookingWarning(Renderer renderer, int x, int y) {
		
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
	
	 	    renderer.draw(currentSign,(int)(x ),(int)(y  - 48),48, 48);
		} else {
			if(cookingItem.foodState.equals(FoodState.COOKED)) {
				renderer.draw(completeSign,(int)(x ),(int)(y  - 48),48, 48);
			} else if(cookingItem.foodState.equals(FoodState.BURNT)){
				renderer.draw(burntSign,(int)(x ),(int)(y  - 48),48, 48);
			}
		}
	}
	public void drawBurntSign(Renderer renderer, int x, int y) {
		renderer.draw(burntSign,(int)(x ),(int)(y  - 48),48, 48);
	}
	public void drawCookingUI(Renderer renderer, int x, int y, boolean isLeft) {
		
		switch(cookStyle) {
		case PASSIVE:
			// Left slot cooking bar
			if(isLeft) {
				if(cookingItem != null) {
					if(cookingItem.foodState.equals(FoodState.RAW)) {
						drawPassiveCookingBar(renderer, x + 16, y + 48 + 16, getCookTime(), getMaxCookTime());
					} else if(cookingItem.foodState.equals(FoodState.BURNT)) {
						drawBurntSign(renderer, (int)(x), (int)y);
					} else {
						drawCookingWarning(renderer, (int)(x), (int)y);
					}
				}
			} else {
				if(cookingItem != null) {
					if(cookingItem.foodState.equals(FoodState.RAW)) {
						drawPassiveCookingBar(renderer, x + 48 + 30, y + 48 + 16, getCookTime(), getMaxCookTime());
					} else if(cookingItem.foodState.equals(FoodState.BURNT)) {
						drawBurntSign(renderer, (int)(x + 56), (int)y);
					} else {
						drawCookingWarning(renderer, (int)(x + 56), (int)y);
					}
				}
			}
			break;
		}
		
	}
	private void drawPassiveCookingBar(Renderer renderer, float worldX, float worldY, double cookTime, int maxCookTime) {
	    float screenX = worldX - 24 ;
	    float screenY = worldY - 72 ;

	    int barWidth = 48;
	    int barHeight = 6;
	    int xOffset = 0;
	    int yOffset = -10; // raise the bar above the pan

	    float progress = Math.min(1.0f, (float)cookTime / (float) maxCookTime);

	    // Interpolate from red to green
	    int r = (int) ((1 - progress) * 255);
	    int g = (int) (progress * 255);

	    // Optional: draw a border
	    renderer.fillRect((int) screenX + xOffset, (int) screenY + yOffset, barWidth, barHeight, Colour.BLACK);
	    
	    renderer.fillRect((int) screenX + xOffset, (int) screenY + yOffset, (int) (barWidth * progress), barHeight, new Colour(r, g, 0));
	}
	
}

