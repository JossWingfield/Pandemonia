package utility.save;

import main.GamePanel;

public class WorldSaveSettings extends SaveData{
	
	  public String username;
	  public int level;
	  public int soulsServed;
	  public int nextLevelAmount;
	  public int wealth;
	  public float x;
	  public float y;
	  public int currentRoomIndex;

	  public String currentItemName;
	  public int currentItemState;
		
	  public void applySaveData(GamePanel gp) {
		  gp.world.setSaveData(this);
	  }
	
}
