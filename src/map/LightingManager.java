package map;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.items.Item;
import main.GamePanel;
import utility.Settings;

public class LightingManager {

    GamePanel gp;
    private List<LightSource> lights; // List of light sources

    private Color ambientColor = new Color(255, 244, 214); // Default sunlight
    private float ambientIntensity = 1f; // Adjust from 0 (dark) to 1 (full daylight)
    private boolean firstUpdate = true;
    private boolean powerOff = false;

    // --- Bloom settings (more visible) ---
    private int bloomThreshold = 60;      // was 100 → lower so more pixels count as "bright"
    private int bloomStrength = 12;       // was 10 → slightly bigger blur
    private float bloomIntensity = 2.5f;  // was 1.5 → much stronger additive blend

    // Cache kernels by radius
    private final Map<Integer, float[]> falloffCache = new HashMap<>();

    private BufferedImage litImageUnscaled; // reused per frame
    private int[] litData;                  // Premultiplied light colors (optimization #7)
    private float[] premulR;
    private float[] premulG;
    private float[] premulB;

    private BufferedImage bloomSmall;   // downscaled + bright
    private BufferedImage bloomBlurred; // blurred, same size as bloomSmall
    private BufferedImage bloomUpscaled;// resized back to full

    private Color morning = new Color(255, 200, 150); // 6h
    private Color noon    = new Color(255, 255, 255); // 12h
    private Color evening = new Color(255, 180, 120); // 18h
    private Color night   = new Color(20, 20, 40);    // 0h / 24h

    public LightingManager(GamePanel gp) {
        this.gp = gp;
        lights = new ArrayList<>();
    }

