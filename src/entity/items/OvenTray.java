package entity.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.TextureRegion;
import utility.Recipe;
import utility.RecipeManager;

public class OvenTray extends Item {
	
	private static final int MAX_LAYERS = 6;

	private List<String> ingredients = new ArrayList<>();
	private List<String> cookMethods = new ArrayList<>();
	private Map<String, Set<FoodState>> canBePutInTray = new HashMap<>();
    
    private TextureRegion foodBorder;
    private FoodState foodState = FoodState.RAW;
	
	public OvenTray(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		name = "Oven Tray";
		importImages();
		setUpStates();
	}
	
	private void importImages() {
		animations = new TextureRegion[1][1][5];
		
		animations[0][0][0] = importImage("/decor/OvenTray.png").getSubimage(0, 0, 16, 16);
		animations[0][0][1] = importImage("/decor/OvenTray.png").getSubimage(16, 0, 16, 16);
		foodBorder = importImage("/food/FoodBorder.png").toTextureRegion();
	}
	public FoodState getFoodState() {
		return foodState;
	}
	public void finishCooking() {
		if (foodState == FoodState.RAW) {
            foodState = FoodState.COOKED;
        }
	}
	private void allowInTray(String ingredientName, FoodState... allowedStates) {
	    canBePutInTray.putIfAbsent(ingredientName, new HashSet<>());
	    for (FoodState state : allowedStates) {
	        canBePutInTray.get(ingredientName).add(state);
	    }
	}
	private void setUpStates() {
	    allowInTray("Bread Slice", FoodState.RAW);
	    allowInTray("Chopped Garlic", FoodState.RAW);
	    allowInTray("Potato", FoodState.RAW);
	    allowInTray("Cheese", FoodState.CHOPPED);
	    allowInTray("Greens", FoodState.CHOPPED);
	    allowInTray("Carrot", FoodState.CHOPPED);
	    allowInTray("Aubergine", FoodState.RAW);
	    allowInTray("Lasagna", FoodState.RAW);
	}
	public boolean isEmpty() {
		return ingredients.size() == 0;
	}
	public List<Food> getFoodItems() {
	    List<Food> foodItems = new ArrayList<>();

	    for (int i = 0; i < ingredients.size(); i++) {
	        String ingredientName = ingredients.get(i);
	        if (ingredientName == null) continue;

	        // Create a fresh Food instance from the registry
	        Food food = (Food) gp.world.itemRegistry.getItemFromName(ingredientName, 0);
	        if (food == null) continue;

	        // Set cook method if it exists
	        if (i < cookMethods.size() && cookMethods.get(i) != null) {
	            food.setCookMethod(cookMethods.get(i));
	        }

	        // Determine the correct FoodState based on setup mapping
	        if (canBePutInTray.containsKey(ingredientName)) {
	            Set<FoodState> allowedStates = canBePutInTray.get(ingredientName);
	            // If the tray's current foodState matches one of the allowed states, use it
	            if (allowedStates.contains(foodState)) {
	                food.setState(foodState);
	            } else {
	                // Otherwise, default to the first allowed state
	                food.setState(allowedStates.iterator().next());
	            }
	        } else {
	            // Fallback to RAW if not in mapping
	            food.setState(FoodState.RAW);
	        }

	        foodItems.add(food);
	    }

	    return foodItems;
	}
	public void addToPlate(Plate p) {

	    for (int i = 0; i < ingredients.size(); i++) {

	        String ingredientName = ingredients.get(i);
	        if (ingredientName == null) continue;

	        // Spawn a fresh Food instance from registry
	        Food food = (Food) gp.world.itemRegistry.getItemFromName(ingredientName, 0);
	        if (food == null) continue;

	        // Apply cooked state + method
	        food.foodState = FoodState.PLATED;
	        food.addCookMethod(cookMethods.get(i));
	        food.addCookMethod("Oven Tray");

	        // Add to plate
	        p.addIngredient(food);
	    }

	    // Clear tray after plating
	    clearIngredients();
	}
	public boolean canBeAddedToTray(String foodName, FoodState state) {

	    // Tray already cooking → locked
	    if (foodState != FoodState.RAW) {
	        return false;
	    }

	    // Ingredient not supported at all
	    if (!canBePutInTray.containsKey(foodName)) {
	        return false;
	    }

	    // Food is in the wrong state
	    Set<FoodState> allowedStates = canBePutInTray.get(foodName);
	    if (!allowedStates.contains(state)) {
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
	            cookMethods.add(null);
	        }

	        // Find the next free layer starting from preferredLayer
	        int targetLayer = -1;
	        for (int i = preferredLayer; i < MAX_LAYERS; i++) {
	            if (ingredients.get(i) == null) {
	                targetLayer = i;
	                break;
	            }
	        }

	        // No free slots → do nothing
	        if (targetLayer == -1) return;

	        // Insert ingredient
	        ingredients.set(targetLayer, foodItem.name);
	        cookMethods.set(targetLayer, foodItem.getCookMethod());

	        // === Recipe check ===
	        List<String> currentIngredients = new ArrayList<>();
	        for (String ing : ingredients) {
	            if (ing != null) currentIngredients.add(ing);
	        }
	        List<String> currentMethods = new ArrayList<>();
	        for (String ing : cookMethods) {
	            if (ing != null) currentMethods.add(ing);
	        }
	        
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
	    public void clearIngredients() {
	        ingredients.clear();
	        cookMethods.clear();
	        foodState = FoodState.RAW;
	    }
	    public void draw(Renderer renderer) {
	    	if(isEmpty()) {
	            renderer.draw(animations[0][0][0], hitbox.x - xDrawOffset, hitbox.y - yDrawOffset, drawWidth, drawHeight);
	    	} else {
	            renderer.draw(animations[0][0][1], hitbox.x - xDrawOffset, hitbox.y - yDrawOffset, drawWidth, drawHeight);
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
		        float startX = (hitbox.x - xDrawOffset+24) - totalWidth / 2f;
		
		        int index = 0;
		        for (String i : ingredients) {
		            if (i != null) {
		                float x = startX + index * spacing;
		
		                renderer.draw(foodBorder, x - 4, hitbox.y - yDrawOffset - 24, 32, 32);
		
		                TextureRegion img = gp.world.itemRegistry.getImageFromName(i);
		                Food f = (Food)gp.world.itemRegistry.getItemFromName(i, 0);
		                renderer.draw(img, x-f.xDrawOffset, hitbox.y - yDrawOffset - 24, 32, 32);
		
		                index++;
		            }
		        }
	        }
	    }
	    public void drawInHand(Renderer renderer, int x, int y, boolean flip) {
	    	if(isEmpty()) {
	            renderer.draw(animations[0][0][0], x, y, drawWidth, drawHeight);
	    	} else {
	            renderer.draw(animations[0][0][1], x, y, drawWidth, drawHeight);
	    	}
	    }
}