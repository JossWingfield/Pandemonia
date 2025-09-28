package utility.save;

import java.util.ArrayList;
import java.util.List;

import main.GamePanel;

public class RoomSaveData  extends SaveData {
	
	public int roomNum;
	public int wallpaper;
	public int floorpaper;
	public int beam;
	public List<BuildingSaveData> buildings = new ArrayList<BuildingSaveData>();
	//public List<NPC> npcs = new ArrayList<>();
	//public List<Item> items = new ArrayList<>(); 
	//public List<LightSource> lights = new ArrayList<>(); 
    public int buildingArrayCounter;
    
	
	public void applySaveData(GamePanel gp) {
		gp.mapM.getRoom(roomNum).applySaveData(this);
	}
    
}
