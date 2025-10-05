package utility.save;


import main.GamePanel;

public class ProgressSaveData extends SaveData {
	
	public boolean fridgeUpgradeI;
	public boolean sinkUpgradeI;
	public boolean stoveUpgradeI;
	public boolean choppingBoardUpgradeI;
	public boolean moreCustomers;
	public boolean fasterCustomers;
	public int phase;
	
	public void applySaveData(GamePanel gp) {
		gp.progressM.applySaveData(this);
	}
	
	
}
