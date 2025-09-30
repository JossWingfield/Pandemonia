package utility;

import java.util.Random;

import main.GamePanel;

public class ProgressManager {
	
    GamePanel gp;
    Random random = new Random();
    
    // Keeps track of level-specific rewards
    public enum RewardType {
        RECIPE,
        UPGRADE
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

    // Current reward choices to display in the GUI
    private Recipe[] recipeChoices;
    private Upgrade[] upgradeChoices;

    public ProgressManager(GamePanel gp) {
        this.gp = gp;
    }

    public void handleLevelUp(int newLevel) {
        // Always recipe choice
        recipeChoices = RecipeManager.getTwoRandomLocked();

        // Sometimes upgrades as well
        if (newLevel % 3 == 0) {
            upgradeChoices = getTwoRandomUpgrades();
            if (upgradeChoices != null) {
                gp.currentState = gp.chooseUpgradeState;
                return; // stop here if upgrades available
            }
        }


        gp.currentState = gp.chooseRecipeState;
    }


    public Recipe[] getRecipeChoices() {
        return recipeChoices;
    }

    public Upgrade[] getUpgradeChoices() {
        return upgradeChoices;
    }

    private Upgrade[] getTwoRandomUpgrades() {
        return UpgradeManager.getTwoRandomLocked();
    }
	
}
