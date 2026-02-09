package utility.save;

import java.util.ArrayList;
import java.util.List;

import main.GamePanel;

public class OrderSaveData extends SaveData {
	  //ORDER
	  public boolean orderEmpty = true;
	  public List<BuildingSaveData> buildingInventory = new ArrayList<BuildingSaveData>();
	  public List<Integer> floorpaperInventory = new ArrayList<>();
	  public List<Integer> wallpaperInventory = new ArrayList<>();
	  public List<Integer> beamInventory = new ArrayList<>();
	  public List<Integer> chairSkinInventory = new ArrayList<>();
	  public List<Integer> tableSkinInventory = new ArrayList<>();
	  public List<Integer> counterSkinInventory = new ArrayList<>();
	  public List<Integer> doorSkinInventory = new ArrayList<>();
	  public List<Integer> panSkinInventory = new ArrayList<>();
	  
	  public void applySaveData(GamePanel gp) {
		  gp.world.gameM.setOrderData(this);
	  }
}
