package entity.items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import main.GamePanel;
import utility.Recipe;
import utility.RecipeManager;

public class Plate extends Item {

    private int currentStackCount = 1;
    private int maxPlateStack = 3;
    
    private static final int MAX_LAYERS = 5;
    private boolean isDirty = false;
    public BufferedImage dirtyImage;

    private List<String> ingredients = new ArrayList<>();
    private List<BufferedImage> ingredientImages = new ArrayList<>();
    private Set<String> platableFoods = new HashSet<>();
    private Set<String> bypassPlateFoods = new HashSet<>();
    private Recipe matchedRecipe;

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
        animations = new BufferedImage[1][1][5];
        BufferedImage sheet = importImage("/decor/kitchen props.png");

        animations[0][0][0] = sheet.getSubimage(0, 48, 16, 16);
        animations[0][0][1] = sheet.getSubimage(16, 48, 16, 16);
        animations[0][0][2] = sheet.getSubimage(32, 48, 16, 16);
        animations[0][0][3] = sheet.getSubimage(48, 48, 16, 16); 
        animations[0][0][4] = sheet.getSubimage(0, 48, 16, 16);
        dirtyImage = importImage("/food/food.png").getSubimage(0, 112, 16, 16);
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
    }
    public void setDirty(BufferedImage dirtyImage) {
    	isDirty = true;
    	if(dirtyImage != null) {
    		this.dirtyImage = dirtyImage;
    	}
    }
    public void setDirty(boolean isDirty) {
    	this.isDirty = isDirty;
    	
    	 if(isDirty) {
        	 Recipe matched = RecipeManager.getMatchingRecipe(getIngredients());
     		 this.dirtyImage = matched.dirtyPlate;
    	 }
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
        int layer = foodItem.getFoodLayer();

        if (layer < 0 || layer >= MAX_LAYERS) return;

        // Only insert if that layer index is not already taken
        while (ingredients.size() <= layer) {
            ingredients.add(null);
            ingredientImages.add(null);
        }

        if (ingredientImages.get(layer) != null) return;

        // Add ingredient data
        ingredientImages.set(layer, foodItem.getImage());
        ingredients.set(layer, foodItem.name);

        // === Check if this ingredient stack matches a recipe ===
        List<String> currentIngredients = new ArrayList<>();
        for (String ing : ingredients) {
            if (ing != null) currentIngredients.add(ing);
        }

        matchedRecipe = RecipeManager.getMatchingRecipe(currentIngredients);
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
    }

    public boolean isFinishedRecipe() {
        return RecipeManager.getMatchingRecipe(getIngredients()) != null;
    }

    public String getFinishedRecipeName() {
        Recipe matched = RecipeManager.getMatchingRecipe(getIngredients());
        return matched != null ? matched.getName() : null;
    }
    public void draw(Graphics2D g) {
        int baseX = (int) hitbox.x - xDrawOffset - gp.player.xDiff;
        int baseY = (int) hitbox.y - yDrawOffset - gp.player.yDiff;

        if(isDirty) {
            g.drawImage(dirtyImage, baseX, baseY, drawWidth, drawHeight, null);
        } else {
            g.drawImage(animations[0][0][0], baseX, baseY, drawWidth, drawHeight, null);
        }
        
        // Draw food layers from bottom to top
        
        if (matchedRecipe != null) {
        	g.drawImage(matchedRecipe.finishedPlate, baseX, baseY, drawWidth, drawHeight, null);
        } else {
            for (int i = 0; i < ingredientImages.size(); i++) {
                BufferedImage img = ingredientImages.get(i);
                if (img != null) {
                    g.drawImage(img, baseX, baseY, drawWidth, drawHeight, null);
                }
            }
        }
        
    }
    public void drawAtPosition(Graphics2D g, int x, int y) {
        int baseX = x;
        int baseY = y;

        if(isDirty) {
            g.drawImage(dirtyImage, baseX, baseY, drawWidth, drawHeight, null);
        } else {
            g.drawImage(animations[0][0][0], baseX, baseY, drawWidth, drawHeight, null);
        }
        
        // Draw food layers from bottom to top
        
        if (matchedRecipe != null) {
        	g.drawImage(matchedRecipe.finishedPlate, baseX, baseY, drawWidth, drawHeight, null);
        } else {
            for (int i = 0; i < ingredientImages.size(); i++) {
                BufferedImage img = ingredientImages.get(i);
                if (img != null) {
                    g.drawImage(img, baseX, baseY, drawWidth, drawHeight, null);
                }
            }
        }
        
    }
    public void drawInHand(Graphics2D g, int x, int y, boolean flip) {
        int baseX = (int) x;
        int baseY = (int) y;
        
        BufferedImage base = animations[0][0][0];
        
        if(flip) {
        	base = createHorizontalFlipped(base);
        }

        if(isDirty) {
            g.drawImage(dirtyImage, baseX, baseY, drawWidth, drawHeight, null);
        } else {
            g.drawImage(base, baseX, baseY, drawWidth, drawHeight, null);
        }
        
        if (matchedRecipe != null) {
        	g.drawImage(matchedRecipe.finishedPlate, baseX, baseY, drawWidth, drawHeight, null);
        } else {
            for (int i = 0; i < ingredientImages.size(); i++) {
                BufferedImage img = ingredientImages.get(i);
                if (img != null) {
                    g.drawImage(img, baseX, baseY, drawWidth, drawHeight, null);
                }
            }
        }
        
    }
}
