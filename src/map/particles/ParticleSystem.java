package map.particles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import ai.Node;
import main.GamePanel;
import main.renderer.Renderer;
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
    private float startX, startY, endX, endY;
    private int count;
    private boolean startEmbers = false;
    //SHAKING
    private boolean randomShaking = false;
    private int shakeCooldown = 0;
    private final Random rand = new Random();
    
    public ParticleSystem(GamePanel gp) {
    	this.gp = gp;
    }
    
    public void addParticle(Particle p) {
        particles.add(p);
    }
    public void setDustActive(boolean active) {
        this.dustActive = active;
    }
    
    public void update(double dt) {
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
                    float x = (float) (Math.random() * gp.frameWidth);
                    float y = (float) (Math.random() * gp.frameHeight);
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
	                float x = (float) (Math.random() * gp.frameWidth);
	                float y = (float) (Math.random() * gp.frameHeight);
	                int lifetime = (int) (Math.random() * 200 + 100);
	                addParticle(new FireflyParticle(gp, x, y, lifetime));
	            }
	        }
        }
        if(startEmbers) {
        	spawnEmberAlongPath(startX, startY, endX, endY, count);
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
        if (randomShaking) {
            if (shakeCooldown <= 0) {
                // Small random chance each frame to trigger a shake
                    int duration = 10 + rand.nextInt(15);   // random duration between 10–25 frames
                    int intensity = 1 + rand.nextInt(3) * 2; // random intensity between 1.5–3.5
                    //gp.screenShake(duration, intensity);
                // Reset cooldown so we don’t trigger again too fast
                shakeCooldown = 10 + rand.nextInt(30);
            } else {
                shakeCooldown--;
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
    public void setSpawnEmbers(float startX, float startY, float endX, float endY, int count) {
    	startEmbers = true;
    	this.startX = startX;
    	this.startY = startY;
    	this.endX = endX;
    	this.endY = endY;
    	this.count = count;
    }
    public void stopEmbers() {
    	startEmbers = false;
    }
    public void draw(Renderer renderer) {
        List<Particle> copy = new ArrayList<>(particles);
        for (Particle p : copy) {
        	if(p != null) {
        		p.draw(renderer);
        	}
        }
    }
    public void setRandomShaking(boolean isShaking) {
    	this.randomShaking = isShaking;
    }
    public void drawEmissive(Renderer renderer) {
        List<Particle> copy = new ArrayList<>(particles);
        for (Particle p : copy) {
        	if(p != null) {
        		p.drawEmissive(renderer);
        	}
        }
    }
    
    public void clear() {
        particles.clear();
    }
	
}
