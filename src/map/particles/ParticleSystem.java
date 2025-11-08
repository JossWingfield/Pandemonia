package map.particles;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import ai.Node;
import main.GamePanel;
import map.Room;


public class ParticleSystem {
	
	GamePanel gp;

    private List<Particle> particles = new ArrayList<>();
    
    private int targetFireflyCount = 60;
    private int spawnCooldown = 0;
    
    private List<EmberPath> activeEmberPaths = new ArrayList<>();
    
    private boolean firefliesActive = false;
    private boolean dustActive = true;
    private int dustTargetCount = 80;
    private int emberLightCooldown = 0; // controls how often a light is spawned
    private final int EMBER_LIGHT_INTERVAL = 4; // create a light every 5 particles
    
    public ParticleSystem(GamePanel gp) {
    	this.gp = gp;
    }
    
    public void addParticle(Particle p) {
        particles.add(p);
    }
    public void setDustActive(boolean active) {
        this.dustActive = active;
    }
    
    public void update() {
        Iterator<Particle> it = particles.iterator();
        while(it.hasNext()) {
            Particle p = it.next();
            p.update();
            if(p.isDead()) it.remove();
        }
        
        dustActive = gp.mapM.currentRoom.darkerRoom;
        
        if (dustActive) {
            spawnCooldown--;
            if (spawnCooldown <= 0) {
                spawnCooldown = 3;
                while (particles.size() < dustTargetCount) {
                    float x = (float) (Math.random() * gp.screenWidth);
                    float y = (float) (Math.random() * gp.screenHeight);
                    addParticle(new DustParticle(gp, x, y));
                }
            }
        }
        
        if(firefliesActive) {
	        // 🔹 Spawn new fireflies gradually
	        spawnCooldown--;
	        if (spawnCooldown <= 0) {
	            spawnCooldown = 2; // every few frames, spawn one or two
	            while (particles.size() < targetFireflyCount) {
	                float x = (float) (Math.random() * gp.screenWidth);
	                float y = (float) (Math.random() * gp.screenHeight);
	                int lifetime = (int) (Math.random() * 200 + 100);
	                addParticle(new FireflyParticle(gp, x, y, lifetime));
	            }
	        }
        }
        
        Iterator<EmberPath> pathIt = activeEmberPaths.iterator();
        while (pathIt.hasNext()) {
            EmberPath ep = pathIt.next();
            if (ep.isFinished()) {
                pathIt.remove();
                continue;
            }
            float[] pos = ep.getNextPosition(1.5f, gp); // speed can vary
            if (pos != null) {
                EmberParticle ember = new EmberParticle(gp, pos[0], pos[1], ep.withLight);
                addParticle(ember);
            }
        }
    }
    public void spawnEmberAlongPath(float startX, float startY, float endX, float endY, int count) {
        // Convert world positions to tile coordinates for pathfinding
        int startTileX = (int)(startX / gp.tileSize);
        int startTileY =  (int)(startY / gp.tileSize);
        int endTileX =  (int)(endX / gp.tileSize);
        int endTileY =  (int)(endY / gp.tileSize);

        Room room = gp.mapM.currentRoom; // or whichever room this path is in
        gp.pathF.setNodes(startTileX, startTileY, endTileX, endTileY, room);
        if (!gp.pathF.search(room)) return;

        // Clone path nodes so each ember moves independently
        List<Node> pathCopy = new ArrayList<>(gp.pathF.pathList);
        activeEmberPaths.add(new EmberPath(pathCopy, true));
    }
    
    public void draw(Graphics2D g, int xDiff, int yDiff) {
        List<Particle> copy = new ArrayList<>(particles);
        for (Particle p : copy) {
        	if(p != null) {
        		p.draw(g, xDiff, yDiff);
        	}
        }
    }
    public void drawEmissive(Graphics2D g, int xDiff, int yDiff) {
        List<Particle> copy = new ArrayList<>(particles);
        for (Particle p : copy) {
        	if(p != null) {
        		p.drawEmissive(g, xDiff, yDiff);
        	}
        }
    }
    
    public void clear() {
        particles.clear();
    }
	
}
