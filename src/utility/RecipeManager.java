package utility;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import main.GamePanel;
import utility.save.RecipeSaveData;

public class RecipeManager {
    private static final List<Recipe> allRecipes = new ArrayList<>();   // all possible recipes
    private static List<Recipe> unlockedRecipes = new ArrayList<>(); // currently unlocked recipes
    private static final List<Recipe> currentOrders = new ArrayList<>();
    private static final List<Recipe> cursedRecipes = new ArrayList<>(); 
    
    private static final Random random = new Random();

    private BufferedImage panIcon, choppedIcon, potIcon, ovenIcon;
    private BufferedImage panIcon2, choppedIcon2, potIcon2, ovenIcon2;

    public RecipeManager() {
        // Register all recipes here (master list)
        Recipe fish = new Recipe(
            "Fish",
            Arrays.asList("Fish"),
            Arrays.asList("Pot"),
            Arrays.asList(""),
            false, 
            importImage("/food/food.png").getSubimage(16, 96, 16, 16),
            importImage("/food/food.png").getSubimage(16, 112, 16, 16),
            6,
            1
        );
        registerRecipe(fish);

        Recipe egg = new Recipe(
            "Egg",
            Arrays.asList("Egg", "Greens"),
            Arrays.asList("Pan", "Chopping Board"),
            Arrays.asList("", ""),
            false, 
            importImage("/food/food.png").getSubimage(32, 96, 16, 16),
            importImage("/food/food.png").getSubimage(32, 112, 16, 16),
            8,
            1
        );
        registerRecipe(egg);

        Recipe chicken = new Recipe(
            "Chicken",
            Arrays.asList("Chicken"),
            Arrays.asList("Oven"),
            Arrays.asList(""),
            false, 
            importImage("/food/food.png").getSubimage(0, 96, 16, 16),
            importImage("/food/food.png").getSubimage(0, 112, 16, 16),
            6,
            2
        );
        registerRecipe(chicken);
        Recipe friedEgg = new Recipe(
                "Fried Egg",
                Arrays.asList("Egg"),
                Arrays.asList("Pan"),
                Arrays.asList(""),
                false, 
                importImage("/food/egg/PlatedEgg.png"),
                null,
                5,
                1
            );
        registerRecipe(friedEgg);
        Recipe steak = new Recipe(
                "Steak",
                Arrays.asList("Steak"),
                Arrays.asList("Pan"),
                Arrays.asList(""),
                false, 
                importImage("/food/Steak.png").getSubimage(32, 0, 16, 16),
                null,
                6,
                1
            );
        registerRecipe(steak);
        
        Recipe potato = new Recipe(
                "Potato",
                Arrays.asList("Potato"),
                Arrays.asList("Oven"),
                Arrays.asList(""),
                false, 
                importImage("/food/food.png").getSubimage(16, 16, 16, 16),
                null,
                5,
                2
            );
        registerRecipe(potato);
        
        Recipe eggSandwich = new Recipe(
                "Egg Sandwich",
                Arrays.asList("Egg", "Greens", "Bread"),
                Arrays.asList("Pan", "Chopping Board", "Chopping Board"),
                Arrays.asList("", "", ""),
                false, 
                importImage("/food/food.png").getSubimage(48, 16, 16, 16),
                null,
                8,
                1
            );
        registerRecipe(eggSandwich);
        
        Recipe cheeseSandwich = new Recipe(
                "Cheese Sandwich",
                Arrays.asList("Cheese", "Bread"),
                Arrays.asList("Chopping Board", "Chopping Board"),
                Arrays.asList("", ""),
                false, 
                importImage("/food/CheeseSandwich.png").getSubimage(0, 0, 16, 16),
                null,
                5,
                1
            );
        registerRecipe(cheeseSandwich);
        Recipe spaghettiNapoli = new Recipe(
                "Napolitana",
                Arrays.asList("Spaghetti", "Chopped Tomatoes"),
                Arrays.asList("Pot", "Chopping Board"),
                Arrays.asList("", "Pan"),
                false, 
                importImage("/food/pasta/Spaghetti.png").getSubimage(48, 0, 16, 16),
                null,
                7,
                1
            );
        registerRecipe(spaghettiNapoli);
        Recipe penneNapoli = new Recipe(
                "Napolitana",
                Arrays.asList("Penne", "Chopped Tomatoes"),
                Arrays.asList("Pot", "Chopping Board"),
                Arrays.asList("", "Pan"),
                false, 
                importImage("/food/pasta/Penne.png").getSubimage(32, 0, 16, 16),
                null,
                7,
                1
            );
        registerRecipe(penneNapoli);
        Recipe penneMeatballs = new Recipe(
                "Meatballs",
                Arrays.asList("Penne", "Chopped Tomatoes", "Meatball"),
                Arrays.asList("Pot", "Chopping Board", "Chopping Board"),
                Arrays.asList("", "Pan", "Pan"),
                false, 
                importImage("/food/pasta/Meatball.png").getSubimage(48, 0, 16, 16),
                null,
                10,
                1
            );
        registerRecipe(penneMeatballs);
        Recipe spaghettiMeatballs = new Recipe(
                "Meatballs",
                Arrays.asList("Spaghetti", "Chopped Tomatoes", "Meatball"),
                Arrays.asList("Pot", "Chopping Board", "Chopping Board"),
                Arrays.asList("", "Pan", "Pan"),
                false, 
                importImage("/food/pasta/Meatball.png").getSubimage(32, 0, 16, 16),
                null,
                10,
                1
            );
        registerRecipe(spaghettiMeatballs);
        Recipe spaghettiCarbonara = new Recipe(
                "Carbonara",
                Arrays.asList("Spaghetti", "Egg", "Cheese"),
                Arrays.asList("Pot", "Pan", "Chopping Board"),
                Arrays.asList("", "", ""),
                false, 
                importImage("/food/pasta/Carbonara.png").getSubimage(0, 0, 16, 16),
                null,
                8,
                1
            );
        registerRecipe(spaghettiCarbonara);
        Recipe penneCarbonara = new Recipe(
                "Carbonara",
                Arrays.asList("Penne", "Egg", "Cheese"),
                Arrays.asList("Pot", "Pan", "Chopping Board"),
                Arrays.asList("", "", ""),
                false, 
                importImage("/food/pasta/Carbonara.png").getSubimage(16, 0, 16, 16),
                null,
                8,
                1
            );
        registerRecipe(penneCarbonara);
        Recipe penneDiavola = new Recipe(
                "Diavola",
                Arrays.asList("Penne", "Chopped Tomatoes", "Chicken Pieces"),
                Arrays.asList("Pot", "Chopping Board", "Chopping Board"),
                Arrays.asList("", "Pan", "Pan"),
                false, 
                importImage("/food/pasta/Chicken.png").getSubimage(32, 0, 16, 16),
                null,
                10,
                1
            );
        registerRecipe(penneDiavola);
        Recipe spaghettiDiavola = new Recipe(
                "Diavola",
                Arrays.asList("Spaghetti", "Chopped Tomatoes", "Chicken Pieces"),
                Arrays.asList("Pot", "Chopping Board", "Chopping Board"),
                Arrays.asList("", "Pan", "Pan"),
                false, 
                importImage("/food/pasta/Chicken.png").getSubimage(48, 0, 16, 16),
                null,
                10,
                1
            );
        registerRecipe(spaghettiDiavola);
        Recipe salad = new Recipe(
                "Salad",
                Arrays.asList("Greens", "Chopped Tomatoes"),
                Arrays.asList("Chopping Board", "Chopping Board"),
                Arrays.asList("", ""),
                false, 
                importImage("/food/Salad.png"),
                null,
                5,
                1
            );
        registerRecipe(salad);
        Recipe bruschetta = new Recipe(
                "Bruschetta",
                Arrays.asList("Bread", "Chopped Tomatoes"),
                Arrays.asList("Chopping Board", "Chopping Board"),
                Arrays.asList("", ""),
                false, 
                importImage("/food/Bruschetta.png"),
                null,
                5,
                1
            );
        registerRecipe(bruschetta);
        Recipe cursedGreens = new Recipe(
                "Cursed Salad",
                Arrays.asList("Cursed Greens", "Chopped Tomatoes"),
                Arrays.asList("Chopping Board", "Chopping Board"),
                Arrays.asList("", ""),
                false, 
                importImage("/food/cursed/CursedGreens.png").getSubimage(16, 0, 16, 16),
                null,
                15,
                1
            );
        registerCursedRecipe(cursedGreens);
        cursedGreens.setCursed();

        // Unlock some recipes at the start of the game
        unlockRecipe(fish);
        unlockRecipe(egg);
        unlockRecipe(friedEgg);
        unlockRecipe(bruschetta);
        unlockRecipe(salad);
        
        // Icons
        panIcon = importImage("/UI/recipe/Icons.png").getSubimage(0, 0, 16, 16);
        choppedIcon = importImage("/UI/recipe/Icons.png").getSubimage(32, 0, 16, 16);
        potIcon = importImage("/UI/recipe/Icons.png").getSubimage(16, 0, 16, 16);
        ovenIcon = importImage("/UI/recipe/Icons.png").getSubimage(48, 0, 16, 16);
        panIcon2 = importImage("/UI/recipe/CursedIcons.png").getSubimage(0, 0, 16, 16);
        choppedIcon2 = importImage("/UI/recipe/CursedIcons.png").getSubimage(32, 0, 16, 16);
        potIcon2 = importImage("/UI/recipe/CursedIcons.png").getSubimage(16, 0, 16, 16);
        ovenIcon2 = importImage("/UI/recipe/CursedIcons.png").getSubimage(48, 0, 16, 16);
    }

