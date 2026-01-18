package entity.items;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;
import utility.Recipe;
import utility.RecipeManager;

public class Plate extends Item {

    private int currentStackCount = 1;
    private int maxPlateStack = 3;
    
    private static final int MAX_LAYERS = 6;
    private boolean isDirty = false;
    public TextureRegion dirtyImage;

    private List<String> ingredients = new ArrayList<>();
    private List<TextureRegion> ingredientImages = new ArrayList<>();
    private Set<String> platableFoods = new HashSet<>();
    private Set<String> bypassPlateFoods = new HashSet<>();
    private Recipe matchedRecipe;
    private TextureRegion matchedRecipeImage, foodBorder;
    
    public float seasoningQuality = -1;
    private int baseX, baseY;

    public Plate(GamePanel gp, float xPos, float yPos) {
        super(gp, xPos, yPos);
        name = "Plate";
        importImages();
        setupPlates();
        updatePlateStack(currentStackCount);
    }
    private void setupPlates() {
        platableFoods.add("Bread");
        platableFoods.add("Fish");
        platableFoods.add("Egg");
        platableFoods.add("Cheese");
        platableFoods.add("Chicken");
        platableFoods.add("Greens");
        platableFoods.add("Cursed Greens");
        bypassPlateFoods.add("Chopped Tomatoes");
    }
    private void importImages() {
        animations = new TextureRegion[1][1][5];
        Texture sheet = importImage("/decor/kitchen props.png");

        animations[0][0][0] = sheet.getSubimage(0, 48, 16, 16);
        animations[0][0][1] = sheet.getSubimage(16, 48, 16, 16);
        animations[0][0][2] = sheet.getSubimage(32, 48, 16, 16);
        animations[0][0][3] = sheet.getSubimage(48, 48, 16, 16); 
        animations[0][0][4] = sheet.getSubimage(0, 48, 16, 16);
        dirtyImage = importImage("/food/food.png").getSubimage(0, 112, 16, 16);
        foodBorder = importImage("/food/FoodBorder.png").toTextureRegion();
    }
    private void updatePlateStack(int count) {
    	if(count < 0 || count > maxPlateStack) {
    		return;
    	}
    	currentStackCount = count;
    	switch(count) {
    	case 0:
    		//make null
    		break;
    	case 1:
    		animations[0][0][0] = animations[0][0][4];
    		break;
    	case 2:
       		animations[0][0][0] = animations[0][0][1];
    		break;
    	case 3:
       		animations[0][0][0] = animations[0][0][2];
    		break;
    	}
    }
    public void decreasePlateStack() {
    	updatePlateStack(currentStackCount-1);
    }
    public void increasePlateStack() {
    	updatePlateStack(currentStackCount+1);
    }
    public void setCurrentStackCount(int i) {
    	updatePlateStack(i);
    }
    public int getCurrentStackCount() {
    	return currentStackCount;
    }
    public boolean isDirty() {
    	return isDirty;
    }
    public void setClean() {
    	isDirty = false;
    }
    public void setDirty() {
    	isDirty = true;
    	seasoningQuality = -1;
    }
    public void setDirty(TextureRegion dirtyImage) {
    	isDirty = true;
    	if(dirtyImage != null) {
    		this.dirtyImage = dirtyImage;
    		seasoningQuality = -1;
    	}
    }
    public void setDirty(boolean isDirty) {
    	this.isDirty = isDirty;
    	
    	 if(isDirty) {
        	 Recipe matched = RecipeManager.getMatchingRecipe(getIngredients());
     		 this.dirtyImage = matched.dirtyPlate;
    	 }
    	 seasoningQuality = -1;
    }
    public boolean canBePlated(String foodName, FoodState state) {
    	
        if (bypassPlateFoods.contains(foodName)) {
        	 for (String ing : ingredients) {
                 if (ing != null && ing.equals(foodName)) {
                     return false;
                 }
             }
        	 return true;
        }
    	
        // Only allow chopped or cooked
        if (state != FoodState.CHOPPED && state != FoodState.COOKED) {
            return false;
        }

        // Only allow if food is in the platable food list
        if (!platableFoods.contains(foodName)) {
            return false;
        }

        // Prevent duplicate ingredients
        for (String ing : ingredients) {
            if (ing != null && ing.equals(foodName)) {
                return false;
            }
        }

        return true;
    }
    public void addIngredient(Food foodItem) {
        int preferredLayer = foodItem.getFoodLayer();

        if (preferredLayer < 0) preferredLayer = 0;
        if (preferredLayer >= MAX_LAYERS) return;

        // Ensure list size
        while (ingredients.size() < MAX_LAYERS) {
            ingredients.add(null);
            ingredientImages.add(null);
        }

        // Find the next free layer starting from preferredLayer
        int targetLayer = -1;
        for (int i = preferredLayer; i < MAX_LAYERS; i++) {
            if (ingredientImages.get(i) == null) {
                targetLayer = i;
                break;
            }
        }

        // No free slots → do nothing
        if (targetLayer == -1) return;

        // Insert ingredient
        ingredientImages.set(targetLayer, foodItem.getImage());
        ingredients.set(targetLayer, foodItem.name);

        // === Recipe check ===
        List<String> currentIngredients = new ArrayList<>();
        for (String ing : ingredients) {
            if (ing != null) currentIngredients.add(ing);
        }

        matchedRecipe = RecipeManager.getMatchingRecipe(currentIngredients);
        matchedRecipeImage = getMatchingRecipeIgnoringSeasoning();
    }
    public TextureRegion getMatchingRecipeIgnoringSeasoning() {
        for (Recipe recipe : RecipeManager.getAllRecipes()) {
            // Skip recipes that don't match in ingredient count (excluding seasoning)
            List<String> requiredIngredients = new ArrayList<>(recipe.getIngredients());
            requiredIngredients.removeIf(this::isSeasoningIngredient); // remove all seasonings from the recipe

            // Collect plate’s non-seasoning ingredients
            List<String> plateIngredients = new ArrayList<>();
            for (String ing : this.ingredients) {
            	if(ing != null) {
            		if (!isSeasoningIngredient(ing)) {
                		plateIngredients.add(ing);
                	}
            	}
            }

            // Compare
            if (plateIngredients.size() == requiredIngredients.size() && plateIngredients.containsAll(requiredIngredients)) {
                return recipe.finishedPlate;
            }
        }
        return null;
    }
    private boolean isSeasoningIngredient(String ingredientName) {
        // Adjust these names to match your actual seasoning item names
        return ingredientName.equalsIgnoreCase("Basil")
            || ingredientName.equalsIgnoreCase("Rosemary")
            || ingredientName.equalsIgnoreCase("Sage")
            || ingredientName.equalsIgnoreCase("Thyme");
    }
    public void addSeasoning(Seasoning seasoning, float quality) {
    	this.seasoningQuality = quality;
    	seasoning.foodState = FoodState.PLATED;
    	addIngredient(seasoning);
    }
    public List<String> getIngredients() {
        List<String> result = new ArrayList<>();
        for (String ing : ingredients) {
            if (ing != null) result.add(ing);
        }
        return result;
    }

