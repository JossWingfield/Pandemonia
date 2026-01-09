package utility;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import entity.buildings.Building;
import entity.buildings.Rubble;
import entity.buildings.WallDecor_Building;
import main.GamePanel;
import main.renderer.AssetPool;
import main.renderer.Colour;
import main.renderer.Texture;
import main.renderer.TextureRegion;
import utility.save.ProgressSaveData;
import utility.save.StatisticsSaveData;

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
        rewardMap.put(4, RewardType.COSMETIC);
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
    public TextureRegion[][] levelRewards; // assign rewards for each level
    private TextureRegion basicReward, kitchenReward, cosmeticReward, emptyReward;
    private TextureRegion basicReward2, kitchenReward2, cosmeticReward2, emptyReward2;
    public int totalLevels = 100; // total number of levels
    public int currentPhase = 1;
    
    //UPGRADE STUFF
    public boolean turntablePresent = false;
    public boolean tipJarPresent = false;
    public boolean bigTipsPresent = false;
    
    public boolean fridgeUpgradeI = false;
    public boolean fridgeUpgradeII = false;
    public boolean sinkUpgradeI = false;
    public boolean stoveUpgradeI = false;
    public boolean choppingBoardUpgradeI = false;
    public boolean ovenUpgradeI = false;
    
    public boolean fasterCustomers = false;
    public boolean moreCustomers = false;
    
    //KITCHEN UNLOCKS
    public boolean seasoningUnlocked = false;
    //ROOM UNLOCKS
    public boolean unlockedKitchen = false;
    
    //ACHIEVEMENTS
    public Map<String, Achievement> achievements = new LinkedHashMap<>();
    
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
        levelRewards = new TextureRegion[totalLevels][2];
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
        //unlockOldKitchen();
        setupAchievements();
    }
    private void setupAchievements() {

    	Achievement a = new Achievement(gp,
    	    "first_steps",
    	    "First Steps",
    	    "Reach level 1",
    	    basicReward
    	);

    	a.setOnUnlock(() -> {
    		gp.player.wealth +=5;
    		gp.gui.addMessage("Earned +5 coins!", Colour.YELLOW);
    	});
    	achievements.put(a.getId(), a);
    	
    	a = new Achievement(gp,
        	    "steady_pace",
        	    "Steady Pace",
        	    "Reach level 5",
        	    basicReward
        	);

        	a.setOnUnlock(() -> {
        		gp.player.wealth +=10;
        		gp.gui.addMessage("Earned +10 coins!", Colour.YELLOW);
        	});
        	
        	achievements.put(a.getId(), a);
        	a = new Achievement(gp,
            	    "veteran_chef",
            	    "Veteran Chef",
            	    "Reach level 20",
            	    basicReward
            	);

            	a.setOnUnlock(() -> {
            		gp.player.wealth +=20;
            		gp.gui.addMessage("Earned +20 coins!", Colour.YELLOW);
            	});
            	achievements.put(a.getId(), a);
    	
        a = new Achievement(gp,
    		   "experienced_chef",
    		   "Experienced Chef",
    		   "Reach level 50",
    		   basicReward
    		   );
        a.setOnUnlock(() -> {
    	   gp.player.wealth +=30;
    	   gp.gui.addMessage("Earned +30 coins!", Colour.YELLOW);
    	   });
        achievements.put(a.getId(), a);    	
        
        a = new Achievement(gp,
     		   "head_chef",
     		   "Head Chef",
     		   "Reach level 75",
     		   basicReward
     		   );
         a.setOnUnlock(() -> {
     	   gp.player.wealth +=40;
     	   gp.gui.addMessage("Earned +40 coins!", Colour.YELLOW);
     	   });
         achievements.put(a.getId(), a);  
         
         a = new Achievement(gp,
       		   "kitchen_legend",
       		   "Kitchen Legend",
       		   "Reach level 100",
       		   basicReward
       		   );
           a.setOnUnlock(() -> {
       	   gp.player.wealth +=50;
       	   gp.gui.addMessage("Earned +50 coins!", Colour.YELLOW);
       	   });
           achievements.put(a.getId(), a);  
       
       //RECIPES
   		a = new Achievement(gp,
    	    "10_recipes",
    	    "Collector",
    	    "Collect 10 Recipes",
    	    emptyReward
    	);
   		
    	a.setOnUnlock(() -> {
    		gp.gui.addMessage("Random recipe unlocked!", Colour.YELLOW);
    	    gp.recipeM.unlockRecipe(gp.recipeM.getRandomLocked(gp));
    	});
    	achievements.put(a.getId(), a);
   		a = new Achievement(gp,
   	    	    "20_recipes",
   	    	    "Cook Book",
   	    	    "Collect 20 Recipes",
   	    	    emptyReward
   	    	);
   	   		
   	    	a.setOnUnlock(() -> {
   	    		gp.gui.addMessage("Random recipe unlocked!", Colour.YELLOW);
   	    	    gp.recipeM.unlockRecipe(gp.recipeM.getRandomLocked(gp));
   	    	});
   	    	achievements.put(a.getId(), a);
   	  		a = new Achievement(gp,
   	   	    	    "50_recipes",
   	   	    	    "Versatile Chef",
   	   	    	    "Collect 50 Recipes",
   	   	    	    emptyReward
   	   	    	);
   	   	    	a.setOnUnlock(() -> {
   	   	    		gp.gui.addMessage("Random recipe unlocked!", Colour.YELLOW);
   	   	    	    gp.recipeM.unlockRecipe(gp.recipeM.getRandomLocked(gp));
   	   	    	});
   	   	    achievements.put(a.getId(), a);
   	  		a = new Achievement(gp,
   	   	    	    "75_recipes",
   	   	    	    "Recipe Archive",
   	   	    	    "Collect 75 Recipes",
   	   	    	    emptyReward
   	   	    	);
   	   	    	a.setOnUnlock(() -> {
   	   	    		gp.gui.addMessage("Random recipe unlocked!", Colour.YELLOW);
   	   	    	    gp.recipeM.unlockRecipe(gp.recipeM.getRandomLocked(gp));
   	   	    	});
   	   	    achievements.put(a.getId(), a);
   	   	    
   	  		a = new Achievement(gp,
   	   	    	    "100_recipes",
   	   	    	    "Culinary Wizard",
   	   	    	    "Collect 100 Recipes",
   	   	    	    emptyReward
   	   	    	);
   	   	    	a.setOnUnlock(() -> {
   	   	    		gp.gui.addMessage("Random recipe unlocked!", Colour.YELLOW);
   	   	    	    gp.recipeM.unlockRecipe(gp.recipeM.getRandomLocked(gp));
   	   	    	});
   	   	    achievements.put(a.getId(), a);
   	   	    
   	    	a = new Achievement(gp,
   	        	    "cat_lover",
   	        	    "Cat Lover",
   	        	    "Have 2 cats in the restaurant at one time!",
   	        	    importImage("/npcs/pet/Cat1.png").getSubimage(0, 0, 48, 48)
   	        	);

   	        	a.setOnUnlock(() -> {
   	        		gp.gui.addMessage("Cat paintings added to catalogue!", Colour.YELLOW);
   	    			gp.catalogue.addToInventory(new WallDecor_Building(gp, 0, 0, 0));
   	    			gp.catalogue.addToInventory(new WallDecor_Building(gp, 0, 0, 1));
   	        	});

   	       achievements.put(a.getId(), a);
  	    	a = new Achievement(gp,
   	        	    "cat_cafe",
   	        	    "Cat cafe",
   	        	    "Have 3 cats in the restaurant at one time!",
   	        	    importImage("/npcs/pet/Cat1.png").getSubimage(0, 0, 48, 48)
   	        	);

   	        	a.setOnUnlock(() -> {
   	        		gp.gui.addMessage("Cat hat unlocked!", Colour.YELLOW);
   	    			//TODO add some sort of cat thing(possibly a hat)
   	        	});
   	       achievements.put(a.getId(), a);
   	       
   	       a = new Achievement(gp,
   	   	    	    "100_chopped",
   	   	    	    "Knife Skills",
   	   	    	    "Cut 100 ingredients",
   	   	    	    kitchenReward
   	   	    );
   	   	   a.setOnUnlock(() -> {
   	   		   gp.player.wealth +=5;
   	       	   gp.gui.addMessage("Earned +5 coins!", Colour.YELLOW);
   	       	   //TODO change this reward
   	   	    });
   	   	   achievements.put(a.getId(), a);
   	   	   
   	       a = new Achievement(gp,
  	   	    	    "50_served",
  	   	    	    "Small Business",
  	   	    	    "Serve 50 customers",
  	   	    	    importImage("/npcs/FaceIcons.png").getSubimage(0, 0, 32, 32)
  	   	    );
  	   	   a.setOnUnlock(() -> {
  	   		   gp.player.wealth +=5;
  	       	   gp.gui.addMessage("Earned +5 coins!", Colour.YELLOW);
  	       	   //TODO change this reward
  	   	    });
  	   	   achievements.put(a.getId(), a);
  	   	   
   	       a = new Achievement(gp,
 	   	    	    "100_served",
 	   	    	    "Popular Local",
 	   	    	    "Serve 100 customers",
 	   	    	    importImage("/npcs/FaceIcons.png").getSubimage(32, 0, 32, 32)
 	   	    );
 	   	   a.setOnUnlock(() -> {
 	   		   gp.player.wealth +=5;
 	       	   gp.gui.addMessage("Earned +5 coins!", Colour.YELLOW);
 	       	   //TODO change this reward
 	   	    });
 	   	   achievements.put(a.getId(), a);
 	   	   
   	       a = new Achievement(gp,
	   	    	    "500_served",
	   	    	    "Hot Spot",
	   	    	    "Serve 500 customers",
	   	    	    importImage("/npcs/FaceIcons.png").getSubimage(64, 0, 32, 32)
	   	    );
	   	   a.setOnUnlock(() -> {
	   		   gp.player.wealth +=5;
	       	   gp.gui.addMessage("Earned +5 coins!", Colour.YELLOW);
	       	   //TODO change this reward
	   	    });
	   	   achievements.put(a.getId(), a);
	   	   
   	       a = new Achievement(gp,
	   	    	    "1000_served",
	   	    	    "Critically Acclaimed",
	   	    	    "Serve 1000 customers",
	   	    	    importImage("/npcs/FaceIcons.png").getSubimage(96, 0, 32, 32)
	   	    );
	   	   a.setOnUnlock(() -> {
	   		   gp.player.wealth +=5;
	       	   gp.gui.addMessage("Earned +5 coins!", Colour.YELLOW);
	       	   //TODO change this reward
	   	    });
	   	   achievements.put(a.getId(), a);
	   	   
   	       a = new Achievement(gp,
	   	    	    "free_tip",
	   	    	    "Free Tip!",
	   	    	    "Claim your first tip!",
	   	 		    importImage("/decor/kitchen props.png").getSubimage(0, 0, 16, 16)
	   	    );
	   	   a.setOnUnlock(() -> {
	   		   gp.player.wealth +=10;
	       	   gp.gui.addMessage("Earned +10 coins!", Colour.YELLOW);
	   	    });
	   	   achievements.put(a.getId(), a);
	   	   
   	       a = new Achievement(gp,
	   	    	    "sweet_talk",
	   	    	    "Sweet Talk",
	   	    	    "Earn 200% more than the base value for an order.",
	   	 		    importImage("/UI/coin.png").toTextureRegion()
	   	    );
	   	   a.setOnUnlock(() -> {
	   		   gp.player.wealth +=20;
	       	   gp.gui.addMessage("Earned +20 coins!", Colour.YELLOW);
	   	    });
	   	   achievements.put(a.getId(), a);
	   	   
   	       a = new Achievement(gp,
	   	    	    "music_to_my_ears",
	   	    	    "Music to my ears",
	   	    	    "Place a turntable.",
	   	 		    importImage("/decor/turntable.png").getSubimage(0, 0, 48, 48)
	   	    );
	   	   a.setOnUnlock(() -> {
	       	   //gp.gui.addMessage("Earned +20 coins!", Colour.YELLOW);
	   		   //TODO change this reward
	   	    });
	   	   achievements.put(a.getId(), a);
	   	   
   	       a = new Achievement(gp,
	   	    	    "about_time",
	   	    	    "About Time",
	   	    	    "Upgrade a kitchen station.",
	   	 		    kitchenReward
	   	    );
	   	   a.setOnUnlock(() -> {
	       	   //gp.gui.addMessage("Earned +20 coins!", Colour.YELLOW);
	   		   //TODO change this reward
	   	    });
	   	   achievements.put(a.getId(), a);
	   	   
   	       a = new Achievement(gp,
	   	    	    "reinforced_workplace",
	   	    	    "Reinforced Workplace",
	   	    	    "Upgrade 5 kitchen stations.",
	   	 		    kitchenReward
	   	    );
	   	   a.setOnUnlock(() -> {
	       	   //gp.gui.addMessage("Earned +20 coins!", Colour.YELLOW);
	   		   //TODO change this reward
	   	    });
	   	   achievements.put(a.getId(), a);
	   	   
   	       a = new Achievement(gp,
	   	    	    "interior_designer",
	   	    	    "Interior Designer",
	   	    	    "Place 20 decorations",
	   	    	 importImage("/decor/plants.png").getSubimage(16, 64, 16, 32)
	   	    );
	   	   a.setOnUnlock(() -> {
	       	   //gp.gui.addMessage("Earned +20 coins!", Colour.YELLOW);
	   		   //TODO change this reward
	   	    });
	   	   achievements.put(a.getId(), a);
    }

    public void handleLevelUp(int newLevel) {
    	if(newLevel-1 == 1) {
    		achievements.get("first_steps").unlock();
    	} else if(newLevel-1 == 5) {
    		achievements.get("steady_pace").unlock();
    	} else if(newLevel-1 == 20) {
    		achievements.get("veteran_chef").unlock();
    	} else if(newLevel-1 == 50) {
    		achievements.get("experienced_chef").unlock();
    	} else if(newLevel-1 == 75) {
    		achievements.get("head_chef").unlock();
    	} else if(newLevel-1 == 100) {
    		achievements.get("kitchen_legend").unlock();
    	}
    	
        // Always give recipe choices
        recipeChoices = RecipeManager.getTwoRandomLocked(gp);

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
                                    reward, currentPhase);
                if (upgradeChoices != null) {
                    // Show both upgrades and recipes (you can design GUI to display both)
                    gp.currentState = gp.chooseUpgradeState;
                } else {
                    gp.currentState = gp.chooseRecipeState; // fallback
                }
                break;

            case COSMETIC:
                upgradeChoices = UpgradeManager.getTwoRandomLockedForCategory(
                                    RewardType.COSMETIC, currentPhase);
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
    public void checkRecipeCollect() {
    	int num = gp.recipeM.getUnlockedRecipes().size();
    	if(num == 10) {
    		achievements.get("10_recipes").unlock();
    	} else if(num == 20){
      		achievements.get("20_recipes").unlock();
    	} else if(num == 50){
      		achievements.get("50_recipes").unlock();
    	} else if(num == 75){
      		achievements.get("75_recipes").unlock();
    	} else if(num == 100){
      		achievements.get("100_recipes").unlock();
    	}
    }
    public void checkKitchenUpgrade() {
		
		Statistics.kitchenUpgradeCount++;
		
		if(Statistics.kitchenUpgradeCount == 1) {
			achievements.get("about_time").unlock();
		} else if(Statistics.kitchenUpgradeCount == 5) {
			achievements.get("reinforced_workplace").unlock();
		}
		
    }
    public void checkDecorationsPlaced() {
    	if(Statistics.decorationsPlaced == 20) {
			achievements.get("interior_designer").unlock();
    	}
    }
    public Recipe[] getRecipeChoices() {
        return recipeChoices;
    }

    public Upgrade[] getUpgradeChoices() {
        return upgradeChoices;
    }
    protected Texture importImage(String filePath) {
		Texture texture = AssetPool.getTexture(filePath);
	    return texture;
	}
    public ProgressSaveData saveData() {
    	ProgressSaveData data = new ProgressSaveData();
    	data.fridgeUpgradeI = fridgeUpgradeI;
    	data.fridgeUpgradeII = fridgeUpgradeII;
    	data.sinkUpgradeI = sinkUpgradeI;
    	data.stoveUpgradeI = stoveUpgradeI;
    	data.ovenUpgradeI = ovenUpgradeI;
    	data.choppingBoardUpgradeI = choppingBoardUpgradeI;
    	data.fasterCustomers = fasterCustomers;
    	data.moreCustomers = moreCustomers;
    	data.phase = currentPhase;
    	data.seasoningUnlocked = seasoningUnlocked;
    	data.cutscenesWatched = gp.cutsceneM.getCutscenesWatched();
    	for (Achievement a : achievements.values()) {
    	    if (a.isUnlocked()) {
    	        data.unlockedAchievements.add(a.getId());
    	    }
    	}
    	return data;
    }
    public void applySaveData(ProgressSaveData data) {
    	currentPhase = data.phase;
    	fridgeUpgradeI = data.fridgeUpgradeI;
    	fridgeUpgradeII = data.fridgeUpgradeII;
    	sinkUpgradeI = data.sinkUpgradeI;
    	stoveUpgradeI = data.stoveUpgradeI;
    	ovenUpgradeI = data.ovenUpgradeI;
    	choppingBoardUpgradeI = data.choppingBoardUpgradeI;
    	fasterCustomers = data.fasterCustomers;
    	moreCustomers = data.moreCustomers;
    	seasoningUnlocked = data.seasoningUnlocked;
    	gp.cutsceneM.setCutscenesWatched(data.cutscenesWatched);
    	for (String id : data.unlockedAchievements) {
    		Achievement a = achievements.get(id);
    	    if (a != null) {
    	    	a.unlockNoReward();
    	    }
    	}
    }
    public StatisticsSaveData saveStatisticsData() {
    	StatisticsSaveData data = new StatisticsSaveData();
    	data.ingredientsChopped = Statistics.ingredientsChopped;
    	data.servedCustomers = Statistics.servedCustomers;
    	data.kitchenUpgradeCount = Statistics.kitchenUpgradeCount;
    	data.decorationsPlaced = Statistics.decorationsPlaced;
    	return data;
    }
    public void applySaveStatisticsData(StatisticsSaveData data) {
    	Statistics.ingredientsChopped = data.ingredientsChopped;
    	Statistics.servedCustomers = data.servedCustomers;
    	Statistics.kitchenUpgradeCount = data.kitchenUpgradeCount;
     	Statistics.decorationsPlaced = data.decorationsPlaced;
    }
    public void moveToNextPhase() {
    	currentPhase++;
    	gp.mapM.enterNewPhase();
    }
	public void unlockOldKitchen() {
		unlockedKitchen = true;
		RoomHelperMethods.KITCHEN = RoomHelperMethods.OLDKITCHEN;
		gp.gui.addMessage("Ignis has found peace.", Colour.GREEN);
		if(gp.mapM.isInRoom(9)) {
			gp.npcM.addCook();
			Rubble b = (Rubble)gp.buildingM.findBuildingWithName("Barricade");
	    	if(b != null) {
	    		b.explode();
	    	}
	    	Building boxes = gp.buildingM.findBuildingWithName("Packages 1");
	    	if(boxes != null) {
	    		gp.buildingM.removeBuilding(boxes);
	    	}
	    	Building Packages = gp.buildingM.findBuildingWithName("Boxes 1");
	    	if(Packages != null) {
	    		gp.buildingM.removeBuilding(Packages);
			}
		} else {
			gp.mapM.getRoom(9).addCook();
			Rubble b = (Rubble)gp.mapM.getRoom(9).findBuildingWithName("Barricade");
	    	if(b != null) {
	    		b.explode();
	    	}
	    	Building boxes = gp.mapM.getRoom(9).findBuildingWithName("Packages 1");
	    	if(boxes != null) {
	    		gp.mapM.getRoom(9).removeBuilding(boxes);
	    	}
	    	Building Packages = gp.mapM.getRoom(9).findBuildingWithName("Boxes 1");
	    	if(Packages != null) {
	    		gp.mapM.getRoom(9).removeBuilding(Packages);
			}
		}
		
	}
}
