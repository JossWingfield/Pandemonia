package utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.renderer.AssetPool;
import main.renderer.Texture;
import utility.ProgressManager.RewardType;
import utility.save.UpgradeSaveData;

public class UpgradeManager {
	
    private static final List<Upgrade> allUpgrades = new ArrayList<>();   // all possible recipes
    private static List<Upgrade> unlockedUpgrades = new ArrayList<>();
	
    private static final Random random = new Random();
    
    public UpgradeManager(GamePanel gp) {

        registerUpgrade(new Upgrade(gp, "Tip Jar",
        		"Customers tip money /nbased on service speed!",
        		importImage("/decor/kitchen props.png").getSubimage(0, 0, 16, 16),
        		1,
        		RewardType.BASIC));
        registerUpgrade(new Upgrade(gp, "Turntable",
        		"Customers become 20% /nmore patient! /n(Also allows you to /nplay music)",
        		importImage("/decor/turntable.png").getSubimage(0, 0, 48, 48),
        		1,
        		RewardType.BASIC,
        		48+8, 48+48+8));
        registerUpgrade(new Upgrade(gp, "More Customers",
        		"100% more customers /nappear!",
        		importImage("/npcs/FaceIcons.png").getSubimage(32*4, 0, 32, 32),
        		1,
        		RewardType.BASIC,
        		24, 24));
        registerUpgrade(new Upgrade(gp, "Faster Customers",
        		"Customers move 100% /nfaster!",
        		importImage("/npcs/FaceIcons.png").getSubimage(32*5, 0, 32, 32),
        		1,
        		RewardType.BASIC,
        		24, 24));
        
        registerUpgrade(new Upgrade(gp, "Shelves",
        		"Shelves are added /nto the catalogue!",
        		importImage("/decor/wall shelf.png").getSubimage(48, 16, 16, 16),
        		1,
        		RewardType.COSMETIC));
        registerUpgrade(new Upgrade(gp, "Small Plants",
        		"Small Plants are added /nto the catalogue!",
        		importImage("/decor/plants.png").getSubimage(64, 0, 32, 16),
        		1,
        		RewardType.COSMETIC, 32, 0));
        registerUpgrade(new Upgrade(gp, "Big Plants",
        		"Big Plants are added /nto the catalogue!",
        		importImage("/decor/plants.png").getSubimage(64, 64, 32, 32),
        		1,
        		RewardType.COSMETIC, 32, 32));
        registerUpgrade(new Upgrade(gp, "Flowers, Hanging Plants",
        		"Flowers and hanging /nplants are added to /nthe catalogue!",
        		importImage("/decor/plants.png").getSubimage(64, 96, 32, 32),
        		1,
        		RewardType.COSMETIC, 32, 28));
        registerUpgrade(new Upgrade(gp, "Paintings",
        		"Paintings are added to /nthe catalogue!",
        		importImage("/decor/painting.png").getSubimage(0, 0, 16, 16),
        		1,
        		RewardType.COSMETIC));
        
        registerUpgrade(new Upgrade(gp, "Fridge Upgrade I",
        		"Increases Fridge Space /nto 10!)",
        		importImage("/decor/kitchen props.png").getSubimage(112, 128, 16, 48),
        		1,
        		RewardType.KITCHEN,
        		0, 48));
        registerUpgrade(new Upgrade(gp, "Sink Upgrade I",
        		"Wash Dishes Faster!",
        		importImage("/decor/bathroom props.png").getSubimage(0, 192, 32, 32),
        		1,
        		RewardType.KITCHEN,
        		24, 24));
        registerUpgrade(new Upgrade(gp, "Stove Upgrade I",
        		"Cook food Faster!",
        		importImage("/decor/kitchen props.png").getSubimage(112, 16, 32, 16),
        		1,
        		RewardType.KITCHEN,
        		24, 0));
        registerUpgrade(new Upgrade(gp, "Chopping Upgrade I",
        		"Chop food Faster!",
        		importImage("/decor/kitchen props.png").getSubimage(112, 0, 16, 16),
        		1,
        		RewardType.KITCHEN));
        
        
        
        //PHASE 2
        registerUpgrade(new Upgrade(gp, "Coloured Walls",
        		"Coloured Walls are added /nto the catalogue.",
        		importImage("/UI/levels/decor/ColoredWallUI.png").toTextureRegion(),
        		2,
        		RewardType.COSMETIC));
        
        registerUpgrade(new Upgrade(gp, "Fridge Upgrade II",
        		"Increases Fridge Space /nto 15!)",
        		importImage("/decor/kitchen props.png").getSubimage(112, 128, 16, 48),
        		2,
        		RewardType.KITCHEN,
        		0, 48));
        registerUpgrade(new Upgrade(gp, "Oven Upgrade I",
        		"Oven cooks food Faster!",
        		importImage("/decor/kitchen props.png").getSubimage(48, 128, 32, 48),
        		2,
        		RewardType.KITCHEN,
        		24, 48+12));
        registerUpgrade(new Upgrade(gp, "Seasoning!",
        		"Some recipes must be /nseasoned. If done correctly /nearn a 50% bonus!",
        		importImage("/food/seasoning/basil.png").getSubimage(0, 16, 16, 16),
        		2,
        		RewardType.KITCHEN));
        
        //ADD SOUPS
    	
    }
    public Texture importImage(String filePath) {
		Texture texture = AssetPool.getTexture(filePath);
	    return texture;
	}
    public static void registerUpgrade(Upgrade u) {
    	allUpgrades.add(u);
    }
    public static Upgrade[] getTwoRandomLocked(int tier) {
        List<Upgrade> locked = getLockedUpgradesByTier(tier);
        if (locked.isEmpty()) return null;

        Upgrade r1 = locked.get(random.nextInt(locked.size()));
        if (locked.size() == 1) return new Upgrade[]{ r1 };

        Upgrade r2;
        do {
            r2 = locked.get(random.nextInt(locked.size()));
        } while (r1.getName().equals(r2.getName()));
        return new Upgrade[]{ r1, r2 };
    }
    public static Upgrade[] getTwoRandomLockedForCategory(RewardType category, int tier) {
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
    public static List<Upgrade> getLockedUpgradesByTier(int tier) {
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
    public static void unlockUpgradeWithoutActivation(Upgrade upgrade) {
        if (!unlockedUpgrades.contains(upgrade)) {
        	unlockedUpgrades.add(upgrade);
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
                	unlockUpgradeWithoutActivation(r);
                }
            }
        }
    }
	
}
