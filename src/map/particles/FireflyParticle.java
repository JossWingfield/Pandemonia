package map.particles;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GamePanel;
import map.LightSource;

public class FireflyParticle extends Particle {
	
    private int lightRadius;
    private LightSource light;
    
    private float targetVX, targetVY;
    private int changeTimer;
    private float speed = 0.5f; 

    public FireflyParticle(GamePanel gp, float x, float y, int lifetime) {
        super(gp, x, y, 0, 0, lifetime, 3, new Color(180, 190, 20));
        lightRadius = 9;
        
        light = new LightSource((int)x, (int)y, color, lightRadius);
        
        gp.lightingM.addLight(light); // assume addLight(x, y, radius, color, intensity)
    }
    
    public void update() {
        // 🔹 Occasionally change direction
        changeTimer--;
        if (changeTimer <= 0) {
            targetVX = (float)(Math.random() * 2 - 1);
            targetVY = (float)(Math.random() * 2 - 1);
            changeTimer = (int)(Math.random() * 120 + 60);
        }

        // 🔹 Smoothly approach target direction
        vx += (targetVX * speed - vx) * 0.05f;
        vy += (targetVY * speed - vy) * 0.05f;

        // 🔹 Move
        x += vx;
        y += vy;

        // 🔹 Move the light with the firefly
        light.setPosition((int)x, (int)y);

        // 🔹 Decrease lifetime
        lifetime -= 1; // 1 per frame

        // 🔹 Fade near end of life
        float alpha = Math.max(0f, lifetime / maxLifetime);
        light.setIntensity(alpha); // optional if your LightSource supports intensity

        // 🔹 Remove when dead
        if (lifetime <= 0) {
            gp.lightingM.removeLight(light);
        }
    }

    @Override
    public void draw(Graphics2D g, int camX, int camY) {
        float alpha = Math.max(0f, lifetime / maxLifetime);
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(alpha * 255)));
        g.fillRect((int)(x - camX), (int)(y - camY), (int)size, (int)size);
    }
}
