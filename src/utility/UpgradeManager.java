package utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class UpgradeManager {
	
    private static final List<Upgrade> allUpgrades = new ArrayList<>();   // all possible recipes
    private static List<Upgrade> unlockedUpgrades = new ArrayList<>();
	
    private static final Random random = new Random();
    
    public UpgradeManager() {
    	Upgrade fish = new Upgrade(
            "Fish"
        );
        registerUpgrade(fish);
        
    	Upgrade salmon = new Upgrade(
                "Salmon"
        );
        registerUpgrade(salmon);
        
        Upgrade test = new Upgrade(
                "test"
        );
        registerUpgrade(test);
    	
    	
    }
    
    public static void registerUpgrade(Upgrade u) {
    	allUpgrades.add(u);
    }
    public static Upgrade[] getTwoRandomLocked() {
        List<Upgrade> locked = getLockedUpgrades();
        if (locked.isEmpty()) return null; // no upgrades left

        Upgrade r1 = locked.get(random.nextInt(locked.size()));
        if (locked.size() == 1) {
            // only one upgrade left, return just that
            return new Upgrade[] { r1 };
        }

        Upgrade r2;
        do {
            r2 = locked.get(random.nextInt(locked.size()));
        } while (r1 == r2);
        return new Upgrade[] { r1, r2 };
    }
    public static List<Upgrade> getLockedUpgrades() {
        List<Upgrade> locked = new ArrayList<>(allUpgrades);
        locked.removeAll(unlockedUpgrades);
        return locked;
    }
    // Unlocking system
    public static void unlockUpgrade(Upgrade upgrade) {
        if (!unlockedUpgrades.contains(upgrade)) {
        	unlockedUpgrades.add(upgrade);
        }
    }
	
}
