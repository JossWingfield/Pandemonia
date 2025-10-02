package utility;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import main.GamePanel;
import utility.Upgrade.Tier;

public class ProgressManager {
	
    GamePanel gp;
    Random random = new Random();
    
    // Keeps track of level-specific rewards
    public enum RewardType {
        RECIPE,        // unlocks a new recipe
        KITCHEN,       // kitchen upgrades
        BASIC,         // generic progression boosts
        COSMETIC       // skins, decorations
    }

    // A container for rewards unlocked at a given level
    public static class LevelReward {
        public RewardType type;
        public String id; // could be recipe name, furniture id, etc.

        public LevelReward(RewardType type, String id) {
            this.type = type;
            this.id = id;
        }
    }
    
    private static Map<Integer, RewardType> rewardMap = new java.util.HashMap<>();

    static {
        rewardMap.put(1, RewardType.RECIPE);
        rewardMap.put(2, RewardType.COSMETIC);
        rewardMap.put(3, RewardType.KITCHEN);
        rewardMap.put(4, RewardType.RECIPE);
        rewardMap.put(5, RewardType.BASIC);
        rewardMap.put(6, RewardType.RECIPE);
        rewardMap.put(7, RewardType.COSMETIC);
        rewardMap.put(8, RewardType.KITCHEN);
        rewardMap.put(9, RewardType.COSMETIC);
    }

    // Current reward choices to display in the GUI
    private Recipe[] recipeChoices;
    private Upgrade[] upgradeChoices;
    
    //ROADMAP
    public float roadmapOffsetX = 0; // current offset for smooth scrolling
    public final int roadmapNodeSpacing = 60; // space between level nodes
    public final int roadmapY = 100; // vertical position of roadmap
    public BufferedImage[][] levelRewards; // assign rewards for each level
    private BufferedImage basicReward, kitchenReward, cosmeticReward, emptyReward;
    private BufferedImage basicReward2, kitchenReward2, cosmeticReward2, emptyReward2;
    public int totalLevels = 100; // total number of levels

    public ProgressManager(GamePanel gp) {
        this.gp = gp;
        
        basicReward = importImage("/UI/levels/BasicReward.png").getSubimage(0, 0, 24, 20);
        kitchenReward = importImage("/UI/levels/KitchenReward.png").getSubimage(0, 0, 24, 20);
        cosmeticReward = importImage("/UI/levels/CosmeticReward.png").getSubimage(0, 0, 24, 20);
        emptyReward = importImage("/UI/levels/EmptyReward.png").getSubimage(0, 0, 24, 20);
        
        basicReward2 = importImage("/UI/levels/BasicReward.png").getSubimage(24, 0, 24, 20);
        kitchenReward2 = importImage("/UI/levels/KitchenReward.png").getSubimage(24, 0, 24, 20);
        cosmeticReward2 = importImage("/UI/levels/CosmeticReward.png").getSubimage(24, 0, 24, 20);
        emptyReward2 = importImage("/UI/levels/EmptyReward.png").getSubimage(24, 0, 24, 20);
        
        int maxLevel = rewardMap.keySet().stream().max(Integer::compare).orElse(0);
        totalLevels = maxLevel;

        // build levelRewards for every level 1..maxLevel
        levelRewards = new BufferedImage[totalLevels][2];
        for (int i = 1; i <= totalLevels; i++) {
            RewardType reward = rewardMap.get(i); // direct lookup
            if (reward == null) {
                levelRewards[i - 1][0] = emptyReward; // default for no reward
                levelRewards[i - 1][1] = emptyReward2; // default for no reward
            } else {
                switch (reward) {
                    case BASIC: 
                    	levelRewards[i - 1][0] = basicReward;
                    	levelRewards[i - 1][1] = basicReward2;
                    break;
                    case KITCHEN: 
                    	levelRewards[i - 1][0] = kitchenReward;
                    	levelRewards[i - 1][1] = kitchenReward2;
                    break;
                    case COSMETIC:
                    	levelRewards[i - 1][0] = cosmeticReward;
                    	levelRewards[i - 1][1] = cosmeticReward2;
                    break;
                    case RECIPE: 
                    	levelRewards[i - 1][0] = emptyReward; 
                    	levelRewards[i - 1][1] = emptyReward2; 
                    break;
                }
            }
        }
    }

    public void handleLevelUp(int newLevel) {
        // Always give recipe choices
        recipeChoices = RecipeManager.getTwoRandomLocked();

        // See if this level has an extra reward
        RewardType reward = rewardMap.get(newLevel-1);

        if (reward == null) {
            // No special reward -> just recipes
            gp.currentState = gp.chooseRecipeState;
            return;
        }

        switch (reward) {
            case KITCHEN:
            case BASIC:
                upgradeChoices = UpgradeManager.getTwoRandomLockedForCategory(
                                    reward, getTierForLevel(newLevel));
                if (upgradeChoices != null) {
                    // Show both upgrades and recipes (you can design GUI to display both)
                    gp.currentState = gp.chooseUpgradeState;
                } else {
                    gp.currentState = gp.chooseRecipeState; // fallback
                }
                break;

            case COSMETIC:
                upgradeChoices = UpgradeManager.getTwoRandomLockedForCategory(
                                    RewardType.COSMETIC, getTierForLevel(newLevel));
                if (upgradeChoices != null) {
                    gp.currentState = gp.chooseUpgradeState; 
                } else {
                    gp.currentState = gp.chooseRecipeState; // fallback
                }
                break;

            case RECIPE:
                // Already handled above (always gives recipe)
                gp.currentState = gp.chooseRecipeState;
                break;
        }
    }

    public Recipe[] getRecipeChoices() {
        return recipeChoices;
    }

    public Upgrade[] getUpgradeChoices() {
        return upgradeChoices;
    }

    private Tier getTierForLevel(int level) {
        if (level < 15) return Tier.EARLY;
        else if (level < 30) return Tier.MID;
        else return Tier.LATE;
    }
    protected BufferedImage importImage(String filePath) { //Imports and stores the image
        BufferedImage importedImage = null;
        try {
            importedImage = ImageIO.read(getClass().getResourceAsStream(filePath));
        } catch(IOException e) {
            e.printStackTrace();
        }
        return importedImage;
    }
	
}
