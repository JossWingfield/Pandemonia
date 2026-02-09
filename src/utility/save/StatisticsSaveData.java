package utility.save;

import main.GamePanel;

public class StatisticsSaveData extends SaveData {
	
    public int ingredientsChopped;
    public int servedCustomers;
    public int kitchenUpgradeCount;
    public int decorationsPlaced;
	
	public void applySaveData(GamePanel gp) {
		gp.world.progressM.applySaveStatisticsData(this);
	}
	
}
