package utility.save;

import main.GamePanel;

public class PlayerSaveData extends SaveData {
	
    public String username;
    public int skinNum;
    public int hairNum;
    public int hairStyleNum;
    public int level;
    public int soulsServed;
    public int nextLevelAmount;
    public int wealth;
    public float x;   // player position
    public float y;
    public int currentRoomIndex;

    public String currentItemName;
    public int currentItemState;
	
	public void applySaveData(GamePanel gp) {
		gp.player.applySaveData(this);
	}
    
}
