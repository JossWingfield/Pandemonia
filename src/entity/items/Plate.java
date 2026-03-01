package entity.items;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;
import utility.recipe.CookStep;
import utility.recipe.PreparedIngredient;
import utility.recipe.Recipe;
import utility.recipe.RecipeManager;

public class Plate extends Item {

    private int currentStackCount = 1;
    private int maxPlateStack = 3;
    
    private static final int MAX_LAYERS = 6;
    private boolean isDirty = false;
    public TextureRegion dirtyImage;

    private List<PreparedIngredient> preparedIngredients = new ArrayList<>();
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

        for (PreparedIngredient prepared : preparedIngredients) {

            if (prepared == null) continue;

            // Create new Food instance from registry
            Food food = (Food) gp.world.itemRegistry
                    .getItemFromName(prepared.getName(), 0);

            if (food == null) continue;

            // Restore performed steps
            for (CookStep step : prepared.getPerformedSteps()) {
                food.addStep(step.getStation());
            }

            // Set plated state
            food.setState(FoodState.PLATED);

            foodItems.add(food);
        }

        return foodItems;
    }
    public void setDirty(boolean isDirty) {
    	this.isDirty = isDirty;
    	
    	 if(isDirty) {
        	 Recipe matched = RecipeManager.getMatchingRecipe(getPreparedIngredients());
     		 this.dirtyImage = matched.dirtyPlate;
    	 }
    	 seasoningQuality = -1;
    }
    public boolean canBePlated(String foodName, FoodState state) {

        // Bypass foods (like Bread Slice etc)
        if (bypassPlateFoods.contains(foodName)) {

            for (PreparedIngredient pi : preparedIngredients) {
                if (pi.getName().equals(foodName)) {
                    return false; // prevent duplicate
                }
            }

            return true;
        }

        // Only allow chopped or cooked
        if (state != FoodState.CHOPPED && state != FoodState.COOKED) {
            return false;
        }

        // Must be platable food
        if (!platableFoods.contains(foodName)) {
            return false;
        }

        // Prevent duplicate ingredients
        for (PreparedIngredient pi : preparedIngredients) {
            if (pi.getName().equals(foodName)) {
                return false;
            }
        }

        return true;
    }
    public void addIngredient(Food foodItem) {

        if (preparedIngredients.size() >= MAX_LAYERS)
            return;

        // Prevent duplicate ingredients (same behaviour as before)
        for (PreparedIngredient pi : preparedIngredients) {
            if (pi.getName().equals(foodItem.name))
                return;
        }

        PreparedIngredient prepared =
                new PreparedIngredient(foodItem.name, foodItem.getPerformedSteps());

        preparedIngredients.add(prepared);
        ingredientImages.add(foodItem.getImage());

        matchedRecipe = RecipeManager.getMatchingRecipe(preparedIngredients);

        if (matchedRecipe != null) {
            System.out.println(matchedRecipe.getName());
        }
    }
    public TextureRegion getMatchingRecipeIgnoringSeasoning() {

    	/*
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
		*/
        return null;
    }
    private boolean isSeasoningIngredient(String ingredientName) {
        // Adjust these names to match your actual seasoning item names
        return ingredientName.equalsIgnoreCase("Basil")
            || ingredientName.equalsIgnoreCase("Rosemary")
            || ingredientName.equalsIgnoreCase("Sage")
            || ingredientName.equalsIgnoreCase("Thyme");
    }
    public void addSeasoning(SeasoningBlend seasoningBlend, float quality) {
    	this.seasoningQuality = quality;
    	seasoningBlend.foodState = FoodState.PLATED;
    	for(String ingredient: seasoningBlend.getIngredients()) {
    		
    	}
    	//addIngredient(seasoningBlend);
    }
    public float getSeasoningQuality() {
		return seasoningQuality;
	}
    
    public void clearIngredients() {
        preparedIngredients.clear();
        ingredientImages.clear();
        matchedRecipe = null;
        matchedRecipeImage = null;
        seasoningQuality = -1;
    }
    public List<PreparedIngredient> getPreparedIngredients() {
		return preparedIngredients;
	}
    public boolean isFinishedRecipe() {
        return RecipeManager.getMatchingRecipe(getPreparedIngredients()) != null;
    }
    public void setSeasoningQuality(float quality) {
    	this.seasoningQuality = quality;
    }
    public String getFinishedRecipeName() {
        Recipe matched = RecipeManager.getMatchingRecipe(getPreparedIngredients());
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

        if (!gp.player.interactHitbox.intersects(hitbox))
            return;

        int spacing = 32;

        // Count ingredients
        int count = preparedIngredients.size();
        if (count == 0) return;

        int totalWidth = count * spacing;

        float startX = (baseX + 24) - totalWidth / 2f;

        int index = 0;

        for (PreparedIngredient prepared : preparedIngredients) {

            if (prepared == null) continue;

            float x = startX + index * spacing;

            renderer.draw(foodBorder, x - 4, baseY - 24, 32, 32);

            // Get image using ingredient name
            TextureRegion img =
                gp.world.itemRegistry.getImageFromName(prepared.getName());

            Food f = (Food) gp.world.itemRegistry
                    .getItemFromName(prepared.getName(), 0);

            renderer.draw(img, x - f.xDrawOffset, baseY - 24, 32, 32);

            index++;
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
