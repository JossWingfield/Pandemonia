package map.particles;

import main.GamePanel;
import main.renderer.AssetPool;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;

public class FreezerMistParticle extends Particle {
	
	TextureRegion image;
	
	private float originX, originY;

    public FreezerMistParticle(GamePanel gp, int roomNum,
                                float x, float y) {

    	super(
    		    gp,
    		    roomNum,
    		    x,
    		    y,
    		    (float)(Math.random() - 0.5f) * 0.6f,   // much faster initial drift
    		    (float)(Math.random() - 0.5f) * 0.6f,
    		    9999f,
    		    16f + (float)Math.random() * 16f,
    		    new Colour(0.85f, 0.9f, 1f, 0.12f)
    		);

    		this.originX = x;
    		this.originY = y;
        
        image = importImage("/decor/Mist.png").toTextureRegion();
    }

    public Texture importImage(String filePath) {
		Texture texture = AssetPool.getTexture(filePath);
	    return texture;
	}
    @Override
    public void update() {

        // --- SPRING RETURN FORCE ---
        float springStrength = 0.01f;   // higher = stronger snap back
        float dx = originX - x;
        float dy = originY - y;

        vx += dx * springStrength;
        vy += dy * springStrength;

        // --- RANDOM TURBULENCE ---
        vx += (Math.random() - 0.5f) * 0.04f;
        vy += (Math.random() - 0.5f) * 0.04f;

        // --- DAMPING (prevents explosion) ---
        vx *= 0.90f;
        vy *= 0.90f;

        // --- SPEED CLAMP ---
        float maxSpeed = 1.5f; // MUCH faster movement
        float speedSq = vx * vx + vy * vy;
        if (speedSq > maxSpeed * maxSpeed) {
            float scale = maxSpeed / (float)Math.sqrt(speedSq);
            vx *= scale;
            vy *= scale;
        }

        // --- MOVE ---
        x += vx;
        y += vy;

        lifetime--;
    }

    @Override
    public void draw(Renderer renderer) {
        renderer.draw(image, x, y, size, size);
    }

    @Override
    public void drawEmissive(Renderer renderer) {
        // none
    }
}