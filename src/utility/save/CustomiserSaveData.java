package utility.save;

import java.util.ArrayList;
import java.util.List;

import main.GamePanel;

public class CustomiserSaveData extends SaveData {
	
	public List<BuildingSaveData> decorBuildingInventory = new ArrayList<BuildingSaveData>();
	public List<BuildingSaveData> kitchenBuildingInventory = new ArrayList<BuildingSaveData>();
	public List<Integer> floorpaperInventory = new ArrayList<>();
	public List<Integer> wallpaperInventory = new ArrayList<>();
	public List<Integer> beamInventory = new ArrayList<>();
	public List<BuildingSaveData> storeBuildingInventory = new ArrayList<BuildingSaveData>();
	public List<BuildingSaveData> bathroomBuildingInventory = new ArrayList<BuildingSaveData>();
	
	public void applySaveData(GamePanel gp) {
		gp.customiser.applySaveData(this);
	}
	
}