    private void startLights() {
        // Example lights:
        // lights.add(new LightSource(13*48, 4*48, Color.BLUE, 100*4));
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

    // --- Lighting pass ---
    public BufferedImage applyLighting(BufferedImage colorBuffer, Graphics2D g2) {
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
        float ambR = (ambientColor.getRed() / 255f) * ambientIntensity;
        float ambG = (ambientColor.getGreen() / 255f) * ambientIntensity;
        float ambB = (ambientColor.getBlue() / 255f) * ambientIntensity;

        updateLightCache();

        // Fill with ambient
        for (int y = 0; y < unscaledHeight; y++) {
            int row = y * unscaledWidth;
            for (int x = 0; x < unscaledWidth; x++) {
                int idxScaled = (y * scale) * width + (x * scale);
                int baseRGB = colorData[idxScaled];
                int a = (baseRGB >>> 24) & 0xFF;
                if (a == 0) {
                    litData[row + x] = 0;
                    continue;
                }

                int rBase = (baseRGB >>> 16) & 0xFF;
                int gBase = (baseRGB >>> 8) & 0xFF;
                int bBase = baseRGB & 0xFF;

                float totalR = rBase * ambR;
                float totalG = gBase * ambG;
                float totalB = bBase * ambB;

                litData[row + x] =
                        (a << 24) |
                        (Math.min(255, Math.round(totalR)) << 16) |
                        (Math.min(255, Math.round(totalG)) << 8) |
                        Math.min(255, Math.round(totalB));
            }
        }

        // Apply lights
        for (int i = 0; i < lights.size(); i++) {
            LightSource light = lights.get(i);

            int lx = Math.round(light.getX() / scale);
            int ly = Math.round(light.getY() / scale);
            int radius = Math.round(light.getRadius() / scale);

            int minTileX = Math.max(0, (lx - radius) / tileSize);
            int maxTileX = Math.min(unscaledWidth / tileSize - 1, (lx + radius) / tileSize);
            int minTileY = Math.max(0, (ly - radius) / tileSize);
            int maxTileY = Math.min(unscaledHeight / tileSize - 1, (ly + radius) / tileSize);

            float[] falloffTable = getCachedFalloff(radius);

            for (int ty = minTileY; ty <= maxTileY; ty++) {
                for (int tx = minTileX; tx <= maxTileX; tx++) {
                    int startX = tx * tileSize;
                    int endX = Math.min(startX + tileSize, unscaledWidth);
                    int startY = ty * tileSize;
                    int endY = Math.min(startY + tileSize, unscaledHeight);

                    for (int y = startY; y < endY; y++) {
                        int row = y * unscaledWidth;
                        for (int x = startX; x < endX; x++) {
                            int idx = row + x;
                            int idxScaled = (y * scale) * width + (x * scale);

                            int baseRGB = colorData[idxScaled];
                            int a = (baseRGB >>> 24) & 0xFF;
                            if (a == 0) continue;

                            int rBase = (baseRGB >>> 16) & 0xFF;
                            int gBase = (baseRGB >>> 8) & 0xFF;
                            int bBase = baseRGB & 0xFF;

                            float dx = lx - x;
                            float dy = ly - y;
                            int dist2 = (int) (dx * dx + dy * dy);
                            if (dist2 > radius * radius) continue;

                            float attenuation = falloffTable[dist2] * light.getIntensity();

                            int prevRGB = litData[idx];
                            float totalR = ((prevRGB >>> 16) & 0xFF);
                            float totalG = ((prevRGB >>> 8) & 0xFF);
                            float totalB = (prevRGB & 0xFF);

                            // Add premultiplied light contribution
                            totalR += rBase * premulR[i] * attenuation;
                            totalG += gBase * premulG[i] * attenuation;
                            totalB += bBase * premulB[i] * attenuation;

                            litData[idx] =
                                    (a << 24) |
                                    (Math.min(255, Math.round(totalR)) << 16) |
                                    (Math.min(255, Math.round(totalG)) << 8) |
                                    Math.min(255, Math.round(totalB));
                        }
                    }
                }
            }
        }

        // Scale up
        BufferedImage litImageScaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.drawImage(litImageUnscaled, 0, 0, width, height, null);

        // Bloom
        if (Settings.bloomEnabled) {
            applyBloomInPlace(litImageScaled);
        }

        return litImageScaled;
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

    // --- Bloom ---
    private void applyBloomInPlace(BufferedImage base) {
        // 1. Extract bright areas + downscale
        bloomSmall = extractAndDownscale(base, bloomThreshold, 0.25f);

        // 2. Blur
        if (bloomBlurred == null ||
            bloomBlurred.getWidth() != bloomSmall.getWidth() ||
            bloomBlurred.getHeight() != bloomSmall.getHeight()) {
            bloomBlurred = new BufferedImage(bloomSmall.getWidth(), bloomSmall.getHeight(), BufferedImage.TYPE_INT_ARGB);
        }
        bloomBlurred = blurImage(bloomSmall, bloomStrength);

        // 3. Upscale blurred bloom back to full res
        if (bloomUpscaled == null ||
            bloomUpscaled.getWidth() != base.getWidth() ||
            bloomUpscaled.getHeight() != base.getHeight()) {
            bloomUpscaled = new BufferedImage(base.getWidth(), base.getHeight(), BufferedImage.TYPE_INT_ARGB);
        }
        Graphics2D g2 = bloomUpscaled.createGraphics();
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRect(0, 0, bloomUpscaled.getWidth(), bloomUpscaled.getHeight());
        g2.setComposite(AlphaComposite.SrcOver);
        g2.drawImage(bloomBlurred, 0, 0, base.getWidth(), base.getHeight(), null);
        g2.dispose();

        // 4. Additive blend into base
        addBloomInPlace(base, bloomUpscaled);
    }

    private void addBloomInPlace(BufferedImage base, BufferedImage bloom) {
        int[] baseData = ((DataBufferInt) base.getRaster().getDataBuffer()).getData();
        int[] bloomData = ((DataBufferInt) bloom.getRaster().getDataBuffer()).getData();

        for (int i = 0; i < baseData.length; i++) {
            int b = bloomData[i];
            if (b == 0) continue;

            int br = (b >>> 16) & 0xFF;
            int bg = (b >>> 8) & 0xFF;
            int bb = b & 0xFF;

            int o = baseData[i];
            int or = (o >>> 16) & 0xFF;
            int og = (o >>> 8) & 0xFF;
            int ob = o & 0xFF;

            int r = Math.min(255, or + Math.round(br * bloomIntensity));
            int g = Math.min(255, og + Math.round(bg * bloomIntensity));
            int bl = Math.min(255, ob + Math.round(bb * bloomIntensity));

            baseData[i] = (0xFF << 24) | (r << 16) | (g << 8) | bl;
        }
    }

    private BufferedImage extractAndDownscale(BufferedImage src, int threshold, float factor) {
        int w = Math.max(1, Math.round(src.getWidth() * factor));
        int h = Math.max(1, Math.round(src.getHeight() * factor));

        if (bloomSmall == null || bloomSmall.getWidth() != w || bloomSmall.getHeight() != h) {
            bloomSmall = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        }

        int[] srcData = ((DataBufferInt) src.getRaster().getDataBuffer()).getData();
        int[] dstData = ((DataBufferInt) bloomSmall.getRaster().getDataBuffer()).getData();

        int scaleX = src.getWidth() / w;
        int scaleY = src.getHeight() / h;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int srcX = x * scaleX;
                int srcY = y * scaleY;
                int idx = srcY * src.getWidth() + srcX;
                int pixel = srcData[idx];
                int a = (pixel >>> 24) & 0xFF;
                if (a == 0) {
                    dstData[y * w + x] = 0;
                    continue;
                }
                int r = (pixel >>> 16) & 0xFF;
                int g = (pixel >>> 8) & 0xFF;
                int b = pixel & 0xFF;
                int brightness = Math.max(r, Math.max(g, b));

                if (brightness > threshold) {
                    dstData[y * w + x] = (0xFF << 24) | (r << 16) | (g << 8) | b;
                } else {
                    dstData[y * w + x] = 0;
                }
            }
        }
        return bloomSmall;
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