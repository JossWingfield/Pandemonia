package main.renderer;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import entity.SkinPalette;
import main.GamePanel;
import map.LightSource;

public class Renderer {

    private final GamePanel gp;
    GLSLCamera camera;

    // Batch limits
    private static final int MAX_SPRITES = 2000;          // max quads per batch before flush
    private static final int VERTICES_PER_SPRITE = 4;
    private static final int INDICES_PER_SPRITE = 6;
    private static final int FLOATS_PER_VERTEX = 9;      // x,y, u,v, texId, r,g,b,a (texId stored as float)
    private static final int VERTEX_SIZE_BYTES = FLOATS_PER_VERTEX * Float.BYTES;

    // GL objects
    private final int vaoId;
    private final int vboId;
    private final int eboId;

    // Host-side buffers for building batch
    private final float[] vertexArray;
    private int vertexCount = 0;   // number of floats written to vertexArray
    private int spriteCount = 0;   // number of sprites currently in batch

    // Index buffer (static for max sprites)
    private final IntBuffer indexBuffer;

    // Current binding state for batching
    private Texture currentTexture = null;
    private Shader currentShader = null;    // shader currently bound for the active batch
    private Shader textShader = null;
    private Shader guiShader = null;
    private final Shader defaultShader;
    private Texture rectTexture = null;

    // tmp float buffer for glBufferSubData
    private final FloatBuffer vertexFloatBuffer;
    
    private BitmapFont currentFont;
    private Colour currentColour = Colour.BLACK;
    public byte[][] lightPassable;

    // Per-frame command list (painter's algorithm)
    private final List<DrawCommand> frameCommands = new ArrayList<>();
    private final List<Vector2f> lightScreenPositions = new ArrayList<>();
    
    private int fsQuadVao;
    private int fsQuadVbo;
    private Shader lightingShader, fullScreen;
    
    private Shader brightnessShader;
    private Shader blurShaderH, blurShaderV;
    private Shader bloomCombineShader;
    private int BLOOM_BLUR_PASSES = 5;
    
    private Shader godRay = null;

    public Renderer(GamePanel gp, GLSLCamera camera) {
        this.gp = gp;
        this.camera = camera;

        // allocate host arrays
        this.vertexArray = new float[MAX_SPRITES * VERTICES_PER_SPRITE * FLOATS_PER_VERTEX];
        this.vertexFloatBuffer = BufferUtils.createFloatBuffer(vertexArray.length);

        // generate a static index buffer for the maximum capacity
        int maxIndices = MAX_SPRITES * INDICES_PER_SPRITE;
        IntBuffer ib = BufferUtils.createIntBuffer(maxIndices);
        for (int i = 0; i < MAX_SPRITES; i++) {
            int offset = i * VERTICES_PER_SPRITE;
            // two triangles: (0,1,2) (2,3,0) with offset
            ib.put(offset + 0);
            ib.put(offset + 1);
            ib.put(offset + 2);
            ib.put(offset + 2);
            ib.put(offset + 3);
            ib.put(offset + 0);
        }
        ib.flip();
        this.indexBuffer = ib;

        // Create VAO, VBO, EBO
        vaoId = glGenVertexArrays();
        vboId = glGenBuffers();
        eboId = glGenBuffers();

        glBindVertexArray(vaoId);

        // VBO: allocate buffer store (dynamic)
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, (long) vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // EBO: upload index data once
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

        // Vertex Attributes layout:
        // layout(location = 0) vec2 aPos;
        // layout(location = 1) vec2 aTexCoord;
        // layout(location = 2) float aTexId;
        // layout(location = 3) vec4 aColor;
        int stride = VERTEX_SIZE_BYTES;
        int offset = 0;

        // aPos (2 floats)
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, stride, offset);
        offset += 2 * Float.BYTES;

