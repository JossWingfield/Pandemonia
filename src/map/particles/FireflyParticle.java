package map.particles;

import java.awt.Color;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import map.LightSource;

public class FireflyParticle extends Particle {
	
    private int lightRadius;
    private LightSource light;
    
    private float targetVX, targetVY;
    private int changeTimer;
    private float speed = 0.5f; 

    public FireflyParticle(GamePanel gp, int roomNum, float x, float y, int lifetime) {
        super(gp, roomNum, x, y, 0, 0, lifetime, 3, new Colour(180, 190, 20));
        lightRadius = 9;
        
        light = new LightSource((int)x, (int)y, colour, lightRadius);
        
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

    //@Override
    public void draw(Renderer renderer) {
        float alpha = Math.max(0f, lifetime / maxLifetime);
        renderer.fillRect((int)(x), (int)(y), (int)size, (int)size, new Colour(colour.r, colour.g, colour.b, (int)(alpha * 255)));
    }

	//@Override
	public void drawEmissive(Renderer renderer) {
		// TODO Auto-generated method stub
		
	}
}
