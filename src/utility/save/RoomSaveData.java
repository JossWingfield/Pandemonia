package utility.save;

import java.util.ArrayList;
import java.util.List;

import entity.buildings.Building;
import entity.items.Item;
import entity.npc.NPC;
import main.GamePanel;
import map.Beam;
import map.FloorPaper;
import map.LightSource;
import map.WallPaper;

public class RoomSaveData  extends SaveData {
	
	public int roomNum;
	public WallPaper wallpaper;
	public FloorPaper floorpaper;
	public Beam beam;
	public Building[] buildings;
	public List<NPC> npcs = new ArrayList<>();
	public List<Item> items = new ArrayList<>(); 
	public List<LightSource> lights = new ArrayList<>(); 
    public int buildingArrayCounter;
    
	
	public void applySaveData(GamePanel gp) {
		gp.mapM.getRoom(roomNum).applySaveData(this);
	}
    
}