        // aTexCoord (2 floats)
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, stride, offset);
        offset += 2 * Float.BYTES;

        // aTexId (1 float)
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(2, 1, GL_FLOAT, false, stride, offset);
        offset += 1 * Float.BYTES;

        // aColor (4 floats)
        glEnableVertexAttribArray(3);
        glVertexAttribPointer(3, 4, GL_FLOAT, false, stride, offset);

        // Unbind
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        // Load the default shader
        this.defaultShader = AssetPool.getShader("/shaders/default.glsl");
        this.textShader = AssetPool.getShader("/shaders/text.glsl");
        guiShader = AssetPool.getShader("/shaders/gui.glsl");
        this.currentShader = defaultShader;
        rectTexture = AssetPool.getTexture("/UI/RectTexture.png");
        
        float[] quad = {
        	    // pos      // uv
        	    -1f, -1f,   0f, 0f,
        	     1f, -1f,   1f, 0f,
        	     1f,  1f,   1f, 1f,
        	    -1f,  1f,   0f, 1f
        	};

        	fsQuadVao = glGenVertexArrays();
        	fsQuadVbo = glGenBuffers();

        	glBindVertexArray(fsQuadVao);
        	glBindBuffer(GL_ARRAY_BUFFER, fsQuadVbo);
        	glBufferData(GL_ARRAY_BUFFER, quad, GL_STATIC_DRAW);

        	glEnableVertexAttribArray(0);
        	glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * Float.BYTES, 0);

        	glEnableVertexAttribArray(1);
        	glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);

        	glBindVertexArray(0);

        	lightingShader = AssetPool.getShader("/shaders/fullscreen.glsl");
        	
            brightnessShader = AssetPool.getShader("/shaders/bloom_brightness.glsl");
            blurShaderH = AssetPool.getShader("/shaders/bloom_blur_h.glsl");
            blurShaderV = AssetPool.getShader("/shaders/bloom_blur_v.glsl");
            bloomCombineShader = AssetPool.getShader("/shaders/bloom_combine.glsl");
            
            fullScreen = AssetPool.getShader("/shaders/fullscreenPlain.glsl");
            godRay = AssetPool.getShader("/shaders/godrays.glsl");
    }

    // -------------------------
    // DrawCommand API
    // -------------------------

    // begin a new frame: clear the command list and reset batch state
    public void beginFrame() {
        frameCommands.clear();
        clearBatch();
        currentShader = defaultShader;
        currentTexture = null;
        updateLightsOncePerFrame();
    }
    public void setFont(BitmapFont font) {
    	this.currentFont = font;
    }
    public void setColour(Colour colour) {
    	this.currentColour = colour;
    }
    public void setGUI() {
    	currentShader = guiShader;
    }
    public void drawSubImage(Texture texture,int srcX, int srcY, int srcWidth, int srcHeight,float destX, float destY, float destWidth, float destHeight) {

        float u0 = (float) srcX / texture.getWidth();
        float u1 = (float) (srcX + srcWidth) / texture.getWidth();

        // Flip V because OpenGL origin is bottom-left
        float v0 = 1f - (float) (srcY + srcHeight) / texture.getHeight();
        float v1 = 1f - (float) srcY / texture.getHeight();

        float[] uv = new float[] { u0, v0, u1, v1 };

        draw(texture, uv, destX, destY, destWidth, destHeight);
    }
    // full-texture UVs (0..1)
    public void draw(Texture texture, float x, float y, float width, float height) {
        float[] fullUV = new float[] { 0f, 1f, 1f, 0f };
        draw(texture, fullUV, x, y, width, height, null, null);
    }

    // explicit UV rectangle (u0,v0,u1,v1), optional shader and tint
    public void draw(Texture texture, float[] uv4, float x, float y, float width, float height) {
        draw(texture, uv4, x, y, width, height, currentShader, null);
    }

    public void draw(Texture texture, float[] uv4, float x, float y, float width, float height, Shader shader) {
        draw(texture, uv4, x, y, width, height, shader, null);
    }

    public void draw(Texture texture, float[] uv4, float x, float y, float width, float height, Vector4f tint) {
        draw(texture, uv4, x, y, width, height, null, tint);
    }

    public void draw(Texture texture, float[] uv4, float x, float y, float width, float height, Shader shader, Vector4f tint) {
        Objects.requireNonNull(texture, "texture cannot be null for draw call");
        if (uv4 == null || uv4.length != 4) throw new IllegalArgumentException("uv4 must be length 4: [u0,v0,u1,v1]");

        Vector4f color = tint == null ? new Vector4f(1f, 1f, 1f, 1f) : tint;
        frameCommands.add(new DrawCommand(texture, uv4, x, y, width, height, shader, color));
    }
    public void fillRect(float x, float y, float width, float height, Colour colour) {
    	float[] fullUV = new float[] { 0f, 1f, 1f, 0f };
    	draw(rectTexture, fullUV, x, y, width, height, colour.toVec4());
    }
    public void fillRect(float x, float y, float width, float height) {
    	float[] fullUV = new float[] { 0f, 1f, 1f, 0f };
    	draw(rectTexture, fullUV, x, y, width, height, currentColour.toVec4());
    }
    public void draw(TextureRegion region, float x, float y, float width, float height) {
        if (region == null || region.texture == null) return;

        // Compute UVs in OpenGL orientation (flip V)
        float[] uv4 = new float[] {
            region.u0, region.v1,  // bottom-left
            region.u1, region.v0   // top-right
        };

        draw(region.texture, uv4, x, y, width, height);
    }
    public void draw(TextureRegion region, float x, float y, float width, float height, Shader shader) {
        if (region == null || region.texture == null) return;

        // Compute UVs in OpenGL orientation (flip V)
        float[] uv4 = new float[] {
            region.u0, region.v1,  // bottom-left
            region.u1, region.v0   // top-right
        };

        draw(region.texture, uv4, x, y, width, height, shader);
    }
    public void draw(TextureRegion region, float x, float y, float width, float height, Vector4f tint) {
        if (region == null || region.texture == null) return;

        // Compute UVs in OpenGL orientation (flip V)
        float[] uv4 = new float[] {
            region.u0, region.v1,  // bottom-left
            region.u1, region.v0   // top-right
        };

        draw(region.texture, uv4, x, y, width, height, tint);
    }
    public void draw(TextureRegion region, float x, float y, float width, float height, Shader shader, Vector4f tint) {
        if (region == null || region.texture == null) return;

        // Compute UVs in OpenGL orientation (flip V)
        float[] uv4 = new float[] {
            region.u0, region.v1,  // bottom-left
            region.u1, region.v0   // top-right
        };

        draw(region.texture, uv4, x, y, width, height, shader, tint);
    }
    /**
     * Draw a string using the provided BitmapFont.
     * @param font single texture atlas font (must be same texture per glyph so batching works)
     * @param text the text
     * @param x starting x (left)
     * @param y baseline y (top of glyphs if you prefer)
     * @param scale scale multiplier (1.0 = original pixel size)
     * @param colour Colour object (use your Colour class). If null will default to white.
     */
    public void drawString(BitmapFont font, String text, float x, float y, float scale, Colour colour) {
        if (text == null || text.isEmpty()) return;

        Colour c = colour == null ? Colour.WHITE : colour;

        float sx = x;
        float sy = y; // THIS IS THE BASELINE

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);

            if (ch == '\n') {
                sx = x;
                sy += font.getLineHeight() * scale;
                continue;
            }

            Glyph g = font.getGlyph(ch);
            if (g == null) continue;

            TextureRegion reg = g.region;

            float gx = sx + g.xOffset * scale;
            float gy = sy + g.yOffset * scale;

            float gw = reg.getPixelWidth() * scale;
            float gh = reg.getPixelHeight() * scale;

            gx = Math.round(gx);
            gy = Math.round(gy);
            draw(reg, gx, gy, gw, gh, textShader, c.toVec4());

            sx += g.xAdvance * scale;
        }
    }

    /** Convenience overload with default scale=1 and white colour */
    public void drawString(BitmapFont font, String text, float x, float y) {
        drawString(font, text, x, y, 1.0f, currentColour);
    }
    public void drawString(String text, float x, float y) {
        drawString(currentFont, text, x, y, 1.0f, currentColour);
    }
    public void drawString(String text, float x, float y, Colour colour) {
        drawString(currentFont, text, x, y, 1.0f, colour);
    }
    public void drawString(BitmapFont font, String text, float x, float y, float scale) {
        drawString(currentFont, text, x, y, scale, currentColour);
    }
    /** Measure string pixel width (useful for alignment) */
    public float measureStringWidth(BitmapFont font, String text, float scale) {
        if (text == null || text.isEmpty()) return 0f;
        float width = 0f;
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '\n') break; // measure only first line
            width += font.getAdvance(ch) * scale;
        }
        return width;
    }

    /** Draw aligned: center, left, right */
    public void drawStringAligned(BitmapFont font, String text, float x, float y, float scale, Colour colour, Align align) {
        float w = measureStringWidth(font, text, scale);
        float startX = x;
        if (align == Align.CENTER) startX = x - w * 0.5f;
        else if (align == Align.RIGHT) startX = x - w;
        drawString(font, text, startX, y, scale, colour);
    }

    public enum Align { LEFT, CENTER, RIGHT }

    // call this at the end of frame to process the commands and draw
    public void endFrame() {
        processCommandsAndRender();
    }

    // -------------------------
    // Command processing + batching
    // -------------------------
    private void processCommandsAndRender() {
        // Reset batch state
        clearBatch();

        for (DrawCommand cmd : frameCommands) {
            Shader shaderToUse = cmd.shader == null ? defaultShader : cmd.shader;
            Texture tex = cmd.texture;

            // If shader or texture changed, or batch full -> flush current batch first
            if (shaderToUse != currentShader || tex != currentTexture || spriteCount >= MAX_SPRITES) {
                flushBatch();
                currentShader = shaderToUse;
                currentTexture = tex;
            }

            // compute UVs and push vertex data into vertexArray
            // input uv4 = [u0, v0, u1, v1]
            float u0 = cmd.uv4[0];
            float v0 = cmd.uv4[1];
            float u1 = cmd.uv4[2];
            float v1 = cmd.uv4[3];

            float x0 = cmd.x;
            float y0 = cmd.y;
            float x1 = cmd.x + cmd.width;
            float y1 = cmd.y + cmd.height;

            // texture slot - single-texture batch (slot 0)
            float texSlot = 0.0f;

            // Vertex order must match indices: top-left, bottom-left, bottom-right, top-right
            putVertex(x0, y0, u0, v0, texSlot, cmd.tint);
            putVertex(x0, y1, u0, v1, texSlot, cmd.tint);
            putVertex(x1, y1, u1, v1, texSlot, cmd.tint);
            putVertex(x1, y0, u1, v0, texSlot, cmd.tint);

            spriteCount++;
        }

        // flush any remaining sprites
        flushBatch();

        // done - clear commands for next frame
        frameCommands.clear();
    }
    public void updateLightsOncePerFrame() {
        lightScreenPositions.clear();
        for (LightSource L : gp.lightingM.getLights()) {
            Vector2f screenPos = gp.camera.worldToScreen(
                    new Vector2f(L.getX(), L.getY()),
                    camera.getViewMatrix(),
                    camera.getProjectionMatrix(),
                    gp.sizeX, gp.sizeY
            );
            lightScreenPositions.add(screenPos);
        }
    }
    
    // helper to write a vertex into the host vertexArray
    private void putVertex(float x, float y, float u, float v, float texSlot, Vector4f color) {
        int base = vertexCount;
        vertexArray[base + 0] = x;
        vertexArray[base + 1] = y;
        vertexArray[base + 2] = u;
        vertexArray[base + 3] = v;
        vertexArray[base + 4] = 0.0f; // Always 0 for single texture
        vertexArray[base + 5] = color.x;
        vertexArray[base + 6] = color.y;
        vertexArray[base + 7] = color.z;
        vertexArray[base + 8] = color.w;
        vertexCount += FLOATS_PER_VERTEX;
    }

    // Clears the batch state (used at frame start)
    private void clearBatch() {
        vertexCount = 0;
        spriteCount = 0;
        // currentTexture and currentShader are set in beginFrame/process loop
    }
    public void drawFullscreenTexture(int textureId) {
        fullScreen.use();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);
        fullScreen.uploadInt("uTextures[0]", 0);

        glBindVertexArray(fsQuadVao);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
        glBindVertexArray(0);

        fullScreen.detach();
    }
    public void renderLightingPass() {
        glBindFramebuffer(GL_FRAMEBUFFER, gp.litFbo);   // <--- render into litFbo
        glViewport(0, 0, gp.sizeX, gp.sizeY);
        glClear(GL_COLOR_BUFFER_BIT);

        lightingShader.use();

        // Bind scene texture
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, gp.sceneTextureId);
        lightingShader.uploadInt("uScene", 0);

        // Bind emissive texture
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, gp.emissiveTextureId);
        lightingShader.uploadInt("uEmissive", 1);

        // Upload lighting uniforms ONCE
        lightingShader.uploadVec2f("uScreenSize", new Vector2f(gp.sizeX, gp.sizeY));
        lightingShader.uploadVec3f("uAmbientColor", gp.lightingM.ambientColor.toVec3());
        lightingShader.uploadFloat("uAmbientIntensity", gp.lightingM.ambientIntensity);
        lightingShader.uploadInt("uNumLights", gp.lightingM.getLights().size());

        for (int i = 0; i < gp.lightingM.getLights().size(); i++) {
            LightSource L = gp.lightingM.getLights().get(i);
            Vector2f pos = lightScreenPositions.get(i);

            String base = "uLights[" + i + "].";
            lightingShader.uploadVec2f(base + "position", pos);
            lightingShader.uploadVec3f(base + "color", L.getColor().toVec3());
            lightingShader.uploadFloat(base + "radius", L.getRadius() * 3);
            lightingShader.uploadFloat(base + "intensity", L.getIntensity());
        }

        // Draw fullscreen quad
        glBindVertexArray(fsQuadVao);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
        glBindVertexArray(0);

        lightingShader.detach();
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
    public void applyAndDrawBloom(int sceneFboTexture, int bloomFbo1, int bloomFbo2, int bloomTex1, int bloomTex2) {
        // 1. Extract bright areas
        glBindFramebuffer(GL_FRAMEBUFFER, bloomFbo1);
        glViewport(0, 0, gp.sizeX, gp.sizeY);
        glClear(GL_COLOR_BUFFER_BIT);
        brightnessShader.use();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, sceneFboTexture);
        brightnessShader.uploadInt("uScene", 0);
        brightnessShader.uploadFloat("uThreshold", gp.lightingM.bloomThreshold);
        glBindVertexArray(fsQuadVao);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
        glBindVertexArray(0);
        brightnessShader.detach();
        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        // 2. Blur ping-pong
        for (int i = 0; i < BLOOM_BLUR_PASSES; i++) {
            // Horizontal pass
            glBindFramebuffer(GL_FRAMEBUFFER, bloomFbo2);
            glClear(GL_COLOR_BUFFER_BIT);
            blurShaderH.use();
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, bloomTex1);
            blurShaderH.uploadInt("uTexture", 0);
            blurShaderH.uploadFloat("uTexelWidth", 1f / gp.sizeX);
            glBindVertexArray(fsQuadVao);
            glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
            glBindVertexArray(0);
            blurShaderH.detach();
            glBindFramebuffer(GL_FRAMEBUFFER, 0);

            // Vertical pass
            glBindFramebuffer(GL_FRAMEBUFFER, bloomFbo1);
            glClear(GL_COLOR_BUFFER_BIT);
            blurShaderV.use();
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, bloomTex2);
            blurShaderV.uploadInt("uTexture", 0);
            blurShaderV.uploadFloat("uTexelHeight", 1f / gp.sizeY);
            glBindVertexArray(fsQuadVao);
            glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
            glBindVertexArray(0);
            blurShaderV.detach();
            glBindFramebuffer(GL_FRAMEBUFFER, 0);
        }

        // 3. Combine scene + bloom in one pass to default framebuffer
        glBindFramebuffer(GL_FRAMEBUFFER, 0); //TODO change to finalFbo
		gp.applyGameViewport(gp.sizeX, gp.sizeY);
        bloomCombineShader.use();

        // Scene
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, sceneFboTexture);
        bloomCombineShader.uploadInt("uScene", 0);

        // Bloom
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, bloomTex1);
        bloomCombineShader.uploadInt("uBloom", 1);
        bloomCombineShader.uploadFloat("uBloomIntensity", gp.lightingM.bloomIntensity);

        // Draw fullscreen quad
        glBindVertexArray(fsQuadVao);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
        glBindVertexArray(0);
        bloomCombineShader.detach();
    }
    public void renderGodRays(int godrayTexture, int targetFbo, float intensity, float decay, float density) {
        glBindFramebuffer(GL_FRAMEBUFFER, targetFbo);
        glViewport(0, 0, gp.sizeX, gp.sizeY);
        glClear(GL_COLOR_BUFFER_BIT);

        godRay.use();

        // Bind input texture (the window shapes)
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, godrayTexture);
        godRay.uploadInt("uTexture", 0);

        // Upload uniforms
        godRay.uploadFloat("uIntensity", intensity);
        godRay.uploadFloat("uDecay", decay);
        godRay.uploadFloat("uDensity", density);
        godRay.uploadFloat("uVerticalBias", 0.9f);
        godRay.uploadInt("uSamples", 60);
        godRay.uploadFloat("uTexelSizeX", 1.0f / gp.sizeY); 
        godRay.uploadFloat("uTexelSizeY", 1.0f / gp.sizeY); // top-left origin: texture height
        godRay.uploadFloat("uTime", (float)glfwGetTime());
        
        glBindVertexArray(fsQuadVao);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
        glBindVertexArray(0);

        godRay.detach();
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
    // Flush current batch: upload VBO and issue draw call
    private void flushBatch() {
        if (spriteCount == 0) return; // nothing to draw

        // Bind shader
        if (currentShader == null) currentShader = defaultShader;
        currentShader.use();
        
        // Upload camera/projection matrix
        Matrix4f mvp = new Matrix4f();
        camera.getProjectionMatrix().mul(camera.getViewMatrix(), mvp);
        currentShader.uploadMat4f("u_MVP", mvp);
        
        Matrix4f guiOrtho = new Matrix4f()
        	    .ortho(0.0f, gp.frameWidth, gp.frameHeight, 0.0f, -1.0f, 1.0f);

        	currentShader.uploadMat4f("u_Ortho", guiOrtho);
        
        
        // Bind texture to texture unit 0
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, currentTexture.getTexId());
        currentShader.uploadInt("uTextures[0]", 0); // tell shader it is on unit 0
        
        // Upload vertex data
        glBindVertexArray(vaoId);
        glBindBuffer(GL_ARRAY_BUFFER, vboId);

        vertexFloatBuffer.clear();
        vertexFloatBuffer.put(vertexArray, 0, vertexCount);
        vertexFloatBuffer.flip();
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertexFloatBuffer);

        // Draw call
        int indexCount = spriteCount * INDICES_PER_SPRITE;
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0);

        // Unbind
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        currentShader.detach();

        // Reset batch
        vertexCount = 0;
        spriteCount = 0;
    }

    // Call this to free GL resources when shutting down your renderer
    public void destroy() {
        glDeleteBuffers(vboId);
        glDeleteBuffers(eboId);
        glDeleteVertexArrays(vaoId);
    }

    // -------------------------
    // Inner DrawCommand class
    // -------------------------
    public static class DrawCommand {
        public final Texture texture;
        /** uv4 is [u0, v0, u1, v1] */
        public final float[] uv4;
        public final float x, y, width, height;
        public final Shader shader; // can be null -> default
        public final Vector4f tint; // not null

        public DrawCommand(Texture texture, float[] uv4, float x, float y, float width, float height, Shader shader, Vector4f tint) {
            this.texture = texture;
            this.uv4 = uv4;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.shader = shader;
            this.tint = tint == null ? new Vector4f(1f,1f,1f,1f) : tint;
        }
    }
}