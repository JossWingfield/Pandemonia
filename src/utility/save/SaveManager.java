package utility.save;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.gson.Gson;

import main.GamePanel;
import map.Room;
import utility.RecipeManager;
import utility.Season;
import utility.UpgradeManager;

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
	private void savePreview(int slot) {
		// World position of the player
		int px = (int)(gp.player.hitbox.x + gp.player.hitbox.width/2);
		int py = (int)(gp.player.hitbox.y + gp.player.hitbox.height/2);

		// Choose how much of the world around the player you want
		int viewW = 180;
		int viewH = 110;

		// Calculate top-left corner of the crop
		int cropX = px - viewW / 2;
		int cropY = py - viewH / 2;

		// Clamp to screen bounds
		cropX = Math.max(0, Math.min(gp.colorBuffer.getWidth() - viewW, cropX));
		cropY = Math.max(0, Math.min(gp.colorBuffer.getHeight() - viewH, cropY));

		// Crop region
		BufferedImage cropped = gp.colorBuffer.getSubimage(cropX, cropY, viewW, viewH);

		// Scale to preview size (zoom effect)
		Image preview = cropped.getScaledInstance(200, 150, Image.SCALE_SMOOTH);

		// Save preview
		BufferedImage out = new BufferedImage(200, 150, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = out.createGraphics();
		g2.drawImage(preview, 0, 0, null);
		g2.dispose();

		try {
			ImageIO.write(out, "png", new File("save/preview" + slot + ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean isSlotEmpty(int slot) {
	    return !saveSlots.getOrDefault(slot, false);
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
		saveToFile("save/upgrades"+Integer.toString(currentSave)+".json", UpgradeManager.saveUpgradeData());
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
	    
		
		savePreview(currentSave);
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
	    loadFromFile("save/upgrades" + save + ".json", UpgradeSaveData.class);
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
	public int getSavedDay(int slot) {
	    try (FileReader reader = new FileReader("save/world" + slot + ".json")) {
	        Gson gson = new Gson();
	        WorldSaveData data = gson.fromJson(reader, WorldSaveData.class);
	        return data != null ? data.day : -1; // return -1 if no save
	    } catch (Exception e) {
	        e.printStackTrace();
	        return -1;
	    }
	}
	public int getSavedMoney(int slot) {
	    try (FileReader reader = new FileReader("save/player" + slot + ".json")) {
	        Gson gson = new Gson();
	        PlayerSaveData data = gson.fromJson(reader, PlayerSaveData.class);
	        return data != null ? data.wealth : -1; // return -1 if no save
	    } catch (Exception e) {
	        e.printStackTrace();
	        return -1;
	    }
	}

	public Season getSavedSeason(int slot) {
	    try (FileReader reader = new FileReader("save/world" + slot + ".json")) {
	        Gson gson = new Gson();
	        WorldSaveData data = gson.fromJson(reader, WorldSaveData.class);
	        return data != null ? data.currentSeason : null; // return null if no save
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
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
