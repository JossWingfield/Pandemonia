package map.particles;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import main.GamePanel;

public class DustParticle extends Particle {
    
    private static final Random rand = new Random();
    private float alpha = 0.2f + rand.nextFloat() * 0.3f; // softer transparency (darker ambience)
    private float fadeSpeed = 0.001f + rand.nextFloat() * 0.0015f;
    private float gravity = 0.005f + rand.nextFloat() * 0.01f;
    
    public DustParticle(GamePanel gp, float x, float y) {
        super(
            gp, 
            x, y,
            (rand.nextFloat() - 0.5f) * 0.15f,  // slower, subtler horizontal drift
            (rand.nextFloat() - 0.5f) * 0.15f,  // slower vertical drift
            500 + rand.nextInt(300),            // slightly longer lifetime
            1.0f + rand.nextFloat() * 2.0f,     // small particles
            generateGreyColor()
        );
    }

    private static Color generateGreyColor() {
        // Randomized soft grey with slight variation (so it doesn’t look uniform)
        int base = 100 + rand.nextInt(80); // range: 100–180
        int tint = rand.nextInt(20) - 10;  // subtle tone shift
        return new Color(
            clamp(base + tint),
            clamp(base + tint),
            clamp(base + tint)
        );
    }

    private static int clamp(int v) {
        return Math.max(0, Math.min(255, v));
    }

    @Override
    public void update() {
        x += vx;
        y += vy;
        
        // gentle downward drift
        vy += gravity * 0.02f;
        
        // slowly fade
        lifetime--;
        alpha -= fadeSpeed;
        if (alpha < 0) alpha = 0;
    }

    @Override
    public void draw(Graphics2D g, int xDiff, int yDiff) {
        int screenX = (int)(x - xDiff);
        int screenY = (int)(y - yDiff);

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g.setColor(color);
        g.fillOval((int)(screenX - size/2), (int)(screenY - size/2), (int)size, (int)size);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}