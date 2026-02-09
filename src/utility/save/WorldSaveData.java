package utility.save;

import java.util.ArrayList;
import java.util.List;

import main.GamePanel;
import utility.Season;

public class WorldSaveData extends SaveData{
	
	  public int day;
	  public int previousSoulsCollected;
	  public boolean crateOrdered;
	  public int crateNum;
	
	  public Season currentSeason;
	  
	  public void applySaveData(GamePanel gp) {
		  gp.world.gameM.setSaveData(this);
	  }
	
}
