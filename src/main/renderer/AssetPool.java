package main.renderer;

import java.awt.Font;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import main.Sound;


public class AssetPool {
	
	private static Map<String, Shader> shaders = new HashMap<>();
	private static Map<String, Texture> textures = new HashMap<>();
	private static Map<String, Sound> sounds = new HashMap<>();
    private static Map<String, Font> fonts = new HashMap<>();
    private static Map<String, BitmapFont> bitmapFonts = new HashMap<>();
	
	public static Shader getShader(String resourceName) {
		resourceName = "res"+resourceName;
		File file = new File(resourceName);
		if(AssetPool.shaders.containsKey(file.getAbsolutePath())) {
			return AssetPool.shaders.get(file.getAbsolutePath());
		} else {
			Shader shader = new Shader(resourceName);
			shader.compile();
			AssetPool.shaders.put(file.getAbsolutePath(), shader);
			return shader;
		}
	}
	public static Texture getTexture(String resourceName) {
		resourceName = "res"+resourceName;
		File file = new File(resourceName);
		if(AssetPool.textures.containsKey(file.getAbsolutePath())) {
			return AssetPool.textures.get(file.getAbsolutePath());
		} else {
			Texture texture = new Texture();
			texture.init(resourceName);
			AssetPool.textures.put(file.getAbsolutePath(), texture);
			return texture;
		}
		
	}
	
	public static Sound getSound(String soundFile) {
		soundFile = "res"+soundFile;
		File file = new File(soundFile);
		if(sounds.containsKey(file.getAbsolutePath())) {
			return sounds.get(file.getAbsolutePath());
		} else {
			assert false : "Sound file not added " + soundFile;
		}
		return null;
	}
	public static Sound addSound(String soundFile, boolean loops) {
		soundFile = "res"+soundFile;
		File file = new File(soundFile);
		if(sounds.containsKey(file.getAbsolutePath())) {
			return sounds.get(file.getAbsolutePath());
		} else {
			Sound sound = new Sound(file.getAbsolutePath(), loops);
			AssetPool.sounds.put(file.getAbsolutePath(), sound);
			return sound;
		}
	}
	public static Collection<Sound> getAllSounds() {
		return sounds.values();
	}
    public static Font getAWTFont(String resourceName, float size) {
    	resourceName = resourceName;
        File file = new File(resourceName);

        // Unique key: absolute path + size
        String key = file.getAbsolutePath() + "|" + size;

        if (fonts.containsKey(key)) {
            return fonts.get(key);
        }

        try {
            Font font = Font
                .createFont(Font.TRUETYPE_FONT, file)
                .deriveFont(size);

            fonts.put(key, font);
            return font;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load TTF font: " + resourceName);
        }
    }
    public static BitmapFont getBitmapFont(String resourceName, float pixelSize) {
    	resourceName = "res"+resourceName;
        File file = new File(resourceName);

        // Unique key: absolute path + size
        String key = file.getAbsolutePath() + "|" + pixelSize;

        if (bitmapFonts.containsKey(key)) {
            return bitmapFonts.get(key);
        }

        // 1. Load raw TTF
        BitmapFont bitmapFont = null;
		try {
			bitmapFont = BitmapFontGenerator.generate(resourceName, (int) pixelSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // 3. Cache it
        bitmapFonts.put(key, bitmapFont);

        return bitmapFont;
    }
	
}
