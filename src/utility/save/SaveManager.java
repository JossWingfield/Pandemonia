package utility.save;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.stb.STBImageWrite;

import com.google.gson.Gson;

import main.GamePanel;
import main.renderer.Texture;
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
		//System.out.println("Liadung meta");
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
	public static ByteBuffer readTexture(Texture tex) {
	    int w = tex.getWidth();
	    int h = tex.getHeight();

	    ByteBuffer buffer = ByteBuffer.allocateDirect(w * h * 4); // RGBA8
	    org.lwjgl.opengl.GL45.glGetTextureImage(
	            tex.getTexId(),
	            0,
	            org.lwjgl.opengl.GL11.GL_RGBA,
	            org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE,
	            buffer
	    );

	    return buffer;
	}
	public static void savePNG(String path, ByteBuffer pixels, int w, int h) {
	    STBImageWrite.stbi_write_png(path, w, h, 4, pixels, w * 4);
	}
	private void savePreview(int slot) {

	    // --- Final framebuffer size (what's actually on screen) ---
	    int fbW = gp.sizeX;
	    int fbH = gp.sizeY;

	    // --- Read back the final rendered frame ---
	    ByteBuffer full = ByteBuffer.allocateDirect(fbW * fbH * 4);

	    glBindFramebuffer(GL_FRAMEBUFFER, 0);
	    glReadBuffer(GL_FRONT);
	    glPixelStorei(GL_PACK_ALIGNMENT, 1);

	    glFinish();
	    glReadPixels(
	            0, 0,
	            fbW, fbH,
	            GL_RGBA,
	            GL_UNSIGNED_BYTE,
	            full
	    );

	    // --- Flip vertically (OpenGL origin -> image origin) ---
	    ByteBuffer flipped = ByteBuffer.allocateDirect(fbW * fbH * 4);
	    int stride = fbW * 4;

	    for (int y = 0; y < fbH; y++) {
	        int srcY = fbH - 1 - y;
	        for (int x = 0; x < stride; x++) {
	            flipped.put(y * stride + x, full.get(srcY * stride + x));
	        }
	    }

	    // --- Player screen position ---
	    float playerWorldX = gp.player.hitbox.x + gp.player.hitbox.width  * 0.5f;
	    float playerWorldY = gp.player.hitbox.y + gp.player.hitbox.height * 0.5f;

	    // --- Camera WORLD position (center of view) ---
	    float camX = gp.camera.position.x;
	    float camY = gp.camera.position.y;

	    // --- Convert to SCREEN space ---
	    int px = (int)(playerWorldX - camX + fbW * 0.5f);
	    int py = (int)(playerWorldY - camY + fbH * 0.5f);
	    
	    px = fbW / 2;
	    py = fbH / 2;

	    // --- World preview capture size ---
	    int viewW = 180*4;
	    int viewH = 110*4;
	    

	    int cropX = px - viewW / 2;
	    int cropY = py - viewH / 2;

	    // Clamp crop region
	    cropX = Math.max(0, Math.min(fbW - viewW, cropX));
	    cropY = Math.max(0, Math.min(fbH - viewH, cropY));

	    // --- Crop around player ---
	    ByteBuffer cropped = ByteBuffer.allocateDirect(viewW * viewH * 4);

	    for (int y = 0; y < viewH; y++) {
	        for (int x = 0; x < viewW; x++) {

	            int srcX = cropX + x;
	            int srcY = cropY + y;

	            int srcIndex = (srcY * fbW + srcX) * 4;
	            int dstIndex = (y * viewW + x) * 4;

	            cropped.put(dstIndex    , flipped.get(srcIndex    ));
	            cropped.put(dstIndex + 1, flipped.get(srcIndex + 1));
	            cropped.put(dstIndex + 2, flipped.get(srcIndex + 2));
	            cropped.put(dstIndex + 3, flipped.get(srcIndex + 3));
	        }
	    }

	    // --- Scale to preview size ---
	    int previewW = 200;
	    int previewH = 150;

	    ByteBuffer scaled = ByteBuffer.allocateDirect(previewW * previewH * 4);

	    float sx = viewW / (float) previewW;
	    float sy = viewH / (float) previewH;

	    for (int y = 0; y < previewH; y++) {
	        for (int x = 0; x < previewW; x++) {

	            int srcX = (int)(x * sx);
	            int srcY = (int)(y * sy);

	            int srcIndex = (srcY * viewW + srcX) * 4;
	            int dstIndex = (y * previewW + x) * 4;

	            scaled.put(dstIndex    , cropped.get(srcIndex    ));
	            scaled.put(dstIndex + 1, cropped.get(srcIndex + 1));
	            scaled.put(dstIndex + 2, cropped.get(srcIndex + 2));
	            scaled.put(dstIndex + 3, cropped.get(srcIndex + 3));
	        }
	    }

	    // --- Save PNG ---
	    String path = "save/preview" + slot + ".png";
	    savePNG(path, scaled, previewW, previewH);

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
		gp.mapM.saveCurrentBuildingsToRoom();
		saveMeta();
		saveToFile("save/player"+Integer.toString(currentSave)+".json", gp.player.toSaveData());
		saveToFile("save/settings"+Integer.toString(currentSave)+".json", gp.world.saveSettingsData());
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
		saveToFile("save/world"+Integer.toString(currentSave)+".json", gp.world.saveWorldData());
		saveToFile("save/order"+Integer.toString(currentSave)+".json", gp.world.saveOrderData());
		saveToFile("save/customiser"+Integer.toString(currentSave)+".json", gp.customiser.saveCustomiserData());
		saveToFile("save/progress"+Integer.toString(currentSave)+".json", gp.progressM.saveData());
		saveToFile("save/statistics"+Integer.toString(currentSave)+".json", gp.progressM.saveStatisticsData());

		
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
	    loadFromFile("save/recipes" + save + ".json", RecipeSaveData.class);
	    loadFromFile("save/upgrades" + save + ".json", UpgradeSaveData.class);
	    loadFromFile("save/catalogue" + save + ".json", CatalogueSaveData.class);

		//System.out.println("Liadung roosm");
	    int counter = 0;
	    for (Room r : gp.mapM.getRooms()) {
	        if (r != null) {
	            loadFromFile("save/rooms" + save + counter + ".json", RoomSaveData.class);
	        }
	        counter++;
	    }
	    
	    loadFromFile("save/world" + save + ".json", WorldSaveData.class);
	    loadFromFile("save/order" + save + ".json", OrderSaveData.class);
	    loadFromFile("save/customiser" + save + ".json", CustomiserSaveData.class);
	    loadFromFile("save/progress" + save + ".json", ProgressSaveData.class);
	    loadFromFile("save/statistics" + save + ".json", StatisticsSaveData.class);
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
