package utility;

import java.util.Map;
import java.util.Random;

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
        rewardMap.put(2, RewardType.RECIPE);
        rewardMap.put(3, RewardType.KITCHEN);
        rewardMap.put(4, RewardType.RECIPE);
        rewardMap.put(5, RewardType.BASIC);
        rewardMap.put(6, RewardType.RECIPE);
        rewardMap.put(7, RewardType.COSMETIC);
        rewardMap.put(9, RewardType.KITCHEN);
        rewardMap.put(10, RewardType.COSMETIC);
    }

    // Current reward choices to display in the GUI
    private Recipe[] recipeChoices;
    private Upgrade[] upgradeChoices;

    public ProgressManager(GamePanel gp) {
        this.gp = gp;
    }

    public void handleLevelUp(int newLevel) {
        // Always give recipe choices
        recipeChoices = RecipeManager.getTwoRandomLocked();

        // See if this level has an extra reward
        RewardType reward = rewardMap.get(newLevel);

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
	
}
