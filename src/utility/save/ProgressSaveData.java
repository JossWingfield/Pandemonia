package utility.save;


import main.GamePanel;

public class ProgressSaveData extends SaveData {
	
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
	
	public void applySaveData(GamePanel gp) {
		gp.progressM.applySaveData(this);
	}
	
	
}