    // Register to master list (but not unlocked yet)
    public static void registerRecipe(Recipe recipe) {
        allRecipes.add(recipe);
    }
    public static void registerCursedRecipe(Recipe recipe) {
        cursedRecipes.add(recipe);
    }
    public static Recipe[] getTwoRandomLocked(GamePanel gp) {
        int currentPhase = gp.progressM.currentPhase;

        // Filter locked recipes by phase
        List<Recipe> locked = getLockedRecipes().stream()
                .filter(r -> r.phase <= currentPhase)
                .toList();

        if (locked.size() < 2) return null; // not enough locked recipes

        Recipe r1 = locked.get(random.nextInt(locked.size()));
        Recipe r2;
        do {
            r2 = locked.get(random.nextInt(locked.size()));
        } while (r1 == r2);

        return new Recipe[] { r1, r2 };
    }
    public static List<Recipe> getLockedRecipes() {
        List<Recipe> locked = new ArrayList<>(allRecipes);
        locked.removeAll(unlockedRecipes);
        return locked;
    }
    // Unlocking system
    public static void unlockRecipe(Recipe recipe) {
        if (!unlockedRecipes.contains(recipe)) {
            unlockedRecipes.add(recipe);
        }
    }

    public static ArrayList<Recipe> getUnlockedRecipes() {
        return new ArrayList<>(unlockedRecipes);
    }

