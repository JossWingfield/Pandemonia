package utility.save;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import main.GamePanel;
import map.Room;
import utility.RecipeManager;

public class SaveManager {
	
	GamePanel gp;
	
	public int currentSave = 0; 
	
	// Slot info stored in save/meta.json
	private Map<Integer, Boolean> saveSlots = new HashMap<>();

	
	public SaveManager(GamePanel gp) {
		this.gp = gp;
		loadMeta(); // load slot states on startup
	}
	// --- META HANDLING ---
	private File getMetaFile() {
		return new File("save/meta.json");
	}
	private void loadMeta() {
		File metaFile = getMetaFile();
		if (metaFile.exists()) {
			try (FileReader reader = new FileReader(metaFile)) {
				Gson gson = new Gson();
				@SuppressWarnings("unchecked")
				Map<String, Boolean> loaded = gson.fromJson(reader, Map.class);
				saveSlots.clear();
				if (loaded != null) {
					for (String key : loaded.keySet()) {
						saveSlots.put(Integer.parseInt(key), loaded.get(key));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// Initialize with all slots empty
			saveSlots.put(1, false);
			saveSlots.put(2, false);
			saveSlots.put(3, false);
		}
	}
	public void clearSaveSlot(int slot) {
	    // Delete all files associated with this save slot
	    File saveDir = new File("save");
	    if (saveDir.exists() && saveDir.isDirectory()) {
	        File[] files = saveDir.listFiles();
	        if (files != null) {
	            for (File f : files) {
	                if (f.getName().matches(".*" + slot + ".*\\.json$")) {
	                    f.delete();
	                }
	            }
	        }
	    }

	    // Mark slot as empty
	    saveSlots.put(slot, false);
	    saveMeta();
	}
	private void saveMeta() {
		try {
			File metaFile = getMetaFile();
			File parent = metaFile.getParentFile();
			if (parent != null) parent.mkdirs();
			try (FileWriter writer = new FileWriter(metaFile)) {
				Gson gson = new Gson();
				Map<String, Boolean> toSave = new HashMap<>();
				for (Integer key : saveSlots.keySet()) {
					toSave.put(key.toString(), saveSlots.get(key));
				}
				gson.toJson(toSave, writer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveGame() {
		saveSlots.put(currentSave, true); // mark slot as filled
		saveMeta();
		saveToFile("save/player"+Integer.toString(currentSave)+".json", gp.player.toSaveData());
		saveToFile("save/settings"+Integer.toString(currentSave)+".json", gp.world.saveSettingsData());
		saveToFile("save/world"+Integer.toString(currentSave)+".json", gp.world.saveWorldData());
		saveToFile("save/recipes"+Integer.toString(currentSave)+".json", RecipeManager.saveRecipeData());
		saveToFile("save/catalogue"+Integer.toString(currentSave)+".json", gp.catalogue.saveCatalogueData());
		int counter = 0;
		for(Room r: gp.mapM.getRooms()) {
			if(r != null) {
				saveToFile("save/rooms"+Integer.toString(currentSave)+"" + counter +".json", r.saveRoomData());
			}
			counter++;
		}
		saveToFile("save/order"+Integer.toString(currentSave)+".json", gp.world.saveOrderData());
		saveToFile("save/customiser"+Integer.toString(currentSave)+".json", gp.customiser.saveCustomiserData());
	}
	
	public void loadGame(int save) {
		currentSave = save;

		if (!saveSlots.getOrDefault(save, false)) {
			startGame();
			return;
		}
	    
	    // Load data
	    loadFromFile("save/player" + save + ".json", PlayerSaveData.class);
	    loadFromFile("save/settings" + save + ".json", SettingsSaveData.class);
	    loadFromFile("save/world" + save + ".json", WorldSaveData.class);
	    loadFromFile("save/recipes" + save + ".json", RecipeSaveData.class);
	    loadFromFile("save/catalogue" + save + ".json", CatalogueSaveData.class);

	    int counter = 0;
	    for (Room r : gp.mapM.getRooms()) {
	        if (r != null) {
	            loadFromFile("save/rooms" + save + counter + ".json", RoomSaveData.class);
	        }
	        counter++;
	    }

	    loadFromFile("save/order" + save + ".json", OrderSaveData.class);
	    loadFromFile("save/customiser" + save + ".json", CustomiserSaveData.class);
	}
	public void startGame() {
		gp.startGame();
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