    public void clearIngredients() {
        ingredients.clear();
        ingredientImages.clear();
        matchedRecipe = null;
        matchedRecipeImage = null;
        seasoningQuality = -1;
    }

    public boolean isFinishedRecipe() {
        return RecipeManager.getMatchingRecipe(getIngredients()) != null;
    }

    public String getFinishedRecipeName() {
        Recipe matched = RecipeManager.getMatchingRecipe(getIngredients());
        return matched != null ? matched.getName() : null;
    }
    public void draw(Renderer renderer) {
        this.baseX = (int) hitbox.x - xDrawOffset ;
        this.baseY = (int) hitbox.y - yDrawOffset ;

        if(isDirty) {
            renderer.draw(dirtyImage, baseX, baseY, drawWidth, drawHeight);
        } else {
            renderer.draw(animations[0][0][0], baseX, baseY, drawWidth, drawHeight);
        }
        
        // Draw food layers from bottom to top
        
        if (matchedRecipeImage != null) {
        	renderer.draw(matchedRecipeImage, baseX, baseY, drawWidth, drawHeight);
        }  else {
            for (int i = 0; i < ingredientImages.size(); i++) {
            	TextureRegion img = ingredientImages.get(i);
                if (img != null) {
                    renderer.draw(img, baseX, baseY, drawWidth, drawHeight);
                }
            }
        }
        
        if (seasoningQuality != -1) {
        	TextureRegion seasoning = ingredientImages.get(ingredientImages.size()-1);
            renderer.draw(seasoning, baseX, baseY, drawWidth, drawHeight);
        }

    }
    public void drawOverlay(Renderer renderer) {
    	if(gp.player.interactHitbox.intersects(hitbox)) {
	        int spacing  = 32;
	
	        // Count non-null ingredients
	        int count = 0;
	        for (String i : ingredients) {
	            if (i != null) count++;
	        }
	
	        // Total width of all items
	        int totalWidth = count * spacing;
	
	        // Starting X so the row is centered on baseX
	        float startX = (baseX+24) - totalWidth / 2f;
	
	        int index = 0;
	        for (String i : ingredients) {
	            if (i != null) {
	                float x = startX + index * spacing;
	
	                renderer.draw(foodBorder, x - 4, baseY - 24, 32, 32);
	
	                TextureRegion img = gp.itemRegistry.getImageFromName(i);
	                Food f = (Food)gp.itemRegistry.getItemFromName(i, 0);
	                renderer.draw(img, x-f.xDrawOffset, baseY - 24, 32, 32);
	
	                index++;
	            }
	        }
        }
    }
    public void drawAtPosition(Renderer renderer, int x, int y) {
        int baseX = x;
        int baseY = y;

        if(isDirty) {
            renderer.draw(dirtyImage, baseX, baseY, drawWidth, drawHeight);
        } else {
            renderer.draw(animations[0][0][0], baseX, baseY, drawWidth, drawHeight);
        }
        
        // Draw food layers from bottom to top
        
        if (matchedRecipeImage != null) {
        	renderer.draw(matchedRecipeImage, baseX, baseY, drawWidth, drawHeight);
        }  else {
            for (int i = 0; i < ingredientImages.size(); i++) {
                TextureRegion img = ingredientImages.get(i);
                if (img != null) {
                    renderer.draw(img, baseX, baseY, drawWidth, drawHeight);
                }
            }
        }
        
        if (seasoningQuality != -1) {
        	TextureRegion seasoning = ingredientImages.get(ingredientImages.size()-1);
            renderer.draw(seasoning, baseX, baseY, drawWidth, drawHeight);
        }
        
    }
    public void drawInHand(Renderer renderer, int x, int y, boolean flip) {
        int baseX = (int) x;
        int baseY = (int) y;
        
        TextureRegion base = animations[0][0][0];
        
        if(flip) {
        	base = createHorizontalFlipped(base);
        }

        if(isDirty) {
            renderer.draw(dirtyImage, baseX, baseY, drawWidth, drawHeight);
        } else {
            renderer.draw(base, baseX, baseY, drawWidth, drawHeight);
        }
        
        if (matchedRecipeImage != null) {
        	renderer.draw(matchedRecipeImage, baseX, baseY, drawWidth, drawHeight);
        } else {
            for (int i = 0; i < ingredientImages.size(); i++) {
                TextureRegion img = ingredientImages.get(i);
                if (img != null) {
                    renderer.draw(img, baseX, baseY, drawWidth, drawHeight);
                }
            }
        }
        
        if (seasoningQuality != -1) {
        	TextureRegion seasoning = ingredientImages.get(ingredientImages.size()-1);
            renderer.draw(seasoning, baseX, baseY, drawWidth, drawHeight);
        }
        
    }
}
