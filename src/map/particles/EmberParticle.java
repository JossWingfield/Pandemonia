package map.particles;

import java.awt.*;
import java.util.Random;
import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import map.LightSource;

public class EmberParticle extends Particle {
    
    private static final Random rand = new Random();
    private static final float SCALE = 3.0f; // matches your pixel scale
    
    private float alpha = 0.6f + rand.nextFloat() * 0.4f;
    private float fadeSpeed = 0.001f + rand.nextFloat() * 0.001f;
    
    private int lightRadius;
    private LightSource light;
    
    private final boolean flare;
    
    public EmberParticle(GamePanel gp, int roomNum, float x, float y, boolean flare) {
        super(
            gp,
            roomNum,
            x, y,
            (rand.nextFloat() - 0.5f) * 1.2f,
            (rand.nextFloat() - 0.5f) * 1.2f,
            flare ? 20 + rand.nextInt(15) : 30 + rand.nextInt(30),
            flare ? (3.0f + rand.nextFloat() * 2.0f) * SCALE
                  : (1.5f + rand.nextFloat() * 2.0f) * SCALE,
            generateEmberColor()
        );

        this.flare = flare;

        alpha = flare ? 1.0f : (0.8f + rand.nextFloat() * 0.2f);
        fadeSpeed = 1.0f / lifetime;

        if (flare) {
            lightRadius = 9;
            light = new LightSource((int)x, (int)y, colour, lightRadius);
            gp.lightingM.addLight(light);
        }
    }
    
    private static Colour generateEmberColor() {
        // Bright fiery tones that vary subtly between orange and red
        int r = 230 + rand.nextInt(25);
        int g = 100 + rand.nextInt(80);
        int b = 20 + rand.nextInt(30);
        return new Colour(r, g, b);
    }
    
    //@Override
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
    
    //@Override
    public void draw(Renderer renderer) {
        // Convert world position to screen position
        float alpha = Math.max(0f, lifetime / maxLifetime);
        int screenX = (int)(x );
        int screenY = (int)(y );

        // Snap to nearest pixel grid (scaled)
        screenX = (screenX / (int)SCALE) * (int)SCALE;
        screenY = (screenY / (int)SCALE) * (int)SCALE;

        // Draw a solid pixel rectangle

        renderer.fillRect(screenX, screenY, (int)SCALE, (int)SCALE, new Colour(colour.r, colour.g, colour.b, (int)(alpha * 255)));
    }
    public void drawEmissive(Renderer renderer) {
        // Convert world position to screen position
        int screenX = (int)(x );
        int screenY = (int)(y );

        // Snap to nearest pixel grid (scaled)
        screenX = (screenX / (int)SCALE) * (int)SCALE;
        screenY = (screenY / (int)SCALE) * (int)SCALE;

        // Draw a solid pixel rectangle
        renderer.fillRect(screenX, screenY, (int)SCALE, (int)SCALE, colour);
    }
}

