package entity.items;

import java.util.ArrayList;

import main.GamePanel;
import main.renderer.TextureRegion;

public class FryingPan extends CookingItem {
	
	private int index = 4, animationCounter = 0;
		
	public FryingPan(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Frying Pan";
		importImages();
		setupRecipes();
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][12];
		
		animations[0][0][0] = importImage("/decor/kitchen props.png").getSubimage(112+16, 16, 16, 16);
		animations[0][0][2] = importImage("/decor/Pan.png").getSubimage(16, 0, 16, 16);
		animations[0][0][3] = importImage("/decor/PanCooking.png").getSubimage(0, 0, 16, 16);
		animations[0][0][4] = importImage("/decor/PanCooking.png").getSubimage(0, 0, 16, 16);
		animations[0][0][5] = importImage("/decor/PanCooking.png").getSubimage(16, 0, 16, 16);
		animations[0][0][6] = importImage("/decor/PanCooking.png").getSubimage(32, 0, 16, 16);
		animations[0][0][8] = importImage("/decor/kitchen props.png").getSubimage(112+16, 16, 16, 16);
		animations[0][0][9] = importImage("/decor/Pan.png").getSubimage(16, 16, 16, 16);
		animations[0][0][10] = importImage("/decor/PanBurnt.png").getSubimage(0, 0 ,16, 16);
		animations[0][0][11] = importImage("/decor/PanBurnt.png").getSubimage(16, 0 ,16, 16);
	}
	public void updateCooking() {
	    if (cooking) {
	        cookTime++;
	        animationCounter++;
	        if(animationCounter >= 6) {
	        	index++;
	        	animationCounter = 0;
	        	if(index == 7) {
	        		index = 3;
	        	}
	        	animations[0][0][3] = animations[0][0][index];
	        }
	        if (cookTime >= maxCookTime) {
	        	if(cookingItem != null) {
		            cookingItem.foodState = FoodState.COOKED;
		            if(cookTime >= maxBurnTime) {
		               	animations[0][0][0] = animations[0][0][10];
			            cookTime = maxBurnTime;
			            cookingItem.foodState = FoodState.BURNT;
			            cooking = false;
		            }
	        	}
	        }
	    }
	}
	public void resetImages() {
		animations[0][0][0] = animations[0][0][8];
	}
	public void setCooking(Item i) {
		animations[0][0][0] = animations[0][0][4];
	    cooking = true;
	    cookTime = 0;
	    cookingItem = (Food)i;
	}
	public void setCookingImage() {
		animations[0][0][0] = animations[0][0][4];
	}
	public void setupRecipes() {
		rawIngredients = new ArrayList<>();
	    cookedResults = new ArrayList<>();

	    // Add ingredient-output pairs
	    rawIngredients.add("Egg");
	    cookedResults.add("Fried Egg");
	    
	    rawIngredients.add("Steak");
	    cookedResults.add("Cooked Steak");
	    
	    rawIngredients.add("Chopped Tomatoes");
	    cookedResults.add("Tomato Sauce");
	    
	    rawIngredients.add("Meatball");
	    cookedResults.add("Cooked Meatball");

	    rawIngredients.add("Chicken Pieces");
	    cookedResults.add("Cooked Chicken Pieces");
	}	
}