package utility.recipe;

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
    private static final List<Recipe> allRecipes      = new ArrayList<>();
    private static List<Recipe>       unlockedRecipes = new ArrayList<>();
    private static final List<Order>  currentOrders   = new ArrayList<>();

    private static final Random random = new Random();

    private TextureRegion PanIcon, choppedIcon, PotIcon, ovenIcon, fryerIcon,
                          freezerIcon, seasoningIcon, ovenTrayIcon, coatedIcon;
    
    private TextureRegion flame, golden, frozen, crispy, hearty, comfort, raw, delicate, aromatic, cursed;


    public RecipeManager() {

        // ── PHASE 1 ──────────────────────────────────────────────────────────
        // Phase 1 may only use: FLAME, CRISPY, DELICATE, RAW, COMFORT, HEARTY
        // (GOLDEN and FROZEN unlock at Phase 2; AROMATIC/CURSED reserved)

        Recipe fish = new Recipe(
            "Fish",
            Arrays.asList(
                new RecipeIngredient("Fish").addStep("Small Pot")
            ),
            importImage("/food/food.png").getSubimage(16, 96, 16, 16),
            importImage("/food/food.png").getSubimage(16, 112, 16, 16),
            6, 1,
            RecipeTag.DELICATE, RecipeTag.FLAME          // 2 tags — uncommon
        );
        registerRecipe(fish);
        
        Recipe egg = new Recipe(
            "Egg",
            Arrays.asList(
                new RecipeIngredient("Egg").addStep("Frying Pan"),
                new RecipeIngredient("Greens").addStep("Chopping Board")
            ),
            importImage("/food/food.png").getSubimage(32, 96, 16, 16),
            importImage("/food/food.png").getSubimage(32, 112, 16, 16),
            8, 1,
            RecipeTag.FLAME, RecipeTag.RAW               // 2 tags — uncommon
        );
        registerRecipe(egg);

        Recipe friedEgg = new Recipe(
            "Fried Egg",
            Arrays.asList(
                new RecipeIngredient("Egg").addStep("Frying Pan")
            ),
            importImage("/food/egg/PlatedEgg.png").toTextureRegion(),
            null,
            5, 1,
            RecipeTag.FLAME                              // 1 tag — common
        );
        registerRecipe(friedEgg);

        Recipe steak = new Recipe(
            "Steak",
            Arrays.asList(
                new RecipeIngredient("Steak").addStep("Frying Pan")
            ),
            importImage("/food/Steak.png").getSubimage(32, 0, 16, 16),
            null,
            6, 1,
            RecipeTag.FLAME, RecipeTag.HEARTY            // 2 tags — uncommon
        );
        registerRecipe(steak);

        Recipe steakAndVeg = new Recipe(
            "Steak and Veg",
            Arrays.asList(
                new RecipeIngredient("Steak").addStep("Frying Pan"),
                new RecipeIngredient("Asparagus").addStep("Chopping Board")
            ),
            importImage("/food/food.png").getSubimage(48, 96, 16, 16),
            importImage("/food/food.png").getSubimage(48, 96 + 16, 16, 16),
            6, 1,
            RecipeTag.FLAME, RecipeTag.HEARTY, RecipeTag.RAW  // 3 tags — rare
        );
        registerRecipe(steakAndVeg);

        Recipe eggSandwich = new Recipe(
            "Egg Sandwich",
            Arrays.asList(
                new RecipeIngredient("Egg").addStep("Frying Pan"),
                new RecipeIngredient("Greens").addStep("Chopping Board"),
                new RecipeIngredient("Bread Slice").addStep("Chopping Board")
            ),
            importImage("/food/food.png").getSubimage(48, 16, 16, 16),
            null,
            8, 1,
            RecipeTag.FLAME, RecipeTag.COMFORT           // 2 tags — uncommon
        );
        registerRecipe(eggSandwich);

        Recipe cheeseSandwich = new Recipe(
            "Cheese Sandwich",
            Arrays.asList(
                new RecipeIngredient("Cheese").addStep("Chopping Board"),
                new RecipeIngredient("Bread Slice").addStep("Chopping Board")
            ),
            importImage("/food/CheeseSandwich.png").getSubimage(0, 0, 16, 16),
            null,
            5, 1,
            RecipeTag.COMFORT                            // 1 tag — common
        );
        registerRecipe(cheeseSandwich);

        Recipe spaghettiNapoli = new Recipe(
            "Napolitana",
            Arrays.asList(
                new RecipeIngredient("Spaghetti").addStep("Small Pot"),
                new RecipeIngredient("Chopped Tomatoes")
                    .addStep("Chopping Board").addStep("Frying Pan")
            ),
            importImage("/food/pasta/Spaghetti.png").getSubimage(48, 0, 16, 16),
            null,
            7, 1,
            RecipeTag.FLAME, RecipeTag.COMFORT           // 2 tags — uncommon
        );
        registerRecipe(spaghettiNapoli);

        Recipe penneNapoli = new Recipe(
            "Napolitana",
            Arrays.asList(
                new RecipeIngredient("Penne").addStep("Small Pot"),
                new RecipeIngredient("Chopped Tomatoes")
                    .addStep("Chopping Board").addStep("Frying Pan")
            ),
            importImage("/food/pasta/Penne.png").getSubimage(32, 0, 16, 16),
            null,
            7, 1,
            RecipeTag.FLAME, RecipeTag.COMFORT           // 2 tags — uncommon
        );
        registerRecipe(penneNapoli);

        Recipe penneMeatballs = new Recipe(
            "Meatballs",
            Arrays.asList(
                new RecipeIngredient("Penne").addStep("Small Pot"),
                new RecipeIngredient("Chopped Tomatoes")
                    .addStep("Chopping Board").addStep("Frying Pan"),
                new RecipeIngredient("Meatball")
                    .addStep("Chopping Board").addStep("Frying Pan")
            ),
            importImage("/food/pasta/Meatball.png").getSubimage(48, 0, 16, 16),
            null,
            10, 1,
            RecipeTag.FLAME, RecipeTag.HEARTY, RecipeTag.COMFORT  // 3 tags — rare
        );
        registerRecipe(penneMeatballs);

        Recipe spaghettiMeatballs = new Recipe(
            "Meatballs",
            Arrays.asList(
                new RecipeIngredient("Spaghetti").addStep("Small Pot"),
                new RecipeIngredient("Chopped Tomatoes")
                    .addStep("Chopping Board").addStep("Frying Pan"),
                new RecipeIngredient("Meatball")
                    .addStep("Chopping Board").addStep("Frying Pan")
            ),
            importImage("/food/pasta/Meatball.png").getSubimage(32, 0, 16, 16),
            null,
            10, 1,
            RecipeTag.FLAME, RecipeTag.HEARTY, RecipeTag.COMFORT  // 3 tags — rare
        );
        registerRecipe(spaghettiMeatballs);

        Recipe spaghettiCarbonara = new Recipe(
            "Carbonara",
            Arrays.asList(
                new RecipeIngredient("Spaghetti").addStep("Small Pot"),
                new RecipeIngredient("Egg").addStep("Frying Pan"),
                new RecipeIngredient("Cheese").addStep("Chopping Board")
            ),
            importImage("/food/pasta/Carbonara.png").getSubimage(0, 0, 16, 16),
            null,
            8, 1,
            RecipeTag.FLAME, RecipeTag.COMFORT           // 2 tags — uncommon
        );
        registerRecipe(spaghettiCarbonara);

        Recipe penneCarbonara = new Recipe(
            "Carbonara",
            Arrays.asList(
                new RecipeIngredient("Penne").addStep("Small Pot"),
                new RecipeIngredient("Egg").addStep("Frying Pan"),
                new RecipeIngredient("Cheese").addStep("Chopping Board")
            ),
            importImage("/food/pasta/Carbonara.png").getSubimage(16, 0, 16, 16),
            null,
            8, 1,
            RecipeTag.FLAME, RecipeTag.COMFORT           // 2 tags — uncommon
        );
        registerRecipe(penneCarbonara);

        Recipe penneDiavola = new Recipe(
            "Diavola",
            Arrays.asList(
                new RecipeIngredient("Penne").addStep("Small Pot"),
                new RecipeIngredient("Chopped Tomatoes")
                    .addStep("Chopping Board").addStep("Frying Pan"),
                new RecipeIngredient("Chicken Pieces")
                    .addStep("Chopping Board").addStep("Frying Pan")
            ),
            importImage("/food/pasta/Chicken.png").getSubimage(32, 0, 16, 16),
            null,
            10, 1,
            RecipeTag.FLAME, RecipeTag.HEARTY            // 2 tags — uncommon
        );
        registerRecipe(penneDiavola);

        Recipe spaghettiDiavola = new Recipe(
            "Diavola",
            Arrays.asList(
                new RecipeIngredient("Spaghetti").addStep("Small Pot"),
                new RecipeIngredient("Chopped Tomatoes")
                    .addStep("Chopping Board").addStep("Frying Pan"),
                new RecipeIngredient("Chicken Pieces")
                    .addStep("Chopping Board").addStep("Frying Pan")
            ),
            importImage("/food/pasta/Chicken.png").getSubimage(48, 0, 16, 16),
            null,
            10, 1,
            RecipeTag.FLAME, RecipeTag.HEARTY            // 2 tags — uncommon
        );
        registerRecipe(spaghettiDiavola);

        Recipe salad = new Recipe(
            "Salad",
            Arrays.asList(
                new RecipeIngredient("Greens").addStep("Chopping Board"),
                new RecipeIngredient("Chopped Tomatoes").addStep("Chopping Board")
            ),
            importImage("/food/Salad.png").toTextureRegion(),
            null,
            5, 1,
            RecipeTag.RAW                                // 1 tag — common
        );
        registerRecipe(salad);

        Recipe bruschetta = new Recipe(
            "Bruschetta",
            Arrays.asList(
                new RecipeIngredient("Bread Slice").addStep("Chopping Board"),
                new RecipeIngredient("Chopped Tomatoes").addStep("Chopping Board")
            ),
            importImage("/food/Bruschetta.png").toTextureRegion(),
            null,
            5, 1,
            RecipeTag.RAW, RecipeTag.COMFORT             // 2 tags — uncommon
        );
        registerRecipe(bruschetta);

        // ── PHASE 2 ──────────────────────────────────────────────────────────
        // Phase 2 adds: GOLDEN, FROZEN  (still no AROMATIC/CURSED)

        Recipe potato = new Recipe(
            "Potato",
            Arrays.asList(
                new RecipeIngredient("Potato").addStep("Oven")
            ),
            importImage("/food/food.png").getSubimage(16, 16, 16, 16),
            null,
            5, 2,
            RecipeTag.GOLDEN                             // 1 tag — common
        );
        registerRecipe(potato);

        Recipe chicken = new Recipe(
            "Chicken",
            Arrays.asList(
                new RecipeIngredient("Chicken").addStep("Oven")
            ),
            importImage("/food/food.png").getSubimage(0, 96, 16, 16),
            importImage("/food/food.png").getSubimage(0, 112, 16, 16),
            8, 2,
            RecipeTag.GOLDEN, RecipeTag.HEARTY           // 2 tags — uncommon
        );
        registerRecipe(chicken);

        Recipe scrambledEgg = new Recipe(
            "Scrambled Egg",
            Arrays.asList(
                new RecipeIngredient("Egg").addStep("Small Pot"),
                new RecipeIngredient("Bread Slice")
                    .addStep("Chopping Board").addStep("Oven")
            ),
            importImage("/food/egg/ScrambledEgg.png").getSubimage(32, 0, 16, 16),
            null,
            9, 2,
            RecipeTag.FLAME, RecipeTag.GOLDEN            // 2 tags — uncommon
        );
        registerRecipe(scrambledEgg);

        Recipe garlicBread = new Recipe(
            "Garlic Bread",
            Arrays.asList(
                new RecipeIngredient("Chopped Garlic")
                    .addStep("Chopping Board").addStep("Oven Tray"),
                new RecipeIngredient("Bread Slice")
                    .addStep("Chopping Board").addStep("Oven Tray")
            ),
            importImage("/food/GarlicBread.png").getSubimage(16, 0, 16, 16),
            null,
            9, 2,
            RecipeTag.GOLDEN, RecipeTag.COMFORT          // 2 tags — uncommon
            // AROMATIC would fit perfectly here — add when phase unlocks
        );
        registerRecipe(garlicBread);

        Recipe grilledCheese = new Recipe(
            "Cheese Toast",
            Arrays.asList(
                new RecipeIngredient("Cheese")
                    .addStep("Chopping Board").addStep("Oven Tray"),
                new RecipeIngredient("Bread Slice")
                    .addStep("Chopping Board").addStep("Oven Tray")
            ),
            importImage("/food/CheeseToast.png").getSubimage(16, 0, 16, 16),
            null,
            9, 2,
            RecipeTag.GOLDEN, RecipeTag.COMFORT          // 2 tags — uncommon
        );
        registerRecipe(grilledCheese);

        Recipe carrotSalad = new Recipe(
            "Carrot Salad",
            Arrays.asList(
                new RecipeIngredient("Carrot").addStep("Chopping Board"),
                new RecipeIngredient("Red Onion").addStep("Chopping Board"),
                new RecipeIngredient("Greens").addStep("Chopping Board")
            ),
            importImage("/food/CarrotSalad.png").getSubimage(0, 0, 16, 16),
            null,
            7, 2,
            RecipeTag.RAW, RecipeTag.HEARTY              // 2 tags — uncommon
        );
        registerRecipe(carrotSalad);

        Recipe roastChicken = new Recipe(
            "Roast Chicken",
            Arrays.asList(
                new RecipeIngredient("Chicken").addStep("Oven Tray"),
                new RecipeIngredient("Potato").addStep("Oven Tray"),
                new RecipeIngredient("Greens")
                    .addStep("Chopping Board").addStep("Oven Tray")
            ),
            importImage("/food/RoastChicken.png").getSubimage(0, 0, 16, 16),
            null,
            11, 2,
            RecipeTag.GOLDEN, RecipeTag.HEARTY, RecipeTag.COMFORT  // 3 tags — rare
        );
        registerRecipe(roastChicken);

        Recipe vegRoast = new Recipe(
            "Vegetable Roast",
            Arrays.asList(
                new RecipeIngredient("Carrot")
                    .addStep("Chopping Board").addStep("Oven Tray"),
                new RecipeIngredient("Potato").addStep("Oven Tray"),
                new RecipeIngredient("Aubergine").addStep("Oven Tray")
            ),
            importImage("/food/VegRoast.png").getSubimage(0, 0, 16, 16),
            null,
            10, 2,
            RecipeTag.GOLDEN, RecipeTag.HEARTY           // 2 tags — uncommon
        );
        registerRecipe(vegRoast);

        Recipe aubergineBake = new Recipe(
            "Aubergine Bake",
            Arrays.asList(
                new RecipeIngredient("Chopped Tomatoes")
                    .addStep("Chopping Board").addStep("Oven Tray"),
                new RecipeIngredient("Cheese")
                    .addStep("Chopping Board").addStep("Oven Tray"),
                new RecipeIngredient("Aubergine")
                    .addStep("Chopping Board").addStep("Oven Tray")
            ),
            importImage("/food/AubergineBake.png").getSubimage(0, 0, 16, 16),
            null,
            10, 2,
            RecipeTag.GOLDEN, RecipeTag.HEARTY           // 2 tags — uncommon
        );
        registerRecipe(aubergineBake);

        Recipe lasagna = new Recipe(
            "Lasagna",
            Arrays.asList(
                new RecipeIngredient("Lasagna").addStep("Oven Tray"),
                new RecipeIngredient("Chopped Tomatoes")
                    .addStep("Chopping Board").addStep("Oven Tray"),
                new RecipeIngredient("Cheese")
                    .addStep("Chopping Board").addStep("Oven Tray")
            ),
            importImage("/food/pasta/Lasagna.png").getSubimage(32, 0, 16, 16),
            null,
            10, 2,
            RecipeTag.GOLDEN, RecipeTag.HEARTY, RecipeTag.COMFORT  // 3 tags — rare
        );
        registerRecipe(lasagna);

        Recipe pastaBake = new Recipe(
            "Pasta Bake",
            Arrays.asList(
                new RecipeIngredient("Penne")
                    .addStep("Small Pot").addStep("Oven Tray"),
                new RecipeIngredient("Chopped Tomatoes")
                    .addStep("Chopping Board").addStep("Oven Tray"),
                new RecipeIngredient("Cheese")
                    .addStep("Chopping Board").addStep("Oven Tray")
            ),
            importImage("/food/pasta/PastaBake.png").getSubimage(0, 0, 16, 16),
            null,
            10, 2,
            RecipeTag.GOLDEN, RecipeTag.HEARTY, RecipeTag.COMFORT  // 3 tags — rare
        );
        registerRecipe(pastaBake);

        // ── PHASE 3 ──────────────────────────────────────────────────────────

        Recipe chips = new Recipe(
            "Chips",
            Arrays.asList(
                new RecipeIngredient("Potato")
                    .addStep("Chopping Board").addStep("Freezer").addStep("Fryer")
            ),
            importImage("/food/potato/Fries.png").getSubimage(32, 0, 16, 16),
            null,
            10, 3,
            RecipeTag.FROZEN, RecipeTag.CRISPY           // 2 tags — uncommon
        );
        registerRecipe(chips);

        Recipe friedChicken = new Recipe(
            "Fried Chicken",
            Arrays.asList(
                new RecipeIngredient("Chicken Pieces")
                    .addStep("Chopping Board").addStep("Coated")
                    .addStep("Freezer").addStep("Fryer")
            ),
            importImage("/food/coating/BreadCrumbs.png").getSubimage(64, 0, 16, 16),
            null,
            11, 3,
            RecipeTag.FROZEN, RecipeTag.CRISPY   // 2 tags — uncommon
        );
        registerRecipe(friedChicken);

        Recipe chickenAndChips = new Recipe(
            "Chicken and Chips",
            Arrays.asList(
                new RecipeIngredient("Chicken Pieces")
                    .addStep("Chopping Board").addStep("Coated")
                    .addStep("Freezer").addStep("Fryer"),
                new RecipeIngredient("Potato")
                    .addStep("Chopping Board").addStep("Freezer").addStep("Fryer")
            ),
            importImage("/food/coating/BreadCrumbs.png").getSubimage(80, 0, 16, 16),
            null,
            13, 3,
            RecipeTag.FROZEN, RecipeTag.CRISPY, RecipeTag.HEARTY   // 3 tags — rare
        );
        registerRecipe(chickenAndChips);

        // ── Starting unlocks ─────────────────────────────────────────────────
        unlockRecipe(fish);
        unlockRecipe(egg);
        unlockRecipe(friedEgg);
        unlockRecipe(bruschetta);
        unlockRecipe(salad);
        unlockRecipe(steak);

        // ── Icons ─────────────────────────────────────────────────────────────
        PanIcon      = importImage("/UI/recipe/Icons.png").getSubimage(0,   0, 16, 16);
        choppedIcon  = importImage("/UI/recipe/Icons.png").getSubimage(32,  0, 16, 16);
        PotIcon      = importImage("/UI/recipe/Icons.png").getSubimage(16,  0, 16, 16);
        ovenIcon     = importImage("/UI/recipe/Icons.png").getSubimage(48,  0, 16, 16);
        fryerIcon    = importImage("/UI/recipe/Icons.png").getSubimage(64,  0, 16, 16);
        freezerIcon  = importImage("/UI/recipe/Icons.png").getSubimage(80,  0, 16, 16);
        seasoningIcon= importImage("/UI/recipe/Icons.png").getSubimage(96,  0, 16, 16);
        ovenTrayIcon = importImage("/UI/recipe/Icons.png").getSubimage(112, 0, 16, 16);
        coatedIcon   = importImage("/food/coating/BreadCrumbs.png").getSubimage(0, 0, 16, 16);
        
        flame   = importImage("/UI/Tags.png").getSubimage(0, 0, 16, 16);
        golden   = importImage("/UI/Tags.png").getSubimage(16, 0, 16, 16);
        frozen   = importImage("/UI/Tags.png").getSubimage(32, 0, 16, 16);
        crispy   = importImage("/UI/Tags.png").getSubimage(48, 0, 16, 16);
        hearty   = importImage("/UI/Tags.png").getSubimage(64, 0, 16, 16);
        comfort   = importImage("/UI/Tags.png").getSubimage(80, 0, 16, 16);
        raw   = importImage("/UI/Tags.png").getSubimage(96, 0, 16, 16);
        delicate   = importImage("/UI/Tags.png").getSubimage(96+16, 0, 16, 16);
        aromatic   = importImage("/UI/Tags.png").getSubimage(96+32, 0, 16, 16);
        cursed   = importImage("/UI/Tags.png").getSubimage(96+32, 0, 16, 16);
    }

    // ── Static helpers ────────────────────────────────────────────────────────

    public void unlockAllRecipes() {
        unlockedRecipes.clear();
        for (Recipe recipe : allRecipes) unlockRecipe(recipe);
    }

    public static void registerRecipe(Recipe recipe) {
        allRecipes.add(recipe);
    }

    /** Returns all unlocked recipes that share at least one of the given tags. */
    public static List<Recipe> getRecipesByTags(List<RecipeTag> tags) {
        List<Recipe> result = new ArrayList<>();
        for (Recipe r : unlockedRecipes) {
            if (r.hasAnyTag(tags)) result.add(r);
        }
        return result;
    }

    /** Returns all unlocked recipes that have ALL of the given tags. */
    public static List<Recipe> getRecipesByAllTags(List<RecipeTag> tags) {
        List<Recipe> result = new ArrayList<>();
        for (Recipe r : unlockedRecipes) {
            boolean hasAll = true;
            for (RecipeTag t : tags) {
                if (!r.hasTag(t)) { hasAll = false; break; }
            }
            if (hasAll) result.add(r);
        }
        return result;
    }

    public Recipe chooseChefSpecial(List<Recipe> todaysMenu) {
        List<Recipe> candidates = unlockedRecipes.stream()
                .filter(r -> !todaysMenu.contains(r))
                .toList();
        if (candidates.isEmpty()) return null;
        return candidates.get(random.nextInt(candidates.size()));
    }

    public static Recipe[] getTwoRandomLocked(GamePanel gp) {
        int currentPhase = gp.world.progressM.currentPhase;
        List<Recipe> locked = getLockedRecipes().stream()
                .filter(r -> r.phase <= currentPhase)
                .toList();
        if (locked.size() < 2) return null;
        Recipe r1 = locked.get(random.nextInt(locked.size()));
        Recipe r2;
        do { r2 = locked.get(random.nextInt(locked.size())); } while (r1 == r2);
        return new Recipe[]{ r1, r2 };
    }

    public static Recipe getRandomLocked(GamePanel gp) {
        int currentPhase = gp.world.progressM.currentPhase;
        List<Recipe> locked = getLockedRecipes().stream()
                .filter(r -> r.phase <= currentPhase)
                .toList();
        if (locked.isEmpty()) return null;
        return locked.get(random.nextInt(locked.size()));
    }

    public static List<Recipe> getLockedRecipes() {
        List<Recipe> locked = new ArrayList<>(allRecipes);
        locked.removeAll(unlockedRecipes);
        return locked;
    }

    public static void unlockRecipe(Recipe recipe) {
        if (!unlockedRecipes.contains(recipe)) unlockedRecipes.add(recipe);
    }

    public static ArrayList<Recipe> getUnlockedRecipes() {
        return new ArrayList<>(unlockedRecipes);
    }

    public static List<Recipe> getAllRecipes() {
        return new ArrayList<>(allRecipes);
    }

    public static Recipe getMatchingRecipe(List<PreparedIngredient> plate) {
        for (Recipe recipe : unlockedRecipes) {
            if (recipe.matches(plate)) return recipe;
        }
        return null;
    }

    public TextureRegion getIconFromName(String name) {
        switch (name) {
            case "Frying Pan":    return PanIcon;
            case "Small Pot":     return PotIcon;
            case "Chopping Board":return choppedIcon;
            case "Oven":          return ovenIcon;
            case "Fryer":         return fryerIcon;
            case "Freezer":       return freezerIcon;
            case "Seasoning":     return seasoningIcon;
            case "Oven Tray":     return ovenTrayIcon;
        }
        return null;
    }
    public TextureRegion getTagIconFromName(RecipeTag tag) {
        switch (tag) {
            case FLAME:    return flame;
            case GOLDEN:     return golden;
            case FROZEN: return frozen;
            case CRISPY:          return crispy;
            case HEARTY:         return hearty;
            case COMFORT:       return comfort;
            case RAW:     return raw;
            case DELICATE:     return delicate;
            case AROMATIC:     return aromatic;
            case CURSED:     return cursed;
        }
        return null;
    }

    public static Recipe getRandomRecipe() {
        if (unlockedRecipes.isEmpty()) return null;
        return unlockedRecipes.get(random.nextInt(unlockedRecipes.size()));
    }

    public static void addOrder(Order order)    { currentOrders.add(order); }
    public static void removeOrder(Order order) { currentOrders.remove(order); }

    public static boolean completeOrder(List<PreparedIngredient> plate) {
        for (int i = 0; i < currentOrders.size(); i++) {
            Recipe order = currentOrders.get(i).getRecipe();
            if (order.matches(plate)) {
                order.incrementCookCount();
                currentOrders.remove(i);
                return true;
            }
        }
        return false;
    }

    private Texture importImage(String filePath) {
        return AssetPool.getTexture(filePath);
    }

    public static List<Order> getCurrentOrders() { return currentOrders; }

    public static RecipeSaveData saveRecipeData() {
        RecipeSaveData data = new RecipeSaveData();
        List<String>  names      = new ArrayList<>();
        List<Integer> stars      = new ArrayList<>();
        List<Integer> cookCounts = new ArrayList<>();
        List<Boolean> mastered   = new ArrayList<>();

        for (Recipe r : unlockedRecipes) {
            names.add(r.getName());
            stars.add(r.getStarLevel());
            cookCounts.add(r.getTimesCooked());
            mastered.add(r.isMastered());
        }
        data.unlockedRecipesNames = names;
        data.starLevels           = stars;
        data.cookCounts           = cookCounts;
        data.masteredFlags        = mastered;
        return data;
    }

    public static Recipe getRecipeByName(String name) {
        for (Recipe recipe : allRecipes) {
            if (recipe.getName().equals(name)) return recipe;
        }
        return null;
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