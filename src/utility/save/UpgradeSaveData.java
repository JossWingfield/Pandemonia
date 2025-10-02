package utility.save;

import java.util.List;

import main.GamePanel;
import utility.UpgradeManager;

public class UpgradeSaveData extends SaveData {
	
    public List<String> unlockedUpgradeNames;
	
	public void applySaveData(GamePanel gp) {
		UpgradeManager.applySaveData(unlockedUpgradeNames);
	}
	
}
