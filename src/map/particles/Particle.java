package map.particles;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GamePanel;

public abstract class Particle {
	
	protected GamePanel gp;
	
    protected float x, y;
    protected float vx, vy;
    protected float lifetime, maxLifetime; // seconds remaining
    protected float size;
    protected Color color;
    
    public Particle(GamePanel gp, float x, float y, float vx, float vy, float lifetime, float size, Color color) {
        this.gp = gp;
    	this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.lifetime = lifetime;
        this.size = size;
        this.color = color;
        maxLifetime = lifetime;
    }
    
    public abstract void update();
    
    public abstract void draw(Graphics2D g, int xDiff, int yDiff);
    
    public boolean isDead() {
        return lifetime <= 0;
    }
}
