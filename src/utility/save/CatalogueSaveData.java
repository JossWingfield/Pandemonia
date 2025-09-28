package utility.save;

import java.util.ArrayList;
import java.util.List;

import entity.buildings.Building;
import main.GamePanel;
import map.Beam;
import map.FloorPaper;
import map.WallPaper;
import utility.Recipe;
import utility.RecipeManager;

public class CatalogueSaveData extends SaveData {
	
	public List<BuildingSaveData> decorBuildingInventory = new ArrayList<BuildingSaveData>();
	public List<BuildingSaveData> kitchenBuildingInventory = new ArrayList<BuildingSaveData>();
	public List<Integer> floorpaperInventory = new ArrayList<>();
	public List<Integer> wallpaperInventory = new ArrayList<>();
	public List<Integer> beamInventory = new ArrayList<>();
	public List<BuildingSaveData> storeBuildingInventory = new ArrayList<BuildingSaveData>();
	public List<BuildingSaveData> bathroomBuildingInventory = new ArrayList<BuildingSaveData>();
	
	public void applySaveData(GamePanel gp) {
		gp.catalogue.applySaveData(this);
	}
	
}
