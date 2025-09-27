package utility.save;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.google.gson.Gson;

import main.GamePanel;

public class SaveManager {
	
	GamePanel gp;
	
	public SaveManager(GamePanel gp) {
		this.gp = gp;
	}
	
	public void saveGame() {
		saveToFile("save/player1.json", gp.player.toSaveData());
		saveToFile("save/settings.json", gp.world.saveSettingsData());
	}
	
	public void loadGame() {
	    loadFromFile("save/player1.json", PlayerSaveData.class);
	    loadFromFile("save/settings.json", SettingsSaveData.class);
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
