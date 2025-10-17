package map;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
    
    
    //Light Occlusion Settings
    private final Map<Integer, BufferedImage> roomOcclusionCache = new HashMap<>();
    private BufferedImage occlusion, lowRes;
    Graphics2D gLowRes;
    private int[] pixelData;

    final float minBrightness = 0.55f;
    int minBrightness255 = (int)(minBrightness * 255); // 0.55 * 255 ≈ 140
    private int[] occlusionLUT = new int[256];
    private int[][] occlusionARGBTable = new int[256][256];

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
        //lights.add(new LightSource(13*48, 4*48, Color.BLUE, 100*4));
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

    public BufferedImage applyLighting(BufferedImage colorBuffer, int xDiff, int yDiff) {
        int width = colorBuffer.getWidth();
        int height = colorBuffer.getHeight();
        int scale = 3;
        int unscaledWidth = width / scale;
        int unscaledHeight = height / scale;
        int tileSize = 16;

        // Reuse buffer
        if (litImageUnscaled == null ||
            litImageUnscaled.getWidth() != unscaledWidth ||
            litImageUnscaled.getHeight() != unscaledHeight) {
            litImageUnscaled = new BufferedImage(unscaledWidth, unscaledHeight, BufferedImage.TYPE_INT_ARGB);
            litData = ((DataBufferInt) litImageUnscaled.getRaster().getDataBuffer()).getData();
        }

        int[] colorData = ((DataBufferInt) colorBuffer.getRaster().getDataBuffer()).getData();

        // Precompute ambient multiplier
        int ambR255 = Math.round(ambientColor.getRed() * ambientIntensity);
        int ambG255 = Math.round(ambientColor.getGreen() * ambientIntensity);
        int ambB255 = Math.round(ambientColor.getBlue() * ambientIntensity);

        updateLightCache();

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
                int rBase = (baseRGB >>> 16) & 0xFF;
                int gBase = (baseRGB >>> 8) & 0xFF;
                int bBase = baseRGB & 0xFF;

                litData[row + x] =
                        (a << 24) |
                        (Math.min(255, rBase * ambR255 / 255) << 16) |
                        (Math.min(255, gBase * ambG255 / 255) << 8) |
                        Math.min(255, bBase * ambB255 / 255);
            }
        }

        for (int i = 0, n = lights.size(); i < n; i++) {
            LightSource light = lights.get(i);

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
        BufferedImage litImageScaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gScaled = litImageScaled.createGraphics();
        gScaled.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        gScaled.drawImage(litImageUnscaled, 0, 0, width, height, null);
        gScaled.dispose();

                
        // Apply occlusion
        if (Settings.lightOcclusionEnabled) {
        	occlusion = roomOcclusionCache.get(gp.mapM.currentRoom.preset);

        	// Directly modify the occlusion for the current frame:
        	int px = (int)(gp.player.hitbox.x + gp.player.hitbox.width / 2);
        	int py = (int)(gp.player.hitbox.y + gp.player.hitbox.height / 2);
        	applyPlayerLOS(occlusion, px, py);
        	applyOcclusionMaskOptimized(litImageScaled, occlusion);
        }

        // Bloom
        if (Settings.bloomEnabled) {
            int[] litDataArr = ((DataBufferInt) litImageScaled.getRaster().getDataBuffer()).getData();
            applyBloomDirect(litDataArr, litImageScaled.getWidth(), litImageScaled.getHeight());
        }

        return litImageScaled;
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
        // Determine the tile the player is standing on
        int playerTileX = playerX / gp.tileSize;
        int playerTileY = playerY / gp.tileSize;

        // If player is in a tile that blocks light, skip all occlusion
        if (!CollisionMethods.canLightPassThroughTile(playerTileX, playerTileY, gp)) {
            return; // leave occlusion buffer as-is
        }

        int width = occlusion.getWidth();
        int height = occlusion.getHeight();
        int scale = 3; // downscale factor

        int lowW = Math.max(1, width / scale);
        int lowH = Math.max(1, height / scale);

        // Create or resize low-res buffer
        if (lowRes == null || lowRes.getWidth() != lowW || lowRes.getHeight() != lowH) {
            lowRes = new BufferedImage(lowW, lowH, BufferedImage.TYPE_INT_ARGB);
            pixelData = ((DataBufferInt) lowRes.getRaster().getDataBuffer()).getData();
        }

        // Fill buffer white (fully visible)
        Arrays.fill(pixelData, 0xFFFFFFFF);

        int px = playerX / scale;
        int py = playerY / scale;
        int tileSize = gp.tileSize / scale;
        int losRadius = Math.max(lowW, lowH);

        int minTileX = Math.max(0, (px - losRadius) / tileSize);
        int maxTileX = Math.min(gp.mapM.currentMapWidth - 1, (px + losRadius) / tileSize);
        int minTileY = Math.max(0, (py - losRadius) / tileSize);
        int maxTileY = Math.min(gp.mapM.currentMapHeight - 1, (py + losRadius) / tileSize);

        // Loop through relevant tiles
        for (int ty = minTileY; ty <= maxTileY; ty++) {
            for (int tx = minTileX; tx <= maxTileX; tx++) {
                if (CollisionMethods.canLightPassThroughTile(tx, ty, gp)) continue;

                // Tile corners
                int left = tx * tileSize;
                int top = ty * tileSize;
                int right = Math.min(lowW, left + tileSize);
                int bottom = Math.min(lowH, top + tileSize);

                int[][] corners = { {left, top}, {right, top}, {right, bottom}, {left, bottom} };
                int[][] projected = new int[4][2];

                // Project each corner away from player
                for (int i = 0; i < 4; i++) {
                    float dx = corners[i][0] - px;
                    float dy = corners[i][1] - py;
                    float len = Math.max(1f, Math.abs(dx) + Math.abs(dy));
                    projected[i][0] = corners[i][0] + Math.round(dx / len * losRadius * 2);
                    projected[i][1] = corners[i][1] + Math.round(dy / len * losRadius * 2);
                }

                // Draw 4 shadow polygons (quads) using scanline fill
                for (int i = 0; i < 4; i++) {
                    int next = (i + 1) % 4;
                    int[] xs = { corners[i][0], corners[next][0], projected[next][0], projected[i][0] };
                    int[] ys = { corners[i][1], corners[next][1], projected[next][1], projected[i][1] };
                    fillPolygon(pixelData, lowW, lowH, xs, ys, 0xFF000000);
                }
            }
        }

        // Scale back to full resolution (nearest-neighbor for speed)
        Graphics2D gMain = occlusion.createGraphics();
        gMain.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        gMain.drawImage(lowRes, 0, 0, width, height, null);
        gMain.dispose();
    }

    // Simple scanline polygon fill for int[] pixel buffer
    private void fillPolygon(int[] pixels, int width, int height, int[] xs, int[] ys, int color) {
        int n = xs.length;
        int yMin = Integer.MAX_VALUE;
        int yMax = Integer.MIN_VALUE;

        for (int y : ys) {
            yMin = Math.min(yMin, y);
            yMax = Math.max(yMax, y);
        }

        yMin = Math.max(0, yMin);
        yMax = Math.min(height - 1, yMax);

        for (int y = yMin; y <= yMax; y++) {
            ArrayList<Integer> nodes = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                int j = (i + 1) % n;
                if ((ys[i] < y && ys[j] >= y) || (ys[j] < y && ys[i] >= y)) {
                    int x = xs[i] + (y - ys[i]) * (xs[j] - xs[i]) / (ys[j] - ys[i]);
                    nodes.add(x);
                }
            }
            Collections.sort(nodes);
            for (int i = 0; i + 1 < nodes.size(); i += 2) {
                int xStart = Math.max(0, nodes.get(i));
                int xEnd = Math.min(width - 1, nodes.get(i + 1));
                for (int x = xStart; x <= xEnd; x++) {
                    pixels[y * width + x] = color;
                }
            }
        }
    }
    private void applyOcclusionMaskOptimized(BufferedImage lightBuffer, BufferedImage occlusionLowRes) {
        int[] lightData = ((DataBufferInt) lightBuffer.getRaster().getDataBuffer()).getData();
        int[] occData = ((DataBufferInt) occlusionLowRes.getRaster().getDataBuffer()).getData();

        int wLight = lightBuffer.getWidth();
        int hLight = lightBuffer.getHeight();
        int wOcc = occlusionLowRes.getWidth();
        int hOcc = occlusionLowRes.getHeight();

        int[] occXLookup = new int[wLight];
        for (int x = 0; x < wLight; x++) occXLookup[x] = x * wOcc / wLight;

        int[] occYLookup = new int[hLight];
        for (int y = 0; y < hLight; y++) occYLookup[y] = y * hOcc / hLight;

        for (int y = 0; y < hLight; y++) {
            int row = y * wLight;
            int occY = occYLookup[y];
            int occRow = occY * wOcc;
            for (int x = 0; x < wLight; x++) {
                int idx = row + x;
                int occX = occXLookup[x];
                int occFactor = occlusionLUT[occData[occRow + occX] & 0xFF];
                int pixel = lightData[idx];
                int a = (pixel >>> 24) & 0xFF;
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
    }
    private void updateLightCache() {
        int n = lights.size();
        if (premulR == null || premulR.length != n) {
            premulR = new float[n];
            premulG = new float[n];
            premulB = new float[n];
        }
        for (int i = 0; i < n; i++) {
            Color c = lights.get(i).getColor();
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
        float downscale = 0.0625f; // 1/16
        int wSmall = Math.max(1, Math.round(baseW * downscale));
        int hSmall = Math.max(1, Math.round(baseH * downscale));

        // Create images if null or size changed
        if (bloomSmall == null || bloomSmall.getWidth() != wSmall || bloomSmall.getHeight() != hSmall) {
            bloomSmall = new BufferedImage(wSmall, hSmall, BufferedImage.TYPE_INT_ARGB);
            bloomBlurred = new BufferedImage(wSmall, hSmall, BufferedImage.TYPE_INT_ARGB);
        }

        int[] smallData = ((DataBufferInt) bloomSmall.getRaster().getDataBuffer()).getData();
        int[] blurData  = ((DataBufferInt) bloomBlurred.getRaster().getDataBuffer()).getData();

        // Extract bright pixels into smallData
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
                smallData[rowSmall + x] = (bright > bloomThreshold) ? (0xFF << 24) | (r << 16) | (g << 8) | b : 0;
            }
        }

        // --- Step 2: Blur ---
        boxBlur(smallData, blurData, wSmall, hSmall, bloomStrength);

        // --- Step 3: Upscale & Additive blend ---
        int bloomIntensityInt = Math.round(bloomIntensity * 256);
        for (int y = 0; y < baseH; y++) {
            int srcY = Math.min(hSmall - 1, y * hSmall / baseH);
            int rowSmall = srcY * wSmall;
            int rowBase  = y * baseW;

            for (int x = 0; x < baseW; x++) {
                int srcX = Math.min(wSmall - 1, x * wSmall / baseW);
                int bPixel = blurData[rowSmall + srcX];
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

    private void boxBlur(int[] src, int[] dst, int w, int h, int radius) {
        int div = radius * 2 + 1;
        int[] tmp = new int[src.length];

        // Horizontal pass
        for (int y = 0; y < h; y++) {
            int sumR = 0, sumG = 0, sumB = 0;
            int row = y * w;
            for (int x = -radius; x <= radius; x++) {
                int px = src[row + clamp(x, 0, w - 1)];
                sumR += (px >>> 16) & 0xFF;
                sumG += (px >>> 8) & 0xFF;
                sumB += px & 0xFF;
            }
            for (int x = 0; x < w; x++) {
                tmp[row + x] = (0xFF << 24) | ((sumR / div) << 16) | ((sumG / div) << 8) | (sumB / div);
                int pxOut = src[row + clamp(x - radius, 0, w - 1)];
                int pxIn  = src[row + clamp(x + radius + 1, 0, w - 1)];
                sumR += ((pxIn >>> 16) & 0xFF) - ((pxOut >>> 16) & 0xFF);
                sumG += ((pxIn >>> 8) & 0xFF) - ((pxOut >>> 8) & 0xFF);
                sumB += (pxIn & 0xFF) - (pxOut & 0xFF);
            }
        }

        // Vertical pass
        for (int x = 0; x < w; x++) {
            int sumR = 0, sumG = 0, sumB = 0;
            for (int y = -radius; y <= radius; y++) {
                int yy = clamp(y, 0, h - 1);
                int px = tmp[yy * w + x];
                sumR += (px >>> 16) & 0xFF;
                sumG += (px >>> 8) & 0xFF;
                sumB += px & 0xFF;
            }
            for (int y = 0; y < h; y++) {
                dst[y * w + x] = (0xFF << 24) | ((sumR / div) << 16) | ((sumG / div) << 8) | (sumB / div);
                int pxOut = tmp[clamp(y - radius, 0, h - 1) * w + x];
                int pxIn  = tmp[clamp(y + radius + 1, 0, h - 1) * w + x];
                sumR += ((pxIn >>> 16) & 0xFF) - ((pxOut >>> 16) & 0xFF);
                sumG += ((pxIn >>> 8) & 0xFF) - ((pxOut >>> 8) & 0xFF);
                sumB += (pxIn & 0xFF) - (pxOut & 0xFF);
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

    public void update() {
        if (firstUpdate) {
            firstUpdate = false;
            startLights();
        }

        float time = gp.world.getRawTime(); // 0–24h
        Color ambient;
        float intensity;

        if (!powerOff && !gp.mapM.isInRoom(6)) {
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
            setAmbientLight(ambient, intensity);
        } else {
            setAmbientLight(night, 0.6f);
        }
    }
}