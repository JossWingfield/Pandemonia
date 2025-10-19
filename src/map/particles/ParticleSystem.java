package map.particles;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import main.GamePanel;


public class ParticleSystem {
	
	GamePanel gp;

    private List<Particle> particles = new ArrayList<>();
    
    private int targetCount = 60;
    private int spawnCooldown = 0;
    
    private boolean firefliesActive = false;
    
    public ParticleSystem(GamePanel gp) {
    	this.gp = gp;
    }
    
    public void addParticle(Particle p) {
        particles.add(p);
    }
    
    public void update() {
        Iterator<Particle> it = particles.iterator();
        while(it.hasNext()) {
            Particle p = it.next();
            p.update();
            if(p.isDead()) it.remove();
        }
        
        if(firefliesActive) {
	        // 🔹 Spawn new fireflies gradually
	        spawnCooldown--;
	        if (spawnCooldown <= 0) {
	            spawnCooldown = 2; // every few frames, spawn one or two
	            while (particles.size() < targetCount) {
	                float x = (float) (Math.random() * gp.screenWidth);
	                float y = (float) (Math.random() * gp.screenHeight);
	                int lifetime = (int) (Math.random() * 200 + 100);
	                addParticle(new FireflyParticle(gp, x, y, lifetime));
	            }
	        }
        }
    }
    
    public void draw(Graphics2D g, int xDiff, int yDiff) {
        List<Particle> copy = new ArrayList<>(particles);
        for (Particle p : copy) {
            p.draw(g, xDiff, yDiff);
        }
    }
    
    public void clear() {
        particles.clear();
    }
	
}
