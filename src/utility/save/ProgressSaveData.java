package utility.save;


import java.util.List;

import main.GamePanel;

public class ProgressSaveData extends SaveData {
	
	public String worldName;
	public boolean fridgeUpgradeI, fridgeUpgradeII;
	public boolean sinkUpgradeI;
	public boolean stoveUpgradeI;
	public boolean choppingBoardUpgradeI;
	public boolean ovenUpgradeI;
	public boolean moreCustomers;
	public boolean fasterCustomers;
	
	//UNLOCKS
	public boolean seasoningUnlocked;
	
	public int phase;
	public List<String> cutscenesWatched;
	public java.util.Set<String> unlockedAchievements = new java.util.HashSet<>();
	
	public void applySaveData(GamePanel gp) {
		gp.progressM.applySaveData(this);
	}
	
	
}
