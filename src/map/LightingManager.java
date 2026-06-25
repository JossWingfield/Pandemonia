package map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.GLSLCamera;
import main.renderer.Texture;
import utility.CollisionMethods;
import utility.Settings;

public class LightingManager {

    GamePanel gp;
    GLSLCamera camera;
    private List<LightSource> lights; // List of light sources

    public Colour ambientColor; // Default sunlight
    public float ambientIntensity = 1f; // Adjust from 0 (dark) to 1 (full daylight)
    private boolean powerOff = false;

    //Bloom Settings
    public float bloomThreshold = 150 / 255f;     // Only really bright pixels bloom
    public int bloomStrength = 6;        // Softer blur radius
    public float bloomIntensity = 0.12f; //0.15f
    
    //Light Occlusion Settings
    private Texture currentOcclusionTexture;
    private Map<Integer, Texture> roomOcclusionCache = new HashMap<>();
    private int lastRoomId = -1;

    final float minBrightness = 0.55f;
    int minBrightness255 = (int)(minBrightness * 255); // 0.55 * 255 ≈ 140

    private Colour morning = Colour.from255(255, 200, 150); // 6h
    private Colour noon    = Colour.from255(255, 255, 255); // 12h
    private Colour evening = Colour.from255(255, 180, 120); // 18h
    private Colour night   = Colour.from255(80, 80, 60);    // 0h / 24h
    

    public LightingManager(GamePanel gp, GLSLCamera camera) {
        this.gp = gp;
        this.camera = camera;
        lights = new ArrayList<>();
        ambientColor = Colour.from255(255, 244, 214);
        
        //computeOcclusionLUT();
        //computeOcclusionARGBTable();
        //getRoomOcclusion();
    }
    private Colour lerpColor(Colour a, Colour b, float t) {
        float r = a.r + (b.r - a.r) * t;
        float g = a.g + (b.g - a.g) * t;
        float bC = a.b + (b.b - a.b) * t;
        return new Colour(r, g, bC);
    }
    // --- Light management ---
    public void addLight(int x, int y, Colour color, int radius, int layer) {
        lights.add(new LightSource(x, y, color, radius));
    }
    public void addLight(LightSource light) {
        lights.add(light);
    }
    public void setDay() {
        setAmbientLight(Colour.from255(255, 255, 255), 1f); // DAY
    }
    public void setNight() {
        setAmbientLight(Colour.from255(20, 20, 25), 1f); // NIGHT
    }
    public void clearLights() {
        lights.clear();
    }
    public List<LightSource> getLights() {
        return lights;
    }
    public void setLights(List<LightSource> lights) {
        this.lights = lights;
    }
    public void removeLightFromPos(int x, int y, int radius, int layer) {
        for (LightSource l : lights) {
            if (l.getX() == x && l.getY() == y && l.getRadius() == radius) {
                lights.remove(l);
                return;
            }
        }
    }

    public void removeLight(LightSource light) {
        lights.remove(light);
    }

    public void moveLight(LightSource light, int x, int y) {
        int index = lights.indexOf(light);
        if (index >= 0 && index < lights.size()) {
            lights.get(index).setPosition(x, y);
        }
    }
    public void clearRoomOcclusionCache() {
        if (roomOcclusionCache != null) {
            roomOcclusionCache.clear();
        }
    }
    public void updateLight(int index, int x, int y) {
        if (index >= 0 && index < lights.size()) {
            lights.get(index).setPosition(x, y);
        }
    }

    public void updateLightColor(int index, Colour color) {
        if (index >= 0 && index < lights.size()) {
            lights.get(index).setColor(color);
        }
    }

    public void updateLightColor(int roomIndex, LightSource light, Colour color) {
        if (gp.world.mapM.isInRoom(roomIndex)) {
            lights.get(lights.indexOf(light)).setColor(color);
        } else {
            var roomLights = gp.world.mapM.getRoom(roomIndex).getLights();
            int idx = roomLights.indexOf(light);
            if (idx != -1) {
                roomLights.get(idx).setColor(color);
            }
        }
    }
    public void getRoomOcclusion() {
        gp.renderer.lightPassable = null;
    }
    public void setPowerOff() {
        powerOff = true;
    }

    public void setPowerOn() {
        powerOff = false;
    }

