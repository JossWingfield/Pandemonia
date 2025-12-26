package entity.items;

import java.util.ArrayList;

import main.GamePanel;
import main.renderer.TextureRegion;

public class SmallPan extends CookingItem {
	
	public SmallPan(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Small Pan";
		importImages();
		setupRecipes();
		xDrawOffset = 10;
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		animations[0][0][0] = importImage("/decor/CookingPots.png").getSubimage(16, 0, 16, 16);
		animations[0][0][1] = importImage("/decor/CookingPots.png").getSubimage(16, 16, 16, 16);
		animations[0][0][2] = importImage("/decor/CookingPots.png").getSubimage(0, 32, 16, 16);
		animations[0][0][3] = importImage("/decor/CookingPots.png").getSubimage(16, 0, 16, 16);
		animations[0][0][4] = importImage("/decor/CookingPots.png").getSubimage(0, 48, 16, 16);
	}
	public void refreshImages() {
		animations = gp.mapM.currentRoom.getPanSkin().getSmallPanAnimations();
	}
	public void setupRecipes() {
		rawIngredients = new ArrayList<>();
	    cookedResults = new ArrayList<>();

	    // Add ingredient-output pairs
	        
	    rawIngredients.add("Fish");
	    cookedResults.add("Cooked Fish");
	    
	    rawIngredients.add("Spaghetti");
	    cookedResults.add("Cooked Spaghetti");
	    
	    rawIngredients.add("Penne");
	    cookedResults.add("Cooked Penne");

	    // Add more recipes as needed
	}
	public void resetImages() {
		animations[0][0][0] = animations[0][0][3];
	}
	public void setCooking(Item i) {
		animations[0][0][0] = animations[0][0][1];
	    cooking = true;
	    cookTime = 0;
	    cookingItem = (Food)i;
	}
	public void setCookingImage() {
		animations[0][0][0] = animations[0][0][1];
	}
}
