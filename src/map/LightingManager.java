package map;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.Camera;
import main.GamePanel;
import utility.CollisionMethods;
import utility.Settings;

public class LightingManager {

    GamePanel gp;
    Camera camera;
    private List<LightSource> lights; // List of light sources

    private Color ambientColor = new Color(255, 244, 214); // Default sunlight
    private float ambientIntensity = 1f; // Adjust from 0 (dark) to 1 (full daylight)
    private boolean firstUpdate = true;
    private boolean powerOff = false;

    // Cache kernels by radius
    private final Map<Integer, float[]> falloffCache = new HashMap<>();

    private BufferedImage litImageUnscaled; // reused per frame
    private int[] litData;                  // Premultiplied light colors (optimization #7)
    private float[] premulR;
    private float[] premulG;
    private float[] premulB;

    //Bloom Settings
    private BufferedImage bloomSmall;   // downscaled + bright
    private BufferedImage bloomBlurred; // blurred, same size as bloomSmall
    public int bloomThreshold = 140;     // Only really bright pixels bloom
    public int bloomStrength = 6;        // Softer blur radius
    public float bloomIntensity = 0.15f;   // was 1.5 → much stronger additive blend
    private int[] bloomSmallData;
    private int[] bloomBlurredData;
    
    
    //Light Occlusion Settings
    private final Map<Integer, BufferedImage> roomOcclusionCache = new HashMap<>();
    private BufferedImage occlusion, lowRes;
    Graphics2D gLowRes;
    private int[] pixelData;

    final float minBrightness = 0.55f;
    int minBrightness255 = (int)(minBrightness * 255); // 0.55 * 255 ≈ 140
    private int[] occlusionLUT = new int[256];
    private int[][] occlusionARGBTable = new int[256][256];
    private byte[][] lightPassable;
    private byte[][] visibleMask;

    private Color morning = new Color(255, 200, 150); // 6h
    private Color noon    = new Color(255, 255, 255); // 12h
    private Color evening = new Color(255, 180, 120); // 18h
    private Color night   = new Color(20, 20, 40);    // 0h / 24h
    

    public LightingManager(GamePanel gp, Camera camera) {
        this.gp = gp;
        this.camera = camera;
        lights = new ArrayList<>();
        computeOcclusionLUT();
        computeOcclusionARGBTable();
        getRoomOcclusion();
    }

