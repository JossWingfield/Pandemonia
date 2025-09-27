package utility.save;

import main.GamePanel;
import utility.Season;

public class WorldSaveData extends SaveData{
	
	  public int day;
	  public int previousSoulsCollected;
	
	  public Season currentSeason;
	  
	  public void applySaveData(GamePanel gp) {
		  gp.world.setSaveData(this);
	  }
	
}
