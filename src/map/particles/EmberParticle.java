package map.particles;

import java.awt.*;
import java.util.Random;
import main.GamePanel;
import map.LightSource;

public class EmberParticle extends Particle {
    
    private static final Random rand = new Random();
    private static final float SCALE = 3.0f; // matches your pixel scale
    
    private float alpha = 0.6f + rand.nextFloat() * 0.4f;
    private float fadeSpeed = 0.001f + rand.nextFloat() * 0.001f;
    
    private int lightRadius;
    private LightSource light;
    
    public EmberParticle(GamePanel gp, float x, float y, boolean addLight) {
        super(
                gp,
                x, y,
                (rand.nextFloat() - 0.5f) * 1.0f,  // random horizontal drift
                (rand.nextFloat() - 0.5f) * 1.0f,  // random vertical drift
                30 + rand.nextInt(30),             // very short lifetime (30–60 frames)
                (1.5f + rand.nextFloat() * 2.0f) * SCALE,  // smaller ember core
                generateEmberColor()
            );

            alpha = 0.8f + rand.nextFloat() * 0.2f;          // initial transparency
            fadeSpeed = 1.0f / lifetime;        
        lightRadius = 9;
        
        light = new LightSource((int)x, (int)y, color, lightRadius);
        
        gp.lightingM.addLight(light); 
    }
    
    
    public EmberParticle(GamePanel gp, float x, float y) {
        super(
                gp,
                x, y,
                (rand.nextFloat() - 0.5f) * 1.0f,  // random horizontal drift
                (rand.nextFloat() - 0.5f) * 1.0f,  // random vertical drift
                30 + rand.nextInt(30),             // very short lifetime (30–60 frames)
                (1.5f + rand.nextFloat() * 2.0f) * SCALE,  // smaller ember core
                generateEmberColor()
            );

            alpha = 0.8f + rand.nextFloat() * 0.2f;          // initial transparency
            fadeSpeed = 1.0f / lifetime;        
    }
    
    private static Color generateEmberColor() {
        // Bright fiery tones that vary subtly between orange and red
        int r = 230 + rand.nextInt(25);
        int g = 100 + rand.nextInt(80);
        int b = 20 + rand.nextInt(30);
        return new Color(r, g, b);
    }
    
    @Override
    public void update() {
        x += vx;
        y += vy;
        
        // Slowly fade over time
        lifetime--;
        alpha -= fadeSpeed;
        if (alpha < 0) alpha = 0;
        
        
        if(light != null) {
        	float alpha = Math.max(0f, lifetime / maxLifetime);
        	light.setIntensity(alpha);
        	light.setPosition((int)x, (int)y);
            if (lifetime <= 0) {
                gp.lightingM.removeLight(light);
            }
        }


   
    }
    
    @Override
    public void draw(Graphics2D g, int xDiff, int yDiff) {
        // Convert world position to screen position
        float alpha = Math.max(0f, lifetime / maxLifetime);
        int screenX = (int)(x - xDiff);
        int screenY = (int)(y - yDiff);

        // Snap to nearest pixel grid (scaled)
        screenX = (screenX / (int)SCALE) * (int)SCALE;
        screenY = (screenY / (int)SCALE) * (int)SCALE;

        // Draw a solid pixel rectangle
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(alpha * 255)));

        g.fillRect(screenX, screenY, (int)SCALE, (int)SCALE);
    }
    public void drawEmissive(Graphics2D g, int xDiff, int yDiff) {
        // Convert world position to screen position
        int screenX = (int)(x - xDiff);
        int screenY = (int)(y - yDiff);

        // Snap to nearest pixel grid (scaled)
        screenX = (screenX / (int)SCALE) * (int)SCALE;
        screenY = (screenY / (int)SCALE) * (int)SCALE;

        // Draw a solid pixel rectangle
        g.setColor(color);
        g.fillRect(screenX, screenY, (int)SCALE, (int)SCALE);
    }
}

