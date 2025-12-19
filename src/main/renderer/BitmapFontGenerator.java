package main.renderer;

import static org.lwjgl.stb.STBTruetype.stbtt_BakeFontBitmap;
import static org.lwjgl.stb.STBTruetype.stbtt_GetFontVMetrics;
import static org.lwjgl.stb.STBTruetype.stbtt_InitFont;
import static org.lwjgl.stb.STBTruetype.stbtt_ScaleForPixelHeight;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;

public class BitmapFontGenerator {

    private static final int FIRST_CHAR = 32;
    private static final int LAST_CHAR = 126;

    public static BitmapFont generate(String ttfPath, int pixelHeight) throws Exception {
        // 1. Load TTF file into memory
        ByteBuffer ttfBuffer = ioResourceToByteBuffer(ttfPath, 512);

        // 2. Allocate baked char buffer
        int count = LAST_CHAR - FIRST_CHAR + 1;
        STBTTBakedChar.Buffer bakedChars = STBTTBakedChar.malloc(count);

        // 3. Create a temporary bitmap atlas
        int atlasWidth = 512;
        int atlasHeight = 512;
        ByteBuffer bitmap = BufferUtils.createByteBuffer(atlasWidth * atlasHeight);

        // 4. Bake font into bitmap
        stbtt_BakeFontBitmap(ttfBuffer, pixelHeight, bitmap, atlasWidth, atlasHeight, FIRST_CHAR, bakedChars);

        // 5. Upload atlas to OpenGL
        Texture atlasTexture = new Texture();
        atlasTexture.init(bitmap, atlasWidth, atlasHeight, 1, false);

        // 6. Create TextureRegions for each glyph
        Map<Character, Glyph> glyphMap = new HashMap<>();

        for (char c = FIRST_CHAR; c <= LAST_CHAR; c++) {
            STBTTBakedChar g = bakedChars.get(c - FIRST_CHAR);

            float u0 = g.x0() / (float) atlasWidth;
            float u1 = g.x1() / (float) atlasWidth;

            float v0 = 1.0f - (g.y1() / (float) atlasHeight);
            float v1 = 1.0f - (g.y0() / (float) atlasHeight);

            TextureRegion region = new TextureRegion(atlasTexture, u0, v0, u1, v1);
            region.setPixelSize(g.x1() - g.x0(), g.y1() - g.y0());

            Glyph glyph = new Glyph(
                region,
                g.xoff(),      
                g.yoff(),        
                g.xadvance()      
            );

            glyphMap.put(c, glyph);
        }

        // 7. Get line height using proper LWJGL STB call
        STBTTFontinfo info = STBTTFontinfo.create();
        if (!stbtt_InitFont(info, ttfBuffer)) {
            throw new RuntimeException("Failed to initialize font: " + ttfPath);
        }

        IntBuffer ascent = BufferUtils.createIntBuffer(1);
        IntBuffer descent = BufferUtils.createIntBuffer(1);
        IntBuffer lineGap = BufferUtils.createIntBuffer(1);
        stbtt_GetFontVMetrics(info, ascent, descent, lineGap);

        float scale = stbtt_ScaleForPixelHeight(info, pixelHeight);
        int lineHeight = Math.round((ascent.get(0) - descent.get(0) + lineGap.get(0)) * scale);

        bakedChars.free();

        return new BitmapFont(atlasTexture, glyphMap, lineHeight);
    }

    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws Exception {
        Path path = Path.of(resource);
        try (FileChannel fc = FileChannel.open(path, StandardOpenOption.READ)) {
            ByteBuffer buffer = BufferUtils.createByteBuffer((int) fc.size() + 1);
            while (fc.read(buffer) != -1) ;
            buffer.flip();
            return buffer;
        }
    }
}