    // --- Ambient lighting ---
    public void setAmbientLight(Colour color, float intensity) {
        this.ambientColor = color;
        this.ambientIntensity = Math.max(0, Math.min(1, intensity));
    }
    public void update(double dt) {
        
        if (!(gp.currentState == gp.playState || gp.currentState == gp.pauseState || gp.currentState == gp.achievementState || gp.currentState == gp.settingsState || gp.currentState == gp.customiseRestaurantState || gp.currentState == gp.xpState || gp.currentState == gp.dialogueState || gp.currentState == gp.chatState)) {
            return;
        }
        
	    Room room = gp.world.mapM.currentRoom;
	
	        if (room != null) {
	            int roomId = room.preset; // however you identify rooms
	
	            if (roomId != lastRoomId) {
	                lastRoomId = roomId;
	                currentOcclusionTexture = getOcclusionForRoom(room);
	            }
	        }

        float time = gp.world.gameM.getRawTime(); // 0–24h
        Colour ambient;
        float intensity;

        if (!powerOff) {
            if (time >= 6 && time < 12) {
                float t = (time - 6f) / 6f;
                ambient = lerpColor(morning, noon, t);
                intensity = 1f;
            } else if (time >= 12 && time < 18) {
                float t = (time - 12f) / 6f;

                // soften approach into evening (prevents hard handoff at 18)
                float smoothT = t * t * (3f - 2f * t); // smoothstep

                ambient = lerpColor(noon, evening, smoothT);
                intensity = 1f;
            }
            else if (time >= 18 && time < 24) {
                float t = (time - 18f) / 6f;

                // pull slightly from noon to avoid hard drop-in
                float smoothT = t * t * (3f - 2f * t);

                Colour base = lerpColor(evening, night, smoothT);

                // IMPORTANT FIX: remove sudden intensity step
                float preFade = 1f - (smoothT * 0.4f); // instead of 1 → 0.6 jump

                ambient = base;
                intensity = preFade;
            } else {
                float t = time / 6f;
                ambient = lerpColor(night, morning, t);
                intensity = 0.6f + 0.4f * t;
            }
            
            if (gp.world.mapM.currentRoom != null) {

                if (gp.world.mapM.currentRoom.darkerRoom) {
                    intensity *= 0.6f;
                    ambient = darkenColor(ambient, 0.75f);
                }

                if (gp.world.mapM.isFreezerRoom()) {
                    ambient = applyFreezerTint(ambient);

                    // Slightly dim freezer for cold sterile feel
                    intensity *= 0.85f;
                }
            }
            
            setAmbientLight(ambient, intensity);
        } else {
            setAmbientLight(night, 0.6f);
        }
    }
    private Texture getOcclusionForRoom(Room room) {

        int roomId = room.preset;

        if (roomOcclusionCache.containsKey(roomId))
            return roomOcclusionCache.get(roomId);

        int width = gp.frameWidth;
        int height = gp.frameHeight;
        int tileSize = gp.tileSize;

        int[] pixels = new int[width * height];

        int white = 0xFFFFFFFF;
        int black = 0xFF000000;

        // Fill white
        Arrays.fill(pixels, white);

        // Get room tile bounds
        int startTileX = 0;
        int startTileY = 0;
        int endTileX = 24;
        int endTileY = 16;

        for (int ty = startTileY; ty < endTileY; ty++) {
            for (int tx = startTileX; tx < endTileX; tx++) {

                if (!CollisionMethods.canLightPassThroughTile(tx, ty, gp)) {

                    int startX = tx * tileSize;
                    int startY = ty * tileSize;

                    for (int y = 0; y < tileSize; y++) {
                        for (int x = 0; x < tileSize; x++) {

                            int px = startX + x;
                            int py = startY + y;

                            if (px < 0 || py < 0 || px >= width || py >= height)
                                continue;

                            int flippedY = height - 1 - py;
                            pixels[flippedY * width + px] = black;
                        }
                    }
                }
            }
        }

        Texture texture = new Texture(width, height, pixels);
        
        roomOcclusionCache.put(roomId, texture);

        return texture;
    }
    private Colour applyFreezerTint(Colour base) {

        // Shift toward cold blue
        float r = base.r * 0.75f;     // reduce warmth
        float g = base.g * 0.9f;      // slightly mute green
        float b = Math.min(base.b * 1.2f + 0.05f, 1f); // boost blue

        return new Colour(r, g, b, base.a);
    }
    public Texture getCurrentOcclusionTexture() {
        return currentOcclusionTexture;
    }
    private Colour darkenColor(Colour color, float factor) {
        // factor = 0.5f means 50% darker
        return new Colour(
            Math.max(0f, Math.min(1f, color.r * factor)),
            Math.max(0f, Math.min(1f, color.g * factor)),
            Math.max(0f, Math.min(1f, color.b * factor))
        );
    }
}