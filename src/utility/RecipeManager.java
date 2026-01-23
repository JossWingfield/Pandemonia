package utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import main.GamePanel;
import main.renderer.AssetPool;
import main.renderer.Texture;
import main.renderer.TextureRegion;
import utility.save.RecipeSaveData;

public class RecipeManager {
    private static final List<Recipe> allRecipes = new ArrayList<>();   // all possible recipes
    private static List<Recipe> unlockedRecipes = new ArrayList<>(); // currently unlocked recipes
    private static final List<Recipe> currentOrders = new ArrayList<>();
    private static final List<Recipe> cursedRecipes = new ArrayList<>(); 
    
    private static final Random random = new Random();

    private TextureRegion  PanIcon, choppedIcon, PotIcon, ovenIcon;
    private TextureRegion  PanIcon2, choppedIcon2, PotIcon2, ovenIcon2;

    public RecipeManager() {
        // Register all recipes here (master list)
        Recipe fish = new Recipe(
            "Fish",
            Arrays.asList("Fish"),
            Arrays.asList("Small Pot"),
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
            Arrays.asList("Frying Pan", "Chopping Board"),
            Arrays.asList("", ""),
            false, 
            importImage("/food/food.png").getSubimage(32, 96, 16, 16),
            importImage("/food/food.png").getSubimage(32, 112, 16, 16),
            8,
            1
        );
        registerRecipe(egg);
        
        Recipe friedEgg = new Recipe(
                "Fried Egg",
                Arrays.asList("Egg"),
                Arrays.asList("Frying Pan"),
                Arrays.asList(""),
                false, 
                importImage("/food/egg/PlatedEgg.png").toTextureRegion(),
                null,
                5,
                1
            );
        registerRecipe(friedEgg);
        Recipe steak = new Recipe(
                "Steak",
                Arrays.asList("Steak"),
                Arrays.asList("Frying Pan"),
                Arrays.asList(""),
                false, 
                importImage("/food/Steak.png").getSubimage(32, 0, 16, 16),
                null,
                6,
                1
            );
        registerRecipe(steak);

        Recipe steakAndVeg = new Recipe(
                "Steak and Veg",
                Arrays.asList("Steak", "Asparagus"),
                Arrays.asList("Frying Pan", "Chopping Board"),
                Arrays.asList("", ""),
                false, 
                importImage("/food/food.png").getSubimage(48, 96, 16, 16),
                importImage("/food/food.png").getSubimage(48, 96+16, 16, 16),
                6,
                1
            );
        registerRecipe(steakAndVeg);

        
        Recipe eggSandwich = new Recipe(
                "Egg Sandwich",
                Arrays.asList("Egg", "Greens", "Bread Slice"),
                Arrays.asList("Frying Pan", "Chopping Board", "Chopping Board"),
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
                Arrays.asList("Cheese", "Bread Slice"),
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
                Arrays.asList("Small Pot", "Chopping Board"),
                Arrays.asList("", "Frying Pan"),
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
                Arrays.asList("Small Pot", "Chopping Board"),
                Arrays.asList("", "Frying Pan"),
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
                Arrays.asList("Small Pot", "Chopping Board", "Chopping Board"),
                Arrays.asList("", "Frying Pan", "Frying Pan"),
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
                Arrays.asList("Small Pot", "Chopping Board", "Chopping Board"),
                Arrays.asList("", "Frying Pan", "Frying Pan"),
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
                Arrays.asList("Small Pot", "Frying Pan", "Chopping Board"),
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
                Arrays.asList("Small Pot", "Frying Pan", "Chopping Board"),
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
                Arrays.asList("Small Pot", "Chopping Board", "Chopping Board"),
                Arrays.asList("", "Frying Pan", "Frying Pan"),
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
                Arrays.asList("Small Pot", "Chopping Board", "Chopping Board"),
                Arrays.asList("", "Frying Pan", "Frying Pan"),
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
                importImage("/food/Salad.png").toTextureRegion(),
                null,
                5,
                1
            );
        registerRecipe(salad);
        Recipe bruschetta = new Recipe(
                "Bruschetta",
                Arrays.asList("Bread Slice", "Chopped Tomatoes"),
                Arrays.asList("Chopping Board", "Chopping Board"),
                Arrays.asList("", ""),
                false, 
                importImage("/food/Bruschetta.png").toTextureRegion(),
                null,
                5,
                1
            );
        registerRecipe(bruschetta);
        
        //PHASE 2
        
        Recipe Potato = new Recipe(
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
        registerRecipe(Potato);
        
        Recipe chicken = new Recipe(
                "Chicken",
                Arrays.asList("Chicken"),
                Arrays.asList("Oven"),
                Arrays.asList(""),
                false, 
                importImage("/food/food.png").getSubimage(0, 96, 16, 16),
                importImage("/food/food.png").getSubimage(0, 112, 16, 16),
                8,
                2
            );
        registerRecipe(chicken);
        Recipe scrambledEgg = new Recipe(
                    "Scrambled Egg",
                    Arrays.asList("Egg", "Bread Slice"),
                    Arrays.asList("Small Pot", "Chopping Board"),
                    Arrays.asList("", "Oven"),
                    false, 
                    importImage("/food/egg/ScrambledEgg.png").getSubimage(32, 0, 16, 16),
                    null,
                    9,
                    2
                );
        registerRecipe(scrambledEgg);
   
        Recipe garlicBread = new Recipe(
                "Garlic Bread",
                Arrays.asList("Bread Slice", "Chopped Garlic"),
                Arrays.asList("Oven Tray", "Chopping Board"),
                Arrays.asList("", "Oven Tray"),
                false, 
                importImage("/food/GarlicBread.png").getSubimage(16, 0, 16, 16),
                null,
                9,
                2
            );
        registerRecipe(garlicBread);
        
        Recipe grilledCheese = new Recipe(
        	    "Cheese Toast",
                Arrays.asList("Bread Slice", "Cheese"),
                Arrays.asList("Oven Tray", "Chopping Board"),
                Arrays.asList("", "Oven Tray"),
                false, 
                importImage("/food/CheeseToast.png").getSubimage(16, 0, 16, 16),
                null,
                9,
                2
            );
        registerRecipe(grilledCheese);
        
        Recipe carrotSalad = new Recipe(
        	    "Carrot Salad",
                Arrays.asList("Carrot", "Red Onion", "Greens"),
                Arrays.asList("Chopping Board", "Chopping Board", "Chopping Board"),
                Arrays.asList("", "", ""),
                false, 
                importImage("/food/CarrotSalad.png").getSubimage(0, 0, 16, 16),
                null,
                7,
                2
            );
        registerRecipe(carrotSalad);
        
        Recipe roastChicken = new Recipe(
        	    "Roast Chicken",
                Arrays.asList("Chicken", "Potato", "Greens"),
                Arrays.asList("Oven Tray", "Oven Tray", "Chopping Board"),
                Arrays.asList("", "", "Oven Tray"),
                false, 
                importImage("/food/RoastChicken.png").getSubimage(0, 0, 16, 16),
                null,
                11,
                2
            );
        registerRecipe(roastChicken);
        
        Recipe vegRoast = new Recipe(
        	    "Vegetable Roast",
                Arrays.asList("Carrot", "Potato", "Aubergine"),
                Arrays.asList("Chopping Board", "Oven Tray", "Oven Tray"),
                Arrays.asList("Oven Tray", "", ""),
                false, 
                importImage("/food/VegRoast.png").getSubimage(0, 0, 16, 16),
                null,
                10,
                2
            );
        registerRecipe(vegRoast);
        
        Recipe aubergineBake = new Recipe(
        	    "Aubergine Bake",
                Arrays.asList("Tomato", "Cheese", "Aubergine"),
                Arrays.asList("Chopping Board", "Chopping Board", "Chopping Board"),
                Arrays.asList("Oven Tray", "Oven Tray", "Oven Tray"),
                false, 
                importImage("/food/AubergineBake.png").getSubimage(0, 0, 16, 16),
                null,
                10,
                2
            );
        registerRecipe(aubergineBake);
        
        Recipe lasagna = new Recipe(
        	    "Lasagna",
                Arrays.asList("Lasagna", "Tomato", "Cheese"),
                Arrays.asList("Oven Tray", "Chopping Board", "Chopping Board"),
                Arrays.asList("", "Oven Tray", "Oven Tray"),
                false, 
                importImage("/food/pasta/Lasagna.png").getSubimage(32, 0, 16, 16),
                null,
                10,
                2
            );
        registerRecipe(lasagna);
        
        Recipe pastaBake = new Recipe(
        	    "Pasta Bake",
                Arrays.asList("Penne", "Tomato", "Cheese"),
                Arrays.asList("Small Pot", "Chopping Board", "Chopping Board"),
                Arrays.asList("OvenTray", "Oven Tray", "Oven Tray"),
                false, 
                importImage("/food/pasta/PastaBake.png").getSubimage(0, 0, 16, 16),
                null,
                10,
                2
            );
        registerRecipe(pastaBake);
        
        //ADDITIONAL RECIPES TO ADD:
        //OVEN RECIPES
        //mac and cheese, pasta al forno, pizza types + calzone + flatbread(dough in oven)
        //Roast beef and pork chops, pies
        //Cakes, cookies, brownies 
        //NEW PASTA types, ravioli, bows etc, rigatoni also add parmesan
        
        //ADD CURRIES and rice dishes
        //FRYER RECIPES
        //chips and chicken, beer battered cod
        
        //SOUPS
        
        //FRUIT SALADS
        //ADD PRE MARINATED FOODS e.g. salt, pepper, marinade sauce, honey glazed
        //POTENTIALLY ADD STARTERS AND SIDES
        
        //CURSED RECIPES
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
        PanIcon = importImage("/UI/recipe/Icons.png").getSubimage(0, 0, 16, 16);
        choppedIcon = importImage("/UI/recipe/Icons.png").getSubimage(32, 0, 16, 16);
        PotIcon = importImage("/UI/recipe/Icons.png").getSubimage(16, 0, 16, 16);
        ovenIcon = importImage("/UI/recipe/Icons.png").getSubimage(48, 0, 16, 16);
        PanIcon2 = importImage("/UI/recipe/CursedIcons.png").getSubimage(0, 0, 16, 16);
        choppedIcon2 = importImage("/UI/recipe/CursedIcons.png").getSubimage(32, 0, 16, 16);
        PotIcon2 = importImage("/UI/recipe/CursedIcons.png").getSubimage(16, 0, 16, 16);
        ovenIcon2 = importImage("/UI/recipe/CursedIcons.png").getSubimage(48, 0, 16, 16);
        
        //unlockAllRecipes();
    }

    public void unlockAllRecipes() {
        unlockedRecipes.clear();

        for (Recipe recipe : allRecipes) {
            unlockRecipe(recipe);
        }
    }
    // Register to master list (but not unlocked yet)
    public static void registerRecipe(Recipe recipe) {
        allRecipes.add(recipe);
    }
    public static void registerCursedRecipe(Recipe recipe) {
        cursedRecipes.add(recipe);
    }
    public Recipe chooseChefSpecial(List<Recipe> todaysMenu) {
        // Filter unlocked recipes that:
        // 1. Are unlocked
        // 2. Are not already on today's menu
        // 3. Are allowed in the current phase
        List<Recipe> candidates = unlockedRecipes.stream()
                .filter(r -> !todaysMenu.contains(r))
                .toList();

        if (candidates.isEmpty()) {
            return null; // no valid chef special
        }

        return candidates.get(random.nextInt(candidates.size()));
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
    public static Recipe getRandomLocked(GamePanel gp) {
        int currentPhase = gp.progressM.currentPhase;

        // Filter locked recipes by phase
        List<Recipe> locked = getLockedRecipes().stream()
                .filter(r -> r.phase <= currentPhase)
                .toList();

        if (locked.size() < 1) return null; // not enough locked recipes

        Recipe r1 = locked.get(random.nextInt(locked.size()));

        return r1;
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
    public static Recipe getMatchingRecipe(List<String> plateIngredients, List<String> cookMethods, List<String> secondaryCookMethods) {
        for (Recipe recipe : unlockedRecipes) { // only match unlocked ones
            if (recipe.matches(plateIngredients, cookMethods, secondaryCookMethods)) {
                return recipe;
            }
        }
        for (Recipe recipe : cursedRecipes) { // only match unlocked ones
            if (recipe.matches(plateIngredients, cookMethods, secondaryCookMethods)) {
                return recipe;
            }
        }
        return null;
    }
    public TextureRegion getIconFromName(String name, boolean isCursed) {
    	if(!isCursed) {
	        switch(name) {
	            case "Frying Pan": return PanIcon;
	            case "Small Pot": return PotIcon;
	            case "Chopping Board": return choppedIcon;
	            case "Oven": return ovenIcon;
	        }
	        return null;
    	} else {
    		switch(name) {
            case "Frying Pan": return PanIcon2;
            case "Small Pot": return PotIcon2;
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

    public static boolean completeOrder(List<String> plateIngredients, List<String> cookMethods, List<String> secondaryCookMethods) {
        for (int i = 0; i < currentOrders.size(); i++) {
            Recipe order = currentOrders.get(i);
            if (order.matches(plateIngredients, cookMethods, secondaryCookMethods)) {
                order.incrementCookCount();
                currentOrders.remove(i);
                return true;
            }
        }
        return false;
    }

    private Texture importImage(String filePath) {
		Texture texture = AssetPool.getTexture(filePath);
	    return texture;
	}

    public static List<Recipe> getCurrentOrders() {
        return currentOrders;
    }
    public static RecipeSaveData saveRecipeData() {
    	RecipeSaveData data = new RecipeSaveData();
    	List<String> names = new ArrayList<String>();

        List<Integer> stars = new ArrayList<>();
        List<Integer> cookCounts = new ArrayList<>();
        List<Boolean> mastered = new ArrayList<>();
        
    	for(Recipe r: unlockedRecipes) {
    		names.add(r.getName());
    	    stars.add(r.getStarLevel());
    	    cookCounts.add(r.getTimesCooked());
    	    mastered.add(r.isMastered());
    	}
    	data.unlockedRecipesNames = names;
        data.starLevels = stars;
        data.cookCounts = cookCounts;
        data.masteredFlags = mastered;
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
    public static void applySaveData(RecipeSaveData data) {
        unlockedRecipes.clear();

        for (int i = 0; i < data.unlockedRecipesNames.size(); i++) {
            Recipe r = getRecipeByName(data.unlockedRecipesNames.get(i));
            if (r != null) {
                unlockRecipe(r);
                r.setCookCount(data.cookCounts.get(i));
                r.setStarLevel(data.starLevels.get(i));
                r.setMastered(data.masteredFlags.get(i));
            }
        }
    }
    
}
