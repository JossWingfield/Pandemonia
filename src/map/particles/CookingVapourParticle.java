package map.particles;

import main.GamePanel;
import main.renderer.AssetPool;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;

public class CookingVapourParticle extends Particle {

    TextureRegion image;

    private float scale;
    private float rotation;
    private float alphaFade;

    public CookingVapourParticle(
            GamePanel gp,
            int roomNum,
            float x,
            float y,
            boolean smoke
    ) {

        super(
            gp,
            roomNum,
            x,
            y,
            (float)(Math.random() - 0.5f) * 0.15f, // sideways drift
            smoke 
                ? -0.15f - (float)Math.random()*0.1f
                : -0.3f - (float)Math.random()*0.15f,
            smoke ? 180f : 120f,
            24f,
            smoke 
                ? new Colour(0.25f,0.25f,0.25f,0.18f)
                : new Colour(0.9f,0.95f,1f,0.22f)
        );


        image = AssetPool.getTexture(
            smoke ? "/decor/Smoke.png" : "/decor/Steam.png"
        ).toTextureRegion();


        scale = 0.5f + (float)Math.random()*0.4f;
        rotation = (float)Math.random()*6.28f;

        alphaFade = smoke ? 0.0015f : 0.003f;
    }


    @Override
    public void update(double dt) {

        // gentle side-to-side wobble
        vx += Math.sin(System.nanoTime()*0.000001 + x)*0.002f;

        // rise upwards
        vy -= 0.005f;


        // slow down movement
        vx *= 0.98f;
        vy *= 0.99f;


        x += vx;
        y += vy;


        // expand as it rises
        scale += 0.002f;


        // fade away
        colour.a -= alphaFade;

        lifetime--;
    }


    private float snap(float v) {
        return Math.round(v / 3f) * 3f;
    }


    @Override
    public void draw(Renderer renderer) {
        float size = 24f * scale;

        renderer.draw(
            image,
            snap(x - size/2),
            snap(y - size/2),
            size,
            size
        );
    }
    public void drawEmissive(Renderer renderer) {
        // none
    }
}