package map.particles;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.util.Random;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;

public class PlayerDustParticle extends Particle {

    private static final Random rand = new Random();
    private float alpha = 0.3f + rand.nextFloat() * 0.3f;
    private float fadeSpeed = 0.01f + rand.nextFloat() * 0.01f;
    
    public PlayerDustParticle(GamePanel gp, float x, float y) {
        super(
            gp,
            x, y,
            (rand.nextFloat() - 0.5f) * 0.3f,   // small outward spread
            -rand.nextFloat() * 0.2f,           // drift slightly upward
            40 + rand.nextInt(30),              // short lifetime (frames)
            1.5f + rand.nextFloat() * 2.0f,     // small particles
            new Colour(
                120 + rand.nextInt(30),
                120 + rand.nextInt(30),
                120 + rand.nextInt(30)
            )
        );
    }

    //@Override
    public void update() {
        x += vx;
        y += vy;

        lifetime--;
        alpha -= fadeSpeed;
        if (alpha < 0) alpha = 0;
    }

    //@Override
    public void draw(Renderer renderer) {
        int screenX = (int)(x );
        int screenY = (int)(y );

        //g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        //g.setColor(color);
        //g.fillOval((int)(screenX - size/2), (int)(screenY - size/2), (int)size, (int)size);
        //g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

	//@Override
	public void drawEmissive(Renderer renderer) {
		// TODO Auto-generated method stub
		
	}
}