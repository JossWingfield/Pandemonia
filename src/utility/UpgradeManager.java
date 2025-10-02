package utility;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import main.GamePanel;
import utility.ProgressManager.RewardType;
import utility.Upgrade.Tier;
import utility.save.RecipeSaveData;
import utility.save.UpgradeSaveData;

public class UpgradeManager {
	
    private static final List<Upgrade> allUpgrades = new ArrayList<>();   // all possible recipes
    private static List<Upgrade> unlockedUpgrades = new ArrayList<>();
	
    private static final Random random = new Random();
    
    public UpgradeManager(GamePanel gp) {

        registerUpgrade(new Upgrade(gp, "Tip Jar",
        		"Customers tip money /nbased on service speed!",
        		importImage("/decor/kitchen props.png").getSubimage(0, 0, 16, 16),
        		Tier.EARLY,
        		RewardType.BASIC));
        registerUpgrade(new Upgrade(gp, "Turntable",
        		"Customers become 20% /nmore patient! /n(Also allows you to /nplay music)",
        		importImage("/decor/turntable.png").getSubimage(0, 0, 48, 48),
        		Tier.EARLY,
        		RewardType.BASIC,
        		48+8, 48+48+8));
        
        registerUpgrade(new Upgrade(gp, "Cosmetic 1",
        		"Customers become 20% /nmore patient! /n(Also allows you to /nplay music)",
        		importImage("/decor/turntable.png").getSubimage(0, 0, 48, 48),
        		Tier.EARLY,
        		RewardType.COSMETIC,
        		48+8, 48+48+8));
        registerUpgrade(new Upgrade(gp, "Cosmetic 2",
        		"Customers become 20% /nmore patient! /n(Also allows you to /nplay music)",
        		importImage("/decor/turntable.png").getSubimage(0, 0, 48, 48),
        		Tier.EARLY,
        		RewardType.COSMETIC,
        		48+8, 48+48+8));
        registerUpgrade(new Upgrade(gp, "Kitchen 1",
        		"Customers become 20% /nmore patient! /n(Also allows you to /nplay music)",
        		importImage("/decor/turntable.png").getSubimage(0, 0, 48, 48),
        		Tier.EARLY,
        		RewardType.KITCHEN,
        		48+8, 48+48+8));
        registerUpgrade(new Upgrade(gp, "Kitchen 2",
        		"Customers become 20% /nmore patient! /n(Also allows you to /nplay music)",
        		importImage("/decor/turntable.png").getSubimage(0, 0, 48, 48),
        		Tier.EARLY,
        		RewardType.KITCHEN,
        		48+8, 48+48+8));
    	
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
    public static void registerUpgrade(Upgrade u) {
    	allUpgrades.add(u);
    }
    public static Upgrade[] getTwoRandomLocked(Tier tier) {
        List<Upgrade> locked = getLockedUpgradesByTier(tier);
        if (locked.isEmpty()) return null;

        Upgrade r1 = locked.get(random.nextInt(locked.size()));
        if (locked.size() == 1) return new Upgrade[]{ r1 };

        Upgrade r2;
        do {
            r2 = locked.get(random.nextInt(locked.size()));
        } while (r1 == r2);
        return new Upgrade[]{ r1, r2 };
    }
    public static Upgrade[] getTwoRandomLockedForCategory(RewardType category, Tier tier) {
        List<Upgrade> locked = new ArrayList<>();
        for (Upgrade u : allUpgrades) {
            if (u.getTier() == tier && u.getCategory() == category && !unlockedUpgrades.contains(u)) {
                locked.add(u);
            }
        }
        if (locked.isEmpty()) return null;

        Upgrade r1 = locked.get(random.nextInt(locked.size()));
        if (locked.size() == 1) return new Upgrade[]{ r1 };

        Upgrade r2;
        do {
            r2 = locked.get(random.nextInt(locked.size()));
        } while (r1 == r2);

        return new Upgrade[]{ r1, r2 };
    }
    public static List<Upgrade> getLockedUpgradesByTier(Tier tier) {
        List<Upgrade> locked = new ArrayList<>();
        for (Upgrade u : allUpgrades) {
            if (u.getTier() == tier && !unlockedUpgrades.contains(u)) {
                locked.add(u);
            }
        }
        return locked;
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
        	upgrade.activateUpgrade();
        }
    }
    public static UpgradeSaveData saveUpgradeData() {
    	UpgradeSaveData data = new UpgradeSaveData();
    	List<String> names = new ArrayList<String>();
    	for(Upgrade r: unlockedUpgrades) {
    		names.add(r.getName());
    	}
    	data.unlockedUpgradeNames = names;
    	return data;
    }
    public static void applySaveData(List<String> unlockedNames) {
        unlockedUpgrades.clear();
        for (String name : unlockedNames) {
            for (Upgrade r : allUpgrades) {
                if (r.getName().equals(name)) {
                    unlockUpgrade(r);
                }
            }
        }
    }
	
}
