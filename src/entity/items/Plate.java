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
    private List<String> cookMethods = new ArrayList<>();
    private List<String> secondaryCookMethods = new ArrayList<>();
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
        platableFoods.add("Fish");
        platableFoods.add("Egg");
        platableFoods.add("Cheese");
        platableFoods.add("Chicken");
        platableFoods.add("Greens");
        platableFoods.add("Cursed Greens");
        bypassPlateFoods.add("Chopped Tomatoes");
        bypassPlateFoods.add("Bread Slice");
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
    public List<Food> getFoodItems() {
        List<Food> foodItems = new ArrayList<>();

        for (int i = 0; i < ingredients.size(); i++) {
            String ingredientName = ingredients.get(i);
            if (ingredientName == null) continue;

            // Create a new Food instance from the ingredient name
            Food food = (Food) gp.world.itemRegistry.getItemFromName(ingredientName, 0);
            if (food == null) continue;

            // Set the cooking state
            if (i < cookMethods.size() && cookMethods.get(i) != null) {
                food.setCookMethod(cookMethods.get(i));
            }

            // Set the secondary cook method
            if (i < secondaryCookMethods.size() && secondaryCookMethods.get(i) != null) {
                food.setSecondaryCookMethod(secondaryCookMethods.get(i));
            }

            food.setState(3);

            foodItems.add(food);
        }

        return foodItems;
    }
    public void setDirty(boolean isDirty) {
    	this.isDirty = isDirty;
    	
    	 if(isDirty) {
        	 Recipe matched = RecipeManager.getMatchingRecipe(getIngredients(), getCookMethods(), getSecondaryCookMethods());
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
            cookMethods.add(null);
            secondaryCookMethods.add(null);
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
        cookMethods.set(targetLayer, foodItem.getCookMethod());
        secondaryCookMethods.set(targetLayer, foodItem.getSecondaryCookMethod());

        // === Recipe check ===
        List<String> currentIngredients = new ArrayList<>();
        for (String ing : ingredients) {
            if (ing != null) currentIngredients.add(ing);
        }
        List<String> currentMethods = new ArrayList<>();
        for (String ing : cookMethods) {
            if (ing != null) currentMethods.add(ing);
        }
        List<String> secondaryCurrentMethods = new ArrayList<>();
        for (String ing : secondaryCookMethods) {
            if (ing != null) secondaryCurrentMethods.add(ing);
        }

        matchedRecipe = RecipeManager.getMatchingRecipe(currentIngredients, currentMethods, secondaryCurrentMethods);
        matchedRecipeImage = getMatchingRecipeIgnoringSeasoning();
    }
    public TextureRegion getMatchingRecipeIgnoringSeasoning() {

        for (Recipe recipe : RecipeManager.getAllRecipes()) {

            // ----- Build filtered ingredient list from recipe -----
            List<String> requiredIngredients = new ArrayList<>(recipe.getIngredients());
            List<String> requiredMethods = new ArrayList<>(recipe.getCookingStates());
            List<String> requiredSecondaryMethods = new ArrayList<>(recipe.getSecondaryCookingStates());
            
            // Remove seasoning entries from recipe side
            for (int i = requiredIngredients.size() - 1; i >= 0; i--) {
                if (isSeasoningIngredient(requiredIngredients.get(i))) {
                    requiredIngredients.remove(i);
                    requiredMethods.remove(i);
                    requiredSecondaryMethods.remove(i);
                }
            }

            // ----- Build filtered ingredient + method lists from this plate -----
            List<String> plateIngredients = new ArrayList<>();
            List<String> plateMethods = new ArrayList<>();
            List<String> plateSecondaryMethods = new ArrayList<>();

            for (int i = 0; i < this.ingredients.size(); i++) {
                String ing = this.ingredients.get(i);

                if (ing != null && !isSeasoningIngredient(ing)) {
                    plateIngredients.add(ing);

                    // PRIMARY METHOD
                    if (i < this.cookMethods.size()) {
                        String method = this.cookMethods.get(i);

                        plateMethods.add(method);
                    } else {
                        plateMethods.add("");
                    }

                    // SECONDARY METHOD
                    if (i < this.secondaryCookMethods.size()) {
                        String secondary = this.secondaryCookMethods.get(i);

                        plateSecondaryMethods.add(secondary);
                    } else {
                        plateSecondaryMethods.add("");
                    }
                }
            }

            // ----- Now use the REAL recipe matching logic -----
            if (recipe.matches(plateIngredients, plateMethods, plateSecondaryMethods)) {
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
    public float getSeasoningQuality() {
		return seasoningQuality;
	}
    public List<String> getIngredients() {
        List<String> result = new ArrayList<>();
        for (String ing : ingredients) {
            if (ing != null) result.add(ing);
        }
        return result;
    }
    public List<String> getCookMethods() {
        List<String> result = new ArrayList<>();
        for (String ing : cookMethods) {
            if (ing != null) result.add(ing);
        }
        return result;
    }
    public List<String> getSecondaryCookMethods() {
        List<String> result = new ArrayList<>();
        for (String ing : secondaryCookMethods) {
            if (ing != null) result.add(ing);
        }
        return result;
    }
    public void clearIngredients() {
        ingredients.clear();
        ingredientImages.clear();
        cookMethods.clear();
        secondaryCookMethods.clear();
        matchedRecipe = null;
        matchedRecipeImage = null;
        seasoningQuality = -1;
    }

    public boolean isFinishedRecipe() {
        return RecipeManager.getMatchingRecipe(getIngredients(), getCookMethods(), getSecondaryCookMethods()) != null;
    }
    public void setSeasoningQuality(float quality) {
    	this.seasoningQuality = quality;
    }
    public String getFinishedRecipeName() {
        Recipe matched = RecipeManager.getMatchingRecipe(getIngredients(), getCookMethods(), getSecondaryCookMethods());
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
	
	                TextureRegion img = gp.world.itemRegistry.getImageFromName(i);
	                Food f = (Food)gp.world.itemRegistry.getItemFromName(i, 0);
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
    	 this.baseX = (int) x;
         this.baseY = (int) y;

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
}
