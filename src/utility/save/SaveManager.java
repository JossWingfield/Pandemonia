package utility.save;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.google.gson.Gson;

import main.GamePanel;
import map.Room;
import utility.RecipeManager;

public class SaveManager {
	
	GamePanel gp;
	
	public SaveManager(GamePanel gp) {
		this.gp = gp;
	}
	
	public void saveGame() {
		saveToFile("save/player1.json", gp.player.toSaveData());
		saveToFile("save/settings1.json", gp.world.saveSettingsData());
		saveToFile("save/world1.json", gp.world.saveWorldData());
		saveToFile("save/recipes1.json", RecipeManager.saveRecipeData());
		saveToFile("save/catalogue1.json", gp.catalogue.saveCatalogueData());
		int counter = 0;
		for(Room r: gp.mapM.getRooms()) {
			if(r != null) {
				saveToFile("save/rooms1" + counter +".json", r.saveRoomData());
			}
			counter++;
		}
		saveToFile("save/order1.json", gp.world.saveOrderData());
		saveToFile("save/customiser1.json", gp.customiser.saveCustomiserData());
	}
	
	public void loadGame() {
	    loadFromFile("save/player1.json", PlayerSaveData.class);
	    loadFromFile("save/settings1.json", SettingsSaveData.class);
	    loadFromFile("save/world1.json", WorldSaveData.class);
	    loadFromFile("save/recipes1.json", RecipeSaveData.class);
	    loadFromFile("save/catalogue1.json", CatalogueSaveData.class);
		int counter = 0;
	    for(Room r: gp.mapM.getRooms()) {
			if(r != null) {
				loadFromFile("save/rooms1" + counter +".json", RoomSaveData.class);
			}
			counter++;
		}
	    loadFromFile("save/order1.json", OrderSaveData.class);
	    loadFromFile("save/customiser1.json", CustomiserSaveData.class);
	}
	
    // Convenience methods to save/load directly from disk
	public void saveToFile(String path, SaveData data) {
	    try {
	        File file = new File(path);

	        // Create parent directories if they don’t exist
	        File parent = file.getParentFile();
	        if (parent != null) {
	            parent.mkdirs();
	        }

	        try (FileWriter writer = new FileWriter(file)) {
	            Gson gson = new Gson();
	            gson.toJson(data, writer);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public <T extends SaveData> void loadFromFile(String path, Class<T> clazz) {
	    try (FileReader reader = new FileReader(path)) {
	        Gson gson = new Gson();
	        T data = gson.fromJson(reader, clazz); // use the class passed in
	        data.applySaveData(gp);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
}
