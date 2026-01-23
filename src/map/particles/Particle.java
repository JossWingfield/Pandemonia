package map.particles;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;

public abstract class Particle {
	
	protected GamePanel gp;
	
    protected float x, y;
    protected float vx, vy;
    protected float lifetime, maxLifetime; // seconds remaining
    protected float size;
    protected Colour colour;
    public int roomNum;
    
    public Particle(GamePanel gp, int roomNum, float x, float y, float vx, float vy, float lifetime, float size, Colour color) {
        this.gp = gp;
        this.roomNum = roomNum;
    	this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.lifetime = lifetime;
        this.size = size;
        this.colour = color;
        maxLifetime = lifetime;
    }
    
    public abstract void update();
    
    public abstract void draw(Renderer renderer);
    public abstract void drawEmissive(Renderer renderer);
    
    public boolean isDead() {
        return lifetime <= 0;
    }
}