    public static List<Recipe> getAllRecipes() {
        return new ArrayList<>(allRecipes);
    }
    public static boolean areHauntedRecipesPresent() {
    	for (Recipe recipe : currentOrders) {
            if(recipe.isCursed) {
            	return true;
            }
        }
    	return false;
    }
    public static String getCurrentHauntedIngredient() {
    	for (Recipe recipe : currentOrders) {
            if(recipe.isCursed) {
            	return recipe.getIngredients().get(0);
            }
        }
    	return null;
    }
    public static Recipe getMatchingRecipe(List<String> plateIngredients) {
        for (Recipe recipe : unlockedRecipes) { // only match unlocked ones
            if (recipe.matches(plateIngredients)) {
                return recipe;
            }
        }
        for (Recipe recipe : cursedRecipes) { // only match unlocked ones
            if (recipe.matches(plateIngredients)) {
                return recipe;
            }
        }
        return null;
    }
    public BufferedImage getIconFromName(String name, boolean isCursed) {
    	if(!isCursed) {
	        switch(name) {
	            case "Pan": return panIcon;
	            case "Pot": return potIcon;
	            case "Chopping Board": return choppedIcon;
	            case "Oven": return ovenIcon;
	        }
	        return null;
    	} else {
    		switch(name) {
            case "Pan": return panIcon2;
            case "Pot": return potIcon2;
            case "Chopping Board": return choppedIcon2;
            case "Oven": return ovenIcon2;
        }
        return null;
    	}
    }

    public static Recipe getRandomRecipe() {
        if (unlockedRecipes.isEmpty()) return null;
        return unlockedRecipes.get(random.nextInt(unlockedRecipes.size()));
    }   
    public static Recipe getRandomCursedRecipe() {
        if (cursedRecipes.isEmpty()) return null;
        return cursedRecipes.get(random.nextInt(cursedRecipes.size()));
    }

    public static void addOrder(Recipe recipe) {
        currentOrders.add(recipe);
    }

    public static void removeOrder(Recipe recipe) {
        currentOrders.remove(recipe);
    }

    public static boolean completeOrder(List<String> plateIngredients) {
        for (int i = 0; i < currentOrders.size(); i++) {
            Recipe order = currentOrders.get(i);
            if (order.matches(plateIngredients)) {
                currentOrders.remove(i);
                return true;
            }
        }
        return false;
    }

    protected BufferedImage importImage(String filePath) {
        BufferedImage importedImage = null;
        try {
            importedImage = ImageIO.read(getClass().getResourceAsStream(filePath));
        } catch(IOException e) {
            e.printStackTrace();
        }
        return importedImage;
    }

    public static List<Recipe> getCurrentOrders() {
        return currentOrders;
    }
    public static RecipeSaveData saveRecipeData() {
    	RecipeSaveData data = new RecipeSaveData();
    	List<String> names = new ArrayList<String>();
    	for(Recipe r: unlockedRecipes) {
    		names.add(r.getName());
    	}
    	data.unlockedRecipesNames = names;
    	return data;
    }
    public static Recipe getRecipeByName(String name) {
        for (Recipe recipe : allRecipes) {
            if (recipe.getName().equals(name)) {
                return recipe;
            }
        }
        return null; // not found
    }
    public static void applySaveData(List<String> unlockedNames) {
        unlockedRecipes.clear();
        for (String name : unlockedNames) {
            for (Recipe r : allRecipes) {
                if (r.getName().equals(name)) {
                    unlockRecipe(r); // instead of unlockedRecipes.add(r)
                }
            }
        }
    }
    
}