    private void startLights() {
        // Example lights:
        //lights.add(new LightSource(13*48, 4*48, Color.BLUE, 100*4, LightSource.Type.BLOOM_ONLY));
        // lights.add(new LightSource(9*48, 6*48, Color.RED, 100*3));
        // lights.add(new LightSource(9*48, 9*48, Color.YELLOW, 100*4));
    }
    private Color lerpColor(Color a, Color b, float t) {
        int r = Math.round(a.getRed() + (b.getRed() - a.getRed()) * t);
        int g = Math.round(a.getGreen() + (b.getGreen() - a.getGreen()) * t);
        int bC = Math.round(a.getBlue() + (b.getBlue() - a.getBlue()) * t);
        return new Color(r, g, bC);
    }
    // --- Light management ---
    public void addLight(int x, int y, Color color, int radius, int layer) {
        lights.add(new LightSource(x, y, color, radius));
    }
    public void addLight(LightSource light) {
        lights.add(light);
    }
    public void setDay() {
        setAmbientLight(new Color(255, 255, 255), 1f); // DAY
    }
    public void setNight() {
        setAmbientLight(new Color(20, 20, 25), 1f); // NIGHT
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

    public void updateLightColor(int index, Color color) {
        if (index >= 0 && index < lights.size()) {
            lights.get(index).setColor(color);
        }
    }

    public void updateLightColor(int roomIndex, LightSource light, Color color) {
        if (gp.mapM.isInRoom(roomIndex)) {
            lights.get(lights.indexOf(light)).setColor(color);
        } else {
            var roomLights = gp.mapM.getRoom(roomIndex).getLights();
            int idx = roomLights.indexOf(light);
            if (idx != -1) {
                roomLights.get(idx).setColor(color);
            }
        }
    }

    public void setPowerOff() {
        powerOff = true;
    }

    public void setPowerOn() {
        powerOff = false;
    }

    // --- Ambient lighting ---
    public void setAmbientLight(Color color, float intensity) {
        this.ambientColor = color;
        this.ambientIntensity = Math.max(0, Math.min(1, intensity));
    }

    public BufferedImage applyLighting(BufferedImage colorBuffer, BufferedImage emissiveBuffer, int xDiff, int yDiff) {
        int width = colorBuffer.getWidth();
        int height = colorBuffer.getHeight();
        int scale = 3;
        int unscaledWidth = width / scale;
        int unscaledHeight = height / scale;
        int tileSize = 16;
        
        //xDiff = Math.round(xDiff / scale) * scale;
        //yDiff = Math.round(yDiff / scale) * scale;

        // Reuse buffer
        if (litImageUnscaled == null ||
            litImageUnscaled.getWidth() != unscaledWidth ||
            litImageUnscaled.getHeight() != unscaledHeight) {
            litImageUnscaled = new BufferedImage(unscaledWidth, unscaledHeight, BufferedImage.TYPE_INT_ARGB);
            litData = ((DataBufferInt) litImageUnscaled.getRaster().getDataBuffer()).getData();
        }

        int[] colorData = ((DataBufferInt) colorBuffer.getRaster().getDataBuffer()).getData();
        int[] emissiveData = ((DataBufferInt) emissiveBuffer.getRaster().getDataBuffer()).getData();

        // Precompute ambient multiplier
        int ambR255 = Math.round(ambientColor.getRed() * ambientIntensity);
        int ambG255 = Math.round(ambientColor.getGreen() * ambientIntensity);
        int ambB255 = Math.round(ambientColor.getBlue() * ambientIntensity);

        // Fill ambient
        for (int y = 0; y < unscaledHeight; y++) {
            int row = y * unscaledWidth;
            int idxScaledRow = (y * scale) * width;
            for (int x = 0; x < unscaledWidth; x++) {
                int idxScaled = idxScaledRow + x * scale;
                int baseRGB = colorData[idxScaled];
                int a = (baseRGB >>> 24) & 0xFF;
                if (a == 0) {
                    litData[row + x] = 0;
                    continue;
                }
                
                int emisRGB = emissiveData[idxScaled];
                int emisA = (emisRGB >>> 24) & 0xFF;
                boolean isEmissive = emisA > 0;

                // Normal color channels
                int rBase = (baseRGB >>> 16) & 0xFF;
                int gBase = (baseRGB >>> 8) & 0xFF;
                int bBase = baseRGB & 0xFF;

                // If emissive, keep at full brightness (skip ambient dimming)
                if (isEmissive) {
                    litData[row + x] = baseRGB;
                } else {
                    litData[row + x] =
                        (a << 24) |
                        (Math.min(255, rBase * ambR255 / 255) << 16) |
                        (Math.min(255, gBase * ambG255 / 255) << 8) |
                        Math.min(255, bBase * ambB255 / 255);
                }
           
            }
        }
        updateLightCache();
        
        if (Settings.bloomEnabled) {
            width = colorBuffer.getWidth();
            height = colorBuffer.getHeight();
            float downscale = 1f / 3f;
            int wSmall = Math.max(1, Math.round(width * downscale));
            int hSmall = Math.max(1, Math.round(height * downscale));

            if (bloomSmall == null || bloomSmall.getWidth() != wSmall || bloomSmall.getHeight() != hSmall) {
                bloomSmall = new BufferedImage(wSmall, hSmall, BufferedImage.TYPE_INT_ARGB);
                bloomBlurred = new BufferedImage(wSmall, hSmall, BufferedImage.TYPE_INT_ARGB);
                bloomSmallData = new int[wSmall * hSmall];
                bloomBlurredData = new int[wSmall * hSmall];
            } else {
                Arrays.fill(bloomSmallData, 0); // clear previous bloom data
            }
        }
        List<LightSource> copy = new ArrayList<>(lights);
        ensurePremulCapacity(lights.size());
        for (int i = 0, n = copy.size(); i < n; i++) {
            LightSource light = copy.get(i);
            if(light == null) {
            	continue;
            }
            
            if (light.getType() == LightSource.Type.BLOOM_ONLY) {
                // Skip normal lighting — but record for bloom buffer
                addBloomLight(light, xDiff, yDiff);
                continue;
            }

            int lx = (light.getX() - xDiff) / scale;
            int ly = (light.getY() - yDiff) / scale;
            int radius = Math.max(1, light.getRadius() / scale);

            // Skip lights completely offscreen
            if (lx + radius < 0 || lx - radius >= unscaledWidth || ly + radius < 0 || ly - radius >= unscaledHeight)
                continue;

            int radius2 = radius * radius;
            float intensity = light.getIntensity();

            // Precompute scaled falloff × premul color for this light
            float[] falloffTable = getCachedFalloff(radius);

            float rMul = premulR[i] * intensity;
            float gMul = premulG[i] * intensity;
            float bMul = premulB[i] * intensity;

            int minTileX = Math.max(0, (lx - radius) / tileSize);
            int maxTileX = Math.min(unscaledWidth / tileSize - 1, (lx + radius) / tileSize);
            int minTileY = Math.max(0, (ly - radius) / tileSize);
            int maxTileY = Math.min(unscaledHeight / tileSize - 1, (ly + radius) / tileSize);

            for (int ty = minTileY; ty <= maxTileY; ty++) {
                int startY = ty * tileSize;
                int endY = Math.min(startY + tileSize, unscaledHeight);
                for (int y = startY; y < endY; y++) {
                    int row = y * unscaledWidth;
                    int dy = ly - y;
                    int dy2 = dy * dy;

                    for (int tx = minTileX; tx <= maxTileX; tx++) {
                        int startX = tx * tileSize;
                        int endX = Math.min(startX + tileSize, unscaledWidth);
                        for (int x = startX; x < endX; x++) {
                            int dx = lx - x;
                            int dist2 = dx * dx + dy2;
                            if (dist2 > radius2) continue;

                            int idx = row + x;
                            int idxScaled = (y * scale) * width + (x * scale);
                            int baseRGB = colorData[idxScaled];
                            int a = (baseRGB >>> 24) & 0xFF;
                            if (a == 0) continue;
                            
                            int emisRGB = emissiveData[idxScaled];
                            if ((emisRGB >>> 24) > 0) continue;

                            int rBase = (baseRGB >>> 16) & 0xFF;
                            int gBase = (baseRGB >>> 8) & 0xFF;
                            int bBase = baseRGB & 0xFF;

                            float attenuation = falloffTable[dist2];

                            // Integer approximation: round by casting
                            int rAdd = Math.min(255, (int)(rBase * rMul * attenuation));
                            int gAdd = Math.min(255, (int)(gBase * gMul * attenuation));
                            int bAdd = Math.min(255, (int)(bBase * bMul * attenuation));

                            int prevRGB = litData[idx];
                            int r = Math.min(255, ((prevRGB >>> 16) & 0xFF) + rAdd);
                            int g = Math.min(255, ((prevRGB >>> 8) & 0xFF) + gAdd);
                            int b = Math.min(255, (prevRGB & 0xFF) + bAdd);

                            litData[idx] = (a << 24) | (r << 16) | (g << 8) | b;
                        }
                    }
                }
            }
        }

        // Scale up
        float fracX = xDiff % scale;
        float fracY = yDiff % scale;

        BufferedImage litImageScaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gScaled = litImageScaled.createGraphics();
        gScaled.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        gScaled.drawImage(
            litImageUnscaled,
            (int)-fracX, (int)-fracY, width - (int)fracX, height - (int)fracY,
            0, 0, litImageUnscaled.getWidth(), litImageUnscaled.getHeight(),
            null
        );
        gScaled.dispose();

                
        // Apply occlusion
        if (Settings.lightOcclusionEnabled) {
        	occlusion = roomOcclusionCache.get(gp.mapM.currentRoom.preset);

        	// Directly modify the occlusion for the current frame:
        	int px = (int)(gp.player.hitbox.x + gp.player.hitbox.width / 2);
        	int py = (int)(gp.player.hitbox.y + gp.player.hitbox.height / 2);
        	if(occlusion != null) {
            	applyPlayerLOS(occlusion, px, py);
            	applyOcclusionMaskOptimized(litImageScaled, occlusion, xDiff, yDiff);
        	}
        }
        
        blendAdditive(litImageScaled, emissiveBuffer);
        
        // Bloom
        if (Settings.bloomEnabled) {
            int[] litDataArr = ((DataBufferInt) litImageScaled.getRaster().getDataBuffer()).getData();
            applyBloomDirect(litDataArr, litImageScaled.getWidth(), litImageScaled.getHeight());
        }

        return litImageScaled;
    }
    private void ensurePremulCapacity(int lightCount) {
        if (premulR == null || premulR.length < lightCount) {
            premulR = new float[lightCount];
            premulG = new float[lightCount];
            premulB = new float[lightCount];

            for (int i = 0; i < lightCount; i++) {
                premulR[i] = 1f; // default multiplier
                premulG[i] = 1f;
                premulB[i] = 1f;
            }
        }
    }
    private void blendAdditive(BufferedImage base, BufferedImage emissive) {
        int w = base.getWidth();
        int h = base.getHeight();

        int[] baseData = ((DataBufferInt) base.getRaster().getDataBuffer()).getData();
        int[] emisData = ((DataBufferInt) emissive.getRaster().getDataBuffer()).getData();

        for (int i = 0; i < baseData.length; i++) {
            int br = (baseData[i] >> 16) & 0xFF;
            int bg = (baseData[i] >> 8) & 0xFF;
            int bb = baseData[i] & 0xFF;

            int er = (emisData[i] >> 16) & 0xFF;
            int eg = (emisData[i] >> 8) & 0xFF;
            int eb = emisData[i] & 0xFF;

            float emissiveStrength = 0.6f; // 1.0 = full, <1.0 = dimmer
            int nr = Math.min(255, br + (int)(er * emissiveStrength));
            //int nr = Math.min(255, br + er);
            //int ng = Math.min(255, bg + eg);
            int ng = Math.min(255, bg + (int)(eg * emissiveStrength));
            //int nb = Math.min(255, bb + eb);
            int nb = Math.min(255, bb + (int)(eb * emissiveStrength));

            baseData[i] = (0xFF << 24) | (nr << 16) | (ng << 8) | nb;
        }
    }
    private void addBloomLight(LightSource light, int xDiff, int yDiff) {
        if (bloomSmall == null) return; // ensure bloom buffers exist

        int scale = 3;
        int lx = (light.getX() - xDiff) / scale;
        int ly = (light.getY() - yDiff) / scale;
        int radius = Math.max(1, light.getRadius() / scale);
        float r = light.getColor().getRed() / 255f;
        float g = light.getColor().getGreen() / 255f;
        float b = light.getColor().getBlue() / 255f;

        float intensity = light.getIntensity(); // base intensity
        float bloomMultiplier = 2f;          
        intensity *= bloomMultiplier;           // amplify glow

        int w = bloomSmall.getWidth();
        int h = bloomSmall.getHeight();
        int[] bloomData = bloomSmallData;
        if (bloomData == null) return;

        int radius2 = radius * radius;
        float exponent = 0.5f; // power-based falloff

        for (int y = Math.max(0, ly - radius); y < Math.min(h, ly + radius); y++) {
            int row = y * w;
            int dy = y - ly;
            int dy2 = dy * dy;

            for (int x = Math.max(0, lx - radius); x < Math.min(w, lx + radius); x++) {
                int dx = x - lx;
                int dist2 = dx * dx + dy2;
                if (dist2 > radius2) continue;

                float attenuation = 1f - ((float) dist2 / radius2);
                attenuation = (float) Math.pow(attenuation, exponent);
                attenuation *= intensity;

                int br = (int) (r * 255 * attenuation);
                int bg = (int) (g * 255 * attenuation);
                int bb = (int) (b * 255 * attenuation);

                int idx = row + x;
                int prev = bloomData[idx];

                int pr = (prev >> 16) & 0xFF;
                int pg = (prev >> 8) & 0xFF;
                int pb = prev & 0xFF;

                int nr = Math.min(255, pr + br);
                int ng = Math.min(255, pg + bg);
                int nb = Math.min(255, pb + bb);

                bloomData[idx] = (0xFF << 24) | (nr << 16) | (ng << 8) | nb;
            }
        }
    }
    public void drawOcclusionDebug(Graphics2D g) {

        BufferedImage occlusion = roomOcclusionCache.get(gp.mapM.currentRoom.preset);
        if (occlusion == null) return;

        // --- CONFIG ---
        final int previewSize = 160; // width of the debug image
        final float alpha = 0.7f;    // transparency level
        final int margin = 20;       // distance from screen edges
        // --------------

        // Maintain aspect ratio
        float aspect = (float) occlusion.getHeight() / occlusion.getWidth();
        int drawW = previewSize;
        int drawH = (int) (previewSize * aspect);

        // Bottom-left corner position
        int drawX = margin;
        int drawY = gp.getHeight() - drawH - margin;

        // Semi-transparent overlay
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        // Draw scaled occlusion image
        g.drawImage(occlusion, drawX, drawY, drawW, drawH, null);

        // White border
        g.setComposite(AlphaComposite.SrcOver);
        g.setColor(Color.WHITE);
        g.drawRect(drawX, drawY, drawW, drawH);

        // Label
        g.setFont(g.getFont().deriveFont(12f));
        g.drawString("Occlusion Debug", drawX + 6, drawY + 14);

        // Reset composite
        g.setComposite(AlphaComposite.SrcOver);
    }
    private void applyPlayerLOS(BufferedImage occlusion, int playerX, int playerY) {
        if (occlusion == null) return;

        // --- Setup ---
        int width = occlusion.getWidth();
        int height = occlusion.getHeight();
        int scale = 3; // lighting downscale factor
        int lowW = Math.max(1, width / scale);
        int lowH = Math.max(1, height / scale);

        if (lowRes == null || lowRes.getWidth() != lowW || lowRes.getHeight() != lowH) {
            lowRes = new BufferedImage(lowW, lowH, BufferedImage.TYPE_INT_ARGB);
            pixelData = ((DataBufferInt) lowRes.getRaster().getDataBuffer()).getData();
        }

        // --- Build map cache ---
        int mapW = gp.mapM.currentMapWidth;
        int mapH = gp.mapM.currentMapHeight;
        if (lightPassable == null || lightPassable.length != mapH || lightPassable[0].length != mapW) {
            lightPassable = new byte[mapH][mapW];
            for (int ty = 0; ty < mapH; ty++) {
                for (int tx = 0; tx < mapW; tx++) {
                    lightPassable[ty][tx] = (byte)(CollisionMethods.canLightPassThroughTile(tx, ty, gp) ? 1 : 0);
                }
            }
        }

        // --- Clear low-res image ---
        Arrays.fill(pixelData, 0xFF000000); // start black

        // --- Compute player position in low-res pixel space ---
        float playerLowX = playerX / (float)scale;
        float playerLowY = playerY / (float)scale;

        int numRays = 1080; // number of rays around player
        float maxDist = (gp.tileSize * 15) / (float)scale; // 15 tiles radius, in low-res pixels

        for (int i = 0; i < numRays; i++) {
            double angle = (i / (double)numRays) * Math.PI * 2.0;
            double dx = Math.cos(angle);
            double dy = Math.sin(angle);

            double x = playerLowX;
            double y = playerLowY;

            for (int d = 0; d < maxDist; d++) {
                x += dx;
                y += dy;

                int px = (int)x;
                int py = (int)y;

                if (px < 0 || py < 0 || px >= lowW || py >= lowH) break;

                // sample the world tile under this low-res pixel
                int worldX = (int)(px * scale);
                int worldY = (int)(py * scale);
                int tileX = worldX / gp.tileSize;
                int tileY = worldY / gp.tileSize;

                // stop if ray hits solid tile
                if (tileX < 0 || tileY < 0 || tileY >= mapH || tileX >= mapW) break;
                if (lightPassable[tileY][tileX] == 0) break;

                // mark visible pixel (white)
                for (int by = 0; by < 2; by++) {
                    int fy = py + by;
                    if (fy < 0 || fy >= lowH) continue;
                    for (int bx = 0; bx < 2; bx++) {
                        int fx = px + bx;
                        if (fx < 0 || fx >= lowW) continue;
                        pixelData[fy * lowW + fx] = 0xFFFFFFFF;
                    }
                }
            }
        }
        // --- Upscale to full-resolution occlusion buffer ---
        Graphics2D gMain = occlusion.createGraphics();
        gMain.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        gMain.drawImage(lowRes, 0, 0, width, height, null);
        gMain.dispose();

    }
    private void applyOcclusionMaskOptimized(BufferedImage lightBuffer, BufferedImage occlusionLowRes,int xDiff, int yDiff) {
		int[] lightData = ((DataBufferInt) lightBuffer.getRaster().getDataBuffer()).getData();
		int[] occData   = ((DataBufferInt) occlusionLowRes.getRaster().getDataBuffer()).getData();
		
		int wLight = lightBuffer.getWidth(); 
		int hLight = lightBuffer.getHeight();
		int wOcc   = occlusionLowRes.getWidth();
		int hOcc   = occlusionLowRes.getHeight();
		
		// Map world pixels → occlusion texture pixels.
		// Compute room size in world pixels (map width in tiles * tileSize)
		int roomPixelW = gp.mapM.currentMapWidth * gp.tileSize;
		int roomPixelH = gp.mapM.currentMapHeight * gp.tileSize;
		
		// If your occlusion image was created with frame size (gp.frameWidth/gp.frameHeight)
		// but the room is larger, use roomPixelW/H. If occlusion image is already room-sized,
		// this mapping will still be correct.
		float occScaleX = (float) wOcc / (float) Math.max(1, roomPixelW);
		float occScaleY = (float) hOcc / (float) Math.max(1, roomPixelH);
		
		// Precompute lookup arrays (cheap and keeps inner loop fast)
		int[] occXLookup = new int[wLight];
		for (int x = 0; x < wLight; x++) {
		// worldX = bufferX + xDiff
		// buffer pixel x corresponds to world X coordinate at (x + xDiff)
		int worldX = x + xDiff;
		int occX = (int) (worldX * occScaleX);
		if (occX < 0) occX = 0;
		else if (occX >= wOcc) occX = wOcc - 1;
		occXLookup[x] = occX;
		}
		
		int[] occYLookup = new int[hLight];
		for (int y = 0; y < hLight; y++) {
		int worldY = y + yDiff;
		int occY = (int) (worldY * occScaleY);
		if (occY < 0) occY = 0;
		else if (occY >= hOcc) occY = hOcc - 1;
		occYLookup[y] = occY;
		}
		
		// Apply occlusion using the lookups
		for (int y = 0; y < hLight; y++) {
		int row = y * wLight;
		int occY = occYLookup[y];
		int occRow = occY * wOcc;
		for (int x = 0; x < wLight; x++) {
		int idx = row + x;
		int occX = occXLookup[x];
		
		int occVal = occData[occRow + occX] & 0xFF;
		int occFactor = occlusionLUT[occVal];
		
		int pixel = lightData[idx];
		int a = (pixel >>> 24) & 0xFF;
		if (a == 0) continue;
		
		int r = occlusionARGBTable[occFactor][(pixel >>> 16) & 0xFF];
		int g = occlusionARGBTable[occFactor][(pixel >>> 8) & 0xFF];
		int b = occlusionARGBTable[occFactor][pixel & 0xFF];
		
		lightData[idx] = (a << 24) | (r << 16) | (g << 8) | b;
		}
		}
	}
    private BufferedImage getOcclusionForRoom(int roomId, int width, int height) {
        // Return cached if exists
        if (roomOcclusionCache.containsKey(roomId)) return roomOcclusionCache.get(roomId);

        int tileSize = gp.tileSize;
        occlusion = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = occlusion.createGraphics();

        // Fill with white (no occlusion)
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        g.setColor(Color.BLACK);
        // Get room tile boundaries
        int startTileX = 0;
        int startTileY = 0;
        int endTileX = gp.mapM.currentMapWidth;
        int endTileY = gp.mapM.currentMapHeight;

        // Draw black for solid tiles
        for (int ty = startTileY; ty < endTileY; ty++) {
            for (int tx = startTileX; tx < endTileX; tx++) {
                if (!CollisionMethods.canLightPassThroughTile(tx, ty, gp)) {
                    g.fillRect(tx * tileSize, ty * tileSize, tileSize, tileSize);
                }
            }
        }
        g.dispose();

        // Pre-blur
        BufferedImage blurred = blurImage(occlusion, 3);
        roomOcclusionCache.put(roomId, blurred);
        return blurred;
    }
    public void getRoomOcclusion() {
        int roomId = gp.mapM.currentRoom.preset;
        BufferedImage occlusion = getOcclusionForRoom(roomId, gp.frameWidth, gp.frameHeight);
        roomOcclusionCache.put(roomId, occlusion);
        lightPassable = null; 
        lowRes = null;
        pixelData = null;
    }
    private void updateLightCache() {
        List<LightSource> copy = new ArrayList<>(lights);
        int n = copy.size();

        if (premulR == null || premulR.length != n) {
            premulR = new float[n];
            premulG = new float[n];
            premulB = new float[n];
        }

        for (int i = 0; i < n; i++) {
            LightSource light = copy.get(i);
            if (light == null) {
                premulR[i] = premulG[i] = premulB[i] = 0f;
                continue;
            }

            Color c = light.getColor();
            premulR[i] = c.getRed() / 255f;
            premulG[i] = c.getGreen() / 255f;
            premulB[i] = c.getBlue() / 255f;
        }
    }

    private float[] getFalloffTable(int radius) {
        float[] table = new float[radius * radius + 1];
        float r2 = radius * radius;
        for (int d2 = 0; d2 <= r2; d2++) {
            float attenuation = 1f - (d2 / r2);
            if (attenuation < 0f) attenuation = 0f;
            table[d2] = attenuation;
        }
        return table;
    }

    private float[] getCachedFalloff(int radius) {
        return falloffCache.computeIfAbsent(radius, this::getFalloffTable);
    }

    private void applyBloomDirect(int[] baseData, int baseW, int baseH) {
        // --- Step 1: Downscale ---
        float downscale = 1f / 3f;
        int wSmall = Math.max(1, Math.round(baseW * downscale));
        int hSmall = Math.max(1, Math.round(baseH * downscale));

        // Create buffers if needed
        if (bloomSmall == null || bloomSmall.getWidth() != wSmall || bloomSmall.getHeight() != hSmall) {
            bloomSmall = new BufferedImage(wSmall, hSmall, BufferedImage.TYPE_INT_ARGB);
            bloomBlurred = new BufferedImage(wSmall, hSmall, BufferedImage.TYPE_INT_ARGB);
        }

        if (bloomSmallData == null || bloomSmallData.length != wSmall * hSmall) {
            bloomSmallData = new int[wSmall * hSmall];
            bloomBlurredData = new int[wSmall * hSmall];
        }

        // --- Step 1: Bright-pixel extraction (additive) ---
        int scaleX = baseW / wSmall;
        int scaleY = baseH / hSmall;
        for (int y = 0; y < hSmall; y++) {
            int srcY = y * scaleY;
            int rowSmall = y * wSmall;
            int rowBase  = srcY * baseW;

            for (int x = 0; x < wSmall; x++) {
                int srcX = x * scaleX;
                int pixel = baseData[rowBase + srcX];

                int r = (pixel >>> 16) & 0xFF;
                int g = (pixel >>> 8) & 0xFF;
                int b = pixel & 0xFF;
                int bright = Math.max(r, Math.max(g, b));

                int dst = bloomSmallData[rowSmall + x]; // existing bloom data (from custom lights)
                int dr = (dst >> 16) & 0xFF;
                int dg = (dst >> 8) & 0xFF;
                int db = dst & 0xFF;

                if (bright > bloomThreshold) {
                    int nr = Math.min(255, dr + r);
                    int ng = Math.min(255, dg + g);
                    int nb = Math.min(255, db + b);
                    bloomSmallData[rowSmall + x] = (0xFF << 24) | (nr << 16) | (ng << 8) | nb;
                }
            }
        }

        // --- Step 2: Blur ---
        boxBlur(bloomSmallData, bloomBlurredData, wSmall, hSmall, bloomStrength);

        // --- Step 3: Upscale & blend ---
        int bloomIntensityInt = Math.round(bloomIntensity * 256);
        for (int y = 0; y < baseH; y++) {
            int srcY = (y * hSmall) / baseH;
            if (srcY >= hSmall) srcY = hSmall - 1;
            int rowSmall = srcY * wSmall;
            int rowBase  = y * baseW;

            for (int x = 0; x < baseW; x++) {
                int srcX = Math.min(wSmall - 1, x * wSmall / baseW);
                int bPixel = bloomBlurredData[rowSmall + srcX];
                if (bPixel == 0) continue;

                int br = (bPixel >>> 16) & 0xFF;
                int bg = (bPixel >>> 8) & 0xFF;
                int bb = bPixel & 0xFF;

                int o = baseData[rowBase + x];
                int or = (o >>> 16) & 0xFF;
                int og = (o >>> 8) & 0xFF;
                int ob = o & 0xFF;

                baseData[rowBase + x] = (0xFF << 24) |
                                        (Math.min(255, or + ((br * bloomIntensityInt) >> 8)) << 16) |
                                        (Math.min(255, og + ((bg * bloomIntensityInt) >> 8)) << 8) |
                                        Math.min(255, ob + ((bb * bloomIntensityInt) >> 8));
            }
        }
    }

    private void boxBlur(int[] src, int[] dst, int w, int h, int radius) {
        if (radius <= 0) {
            System.arraycopy(src, 0, dst, 0, src.length);
            return;
        }

        final int div = radius * 2 + 1;
        final float invDiv = 1.0f / div; // faster than dividing per pixel
        int[] tmp = new int[src.length];

        // --- Horizontal pass ---
        for (int y = 0; y < h; y++) {
            int base = y * w;
            int sumR = 0, sumG = 0, sumB = 0;

            // preload initial window
            int px = src[base];
            int first = px, last = src[base + w - 1];
            for (int i = -radius; i <= radius; i++) {
                int idx = base + Math.min(w - 1, Math.max(0, i));
                px = src[idx];
                sumR += (px >> 16) & 0xFF;
                sumG += (px >> 8) & 0xFF;
                sumB += px & 0xFF;
            }

            // sliding window
            for (int x = 0; x < w; x++) {
                tmp[base + x] = (0xFF << 24)
                        | ((int)(sumR * invDiv) << 16)
                        | ((int)(sumG * invDiv) << 8)
                        | (int)(sumB * invDiv);

                int i1 = x - radius;
                int i2 = x + radius + 1;

                int out = src[base + (i1 < 0 ? 0 : i1)];
                int in  = src[base + (i2 >= w ? w - 1 : i2)];

                sumR += ((in >> 16) & 0xFF) - ((out >> 16) & 0xFF);
                sumG += ((in >> 8) & 0xFF) - ((out >> 8) & 0xFF);
                sumB += (in & 0xFF) - (out & 0xFF);
            }
        }

        // --- Vertical pass ---
        for (int x = 0; x < w; x++) {
            int sumR = 0, sumG = 0, sumB = 0;

            // preload initial window
            int topIdx = x;
            int bottomIdx = (h - 1) * w + x;
            for (int i = -radius; i <= radius; i++) {
                int yy = Math.min(h - 1, Math.max(0, i));
                int px = tmp[yy * w + x];
                sumR += (px >> 16) & 0xFF;
                sumG += (px >> 8) & 0xFF;
                sumB += px & 0xFF;
            }

            for (int y = 0; y < h; y++) {
                dst[y * w + x] = (0xFF << 24)
                        | ((int)(sumR * invDiv) << 16)
                        | ((int)(sumG * invDiv) << 8)
                        | (int)(sumB * invDiv);

                int y1 = y - radius;
                int y2 = y + radius + 1;

                int out = tmp[(y1 < 0 ? 0 : y1) * w + x];
                int in  = tmp[(y2 >= h ? h - 1 : y2) * w + x];

                sumR += ((in >> 16) & 0xFF) - ((out >> 16) & 0xFF);
                sumG += ((in >> 8) & 0xFF) - ((out >> 8) & 0xFF);
                sumB += (in & 0xFF) - (out & 0xFF);
            }
        }
    }
    private void boxBlurPass(int[] src, int[] dst, int w, int h, int radius, boolean horizontal) {
        int div = radius * 2 + 1;

        if (horizontal) {
            for (int y = 0; y < h; y++) {
                int sumR = 0, sumG = 0, sumB = 0, sumA = 0;
                int idx = y * w;

                for (int i = -radius; i <= radius; i++) {
                    int px = src[idx + clamp(i, 0, w - 1)];
                    sumA += (px >>> 24) & 0xFF;
                    sumR += (px >>> 16) & 0xFF;
                    sumG += (px >>> 8) & 0xFF;
                    sumB += px & 0xFF;
                }

                for (int x = 0; x < w; x++) {
                    dst[idx + x] = ((sumA / div) << 24) | ((sumR / div) << 16) | ((sumG / div) << 8) | (sumB / div);

                    if (x - radius >= 0) {
                        int px = src[idx + x - radius];
                        sumA -= (px >>> 24) & 0xFF;
                        sumR -= (px >>> 16) & 0xFF;
                        sumG -= (px >>> 8) & 0xFF;
                        sumB -= px & 0xFF;
                    }
                    if (x + radius + 1 < w) {
                        int px = src[idx + x + radius + 1];
                        sumA += (px >>> 24) & 0xFF;
                        sumR += (px >>> 16) & 0xFF;
                        sumG += (px >>> 8) & 0xFF;
                        sumB += px & 0xFF;
                    }
                }
            }
        } else {
            for (int x = 0; x < w; x++) {
                int sumR = 0, sumG = 0, sumB = 0, sumA = 0;

                for (int i = -radius; i <= radius; i++) {
                    int yy = clamp(i, 0, h - 1);
                    int px = src[yy * w + x];
                    sumA += (px >>> 24) & 0xFF;
                    sumR += (px >>> 16) & 0xFF;
                    sumG += (px >>> 8) & 0xFF;
                    sumB += px & 0xFF;
                }

                for (int y = 0; y < h; y++) {
                    dst[y * w + x] = ((sumA / div) << 24) | ((sumR / div) << 16) | ((sumG / div) << 8) | (sumB / div);

                    if (y - radius >= 0) {
                        int px = src[(y - radius) * w + x];
                        sumA -= (px >>> 24) & 0xFF;
                        sumR -= (px >>> 16) & 0xFF;
                        sumG -= (px >>> 8) & 0xFF;
                        sumB -= px & 0xFF;
                    }
                    if (y + radius + 1 < h) {
                        int px = src[(y + radius + 1) * w + x];
                        sumA += (px >>> 24) & 0xFF;
                        sumR += (px >>> 16) & 0xFF;
                        sumG += (px >>> 8) & 0xFF;
                        sumB += px & 0xFF;
                    }
                }
            }
        }
    }

    private int clamp(int v, int min, int max) {
        return (v < min) ? min : (v > max ? max : v);
    }
    private BufferedImage blurImage(BufferedImage src, int radius) {
        if (radius <= 0) return src;

        int w = src.getWidth();
        int h = src.getHeight();
        int[] srcData = ((DataBufferInt) src.getRaster().getDataBuffer()).getData();
        int[] tmpData = new int[srcData.length];
        int[] dstData = new int[srcData.length];

        int passes = 5; // number of blur passes for Gaussian approximation
        System.arraycopy(srcData, 0, dstData, 0, srcData.length);

        for (int p = 0; p < passes; p++) {
            boxBlurPass(dstData, tmpData, w, h, radius, true);
            boxBlurPass(tmpData, dstData, w, h, radius, false);
        }

        System.arraycopy(dstData, 0, srcData, 0, srcData.length);
        return src;
    }
    private void computeOcclusionARGBTable() {
        for (int occ = 0; occ < 256; occ++) {
            int factor = occlusionLUT[occ];
            for (int base = 0; base < 256; base++) {
                occlusionARGBTable[occ][base] = base * factor / 255;
            }
        }
    }
    private void computeOcclusionLUT() {
        for (int i = 0; i < 256; i++) {
            // Map black (0) → minBrightness, white (255) → 255
            occlusionLUT[i] = minBrightness255 + (i * (255 - minBrightness255)) / 255;
        }
    }

    public void update(double dt) {
        if (firstUpdate) {
            firstUpdate = false;
            startLights();
        }

        float time = gp.world.getRawTime(); // 0–24h
        Color ambient;
        float intensity;

        if (!powerOff) {
            if (time >= 6 && time < 12) {
                float t = (time - 6f) / 6f;
                ambient = lerpColor(morning, noon, t);
                intensity = 1f;
            } else if (time >= 12 && time < 18) {
                float t = (time - 12f) / 6f;
                ambient = lerpColor(noon, evening, t);
                intensity = 1f;
            } else if (time >= 18 && time < 24) {
                float t = (time - 18f) / 6f;
                ambient = lerpColor(evening, night, t);
                intensity = 0.6f;
            } else {
                float t = time / 6f;
                ambient = lerpColor(night, morning, t);
                intensity = 0.6f + 0.4f * t;
            }
            
            if (gp.mapM.currentRoom != null && gp.mapM.currentRoom.darkerRoom) {
                // Make the room’s lighting darker (reduce intensity + darken color)
                intensity *= 0.6f; // 40% darker
                ambient = darkenColor(ambient, 0.75f); // darken by 50%
            }
            
            setAmbientLight(ambient, intensity);
        } else {
            setAmbientLight(night, 0.6f);
        }
    }
    private Color darkenColor(Color color, float factor) {
        // factor = 0.5f means 50% darker
        int r = (int) (color.getRed() * factor);
        int g = (int) (color.getGreen() * factor);
        int b = (int) (color.getBlue() * factor);
        return new Color(
            Math.max(0, Math.min(255, r)),
            Math.max(0, Math.min(255, g)),
            Math.max(0, Math.min(255, b))
        );
    }
}