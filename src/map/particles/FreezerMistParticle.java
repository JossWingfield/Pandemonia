package map.particles;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;

public class FreezerMistParticle extends Particle {

    public FreezerMistParticle(GamePanel gp, int roomNum,
                                float x, float y) {

        super(
            gp,
            roomNum,
            x,
            y,
            (float)(Math.random() - 0.5f) * 0.1f, // VERY slow drift
            (float)(Math.random() - 0.5f) * 0.1f,
            9999f, // basically persistent
            16f + (float)Math.random() * 16f, // larger soft mist blobs
            new Colour(0.85f, 0.9f, 1f, 0.12f) // icy blue with low alpha
        );
    }

    @Override
    public void update() {

        // Very slow movement
        x += vx;
        y += vy;

        // Slight random drift change
        vx += (Math.random() - 0.5f) * 0.002f;
        vy += (Math.random() - 0.5f) * 0.002f;

        lifetime--; // extremely slow decay
    }

    @Override
    public void draw(Renderer renderer) {
        renderer.drawFilledCircle(x, y, size, colour);
    }

    @Override
    public void drawEmissive(Renderer renderer) {
        // none
    }
}