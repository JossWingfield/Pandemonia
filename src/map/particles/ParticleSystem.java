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
    public boolean roomEmbers = false;
    private boolean dustActive = true;
    private int dustTargetCount = 80;
    private float startX, startY, endX, endY;
    private int count;
    private boolean startEmbers = false;
    //SHAKING
    public boolean randomShaking = false;
    private int shakeCooldown = 0;
    private final Random rand = new Random();
    
    //PAN EMBERS
    private static class PanEmber {
        int slot;       // 0, 1, etc.
        float x, y, width, height;
        boolean active;

        public PanEmber(int slot, float x, float y, float width, float height) {
            this.slot = slot;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.active = true;
        }
    }

    private final List<PanEmber> panEmbers = new ArrayList<>();
    
    private static final int ROOM_EMBER_TARGET = 7000;
    private static final int ROOM_EMBER_SPAWN_RATE = 1; // frames
    private static final float ROOM_FLARE_CHANCE = 0.05f;
    
    private static final int ROOM_EMBER_BURST_MIN = 4;
    private static final int ROOM_EMBER_BURST_MAX = 14;

    private static final float CLUMP_RADIUS = 24f; // pixels
    private static final float CLUMP_CHANCE = 0.35f;
    
    //FREEZER MIST
    private boolean freezerMistActive = false;
    private int freezerMistTarget = 300;
    private float mistAreaX, mistAreaY, mistAreaW, mistAreaH;
  
    
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
        
        if(gp.player == null) {
        	return;
        }
        
        dustActive = gp.world.mapM.currentRoom.darkerRoom;
        
        if (dustActive) {
            spawnCooldown--;
            if (spawnCooldown <= 0) {
                spawnCooldown = 3;
                while (particles.size() < dustTargetCount) {
                    float x = (float) (Math.random() * gp.frameWidth);
                    float y = (float) (Math.random() * gp.frameHeight);
                    addParticle(new DustParticle(gp, gp.player.currentRoomIndex, x, y));
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
	                addParticle(new FireflyParticle(gp, gp.player.currentRoomIndex, x, y, lifetime));
	            }
	        }
        }
        
        if (roomEmbers) {
            spawnCooldown--;

            if (spawnCooldown <= 0) {
                spawnCooldown = ROOM_EMBER_SPAWN_RATE;

                int emberCount = 0;
                for (Particle p : particles) {
                    if (p instanceof EmberParticle) emberCount++;
                }

                if (emberCount < ROOM_EMBER_TARGET) {

                    float baseX = rand.nextFloat() * gp.frameWidth;
                    float baseY = rand.nextFloat() * gp.frameHeight;

                    boolean clump = rand.nextFloat() < CLUMP_CHANCE;
                    int burstCount = clump
                        ? ROOM_EMBER_BURST_MIN + rand.nextInt(ROOM_EMBER_BURST_MAX)
                        : 1;

                    for (int i = 0; i < burstCount && emberCount < ROOM_EMBER_TARGET; i++) {

                        float x = baseX;
                        float y = baseY;

                        if (clump) {
                            float angle = rand.nextFloat() * (float)Math.PI * 2f;
                            float radius = rand.nextFloat() * CLUMP_RADIUS;
                            x += Math.cos(angle) * radius;
                            y += Math.sin(angle) * radius;
                        }

                        boolean flare = rand.nextFloat() < ROOM_FLARE_CHANCE;
                        addParticle(new EmberParticle(gp, gp.player.currentRoomIndex, x, y, flare));

                        emberCount++;
                    }
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

            	float FLARE_CHANCE = 0.06f; // ~6% of embers

            	boolean flare = ep.withLight &&
            	                Math.random() < FLARE_CHANCE;

                addParticle(new EmberParticle(gp, gp.player.currentRoomIndex, pos[0], pos[1], flare));
            }
        }
        if (!panEmbers.isEmpty()) {
            spawnCooldown--;

            if (spawnCooldown <= 0) {
                spawnCooldown = 5; // slower spawn for pan embers (less frequent)

                int totalPanEmbers = 0;
                for (Particle p : particles) {
                    if (p instanceof EmberParticle) totalPanEmbers++;
                }

                int maxPanEmbers = 20; // total maximum across all pans

                for (PanEmber pan : panEmbers) {
                    if (!pan.active) continue;

                    boolean clump = rand.nextFloat() < CLUMP_CHANCE;
                    int burstCount = clump ? 1 : 1; // only 1 per spawn for low density

                    for (int i = 0; i < burstCount && totalPanEmbers < maxPanEmbers; i++) {
                        float x = pan.x + rand.nextFloat() * pan.width - pan.width / 2;
                        float y = pan.y + rand.nextFloat() * pan.height - pan.height / 2;

                        // optional clump randomness
                        if (clump) {
                            float angle = rand.nextFloat() * (float)Math.PI * 2f;
                            float radius = rand.nextFloat() * CLUMP_RADIUS * 0.3f; // smaller clump
                            x += Math.cos(angle) * radius;
                            y += Math.sin(angle) * radius;
                        }

                        boolean flare = rand.nextFloat() < ROOM_FLARE_CHANCE;
                        addParticle(new EmberParticle(gp, gp.player.currentRoomIndex, x, y, flare));

                        totalPanEmbers++;
                    }
                }
            }
        }
        if (freezerMistActive) {

            int mistCount = 0;
            for (Particle p : particles) {
                if (p instanceof FreezerMistParticle) mistCount++;
            }

            while (mistCount < freezerMistTarget) {

                float x = mistAreaX + rand.nextFloat() * mistAreaW;
                float y = mistAreaY + rand.nextFloat() * mistAreaH;

                addParticle(new FreezerMistParticle(
                        gp,
                        gp.player.currentRoomIndex,
                        x, y));

                mistCount++;
            }
            float playerX = gp.player.interactHitbox.x + gp.player.interactHitbox.width/2;
            float playerY = gp.player.interactHitbox.y + gp.player.interactHitbox.height/2;

            float clearRadius = 60f;

            for (Particle p : particles) {
                if (p instanceof FreezerMistParticle) {

                    float dx = p.x - playerX;
                    float dy = p.y - playerY;

                    float distSq = dx * dx + dy * dy;

                    if (distSq < clearRadius * clearRadius) {

                        float dist = (float)Math.sqrt(distSq);
                        if (dist < 0.01f) dist = 0.01f;

                        float pushStrength = (clearRadius - dist) * 0.15f;

                        p.x += (dx / dist) * pushStrength;
                        p.y += (dy / dist) * pushStrength;

                        // Optional: fade alpha near player
                        p.colour.a = 0.05f;
                    } else {
                        p.colour.a = 0.12f; // normal opacity
                    }
                }
            }
        }
        if (randomShaking) {
            if (shakeCooldown <= 0) {
                // Small random chance each frame to trigger a shake
                    int duration = 10 + rand.nextInt(15);   // random duration between 10–25 frames
                    int intensity = 1 + rand.nextInt(3) * 2; // random intensity between 1.5–3.5
                    gp.screenShake(duration, intensity);
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

        Room room = gp.world.mapM.currentRoom; // or whichever room this path is in
        gp.world.pathF.setNodes(startTileX, startTileY, endTileX, endTileY, room);
        if (!gp.world.pathF.search(room)) return;

        // Clone path nodes so each ember moves independently
        List<Node> pathCopy = new ArrayList<>(gp.world.pathF.pathList);
        activeEmberPaths.add(new EmberPath(pathCopy, true));
    }
    public void setSpawnEmbers(float startX, float startY, float endX, float endY, int count) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.count = count;

        spawnEmberAlongPath(startX, startY, endX, endY, count);
        //startEmbers = false; // IMPORTANT
        startEmbers = true;
    }
    public void stopEmbers() {
    	startEmbers = false;
    }
    public void draw(Renderer renderer) {
    	for (int i = 0; i < particles.size(); i++) {
    	    Particle p = particles.get(i);
    	    if (p != null) {
    	    	if(p.roomNum == gp.player.currentRoomIndex) {
    	    		p.draw(renderer); 
    	    	}
    	    }
    	}
    }
    public void setRandomShaking(boolean isShaking) {
    	this.randomShaking = isShaking;
    }
    public void drawEmissive(Renderer renderer) {
    	for (int i = 0; i < particles.size(); i++) {
    	    Particle p = particles.get(i);
    	    if (p != null) p.drawEmissive(renderer);
    	}
    }
    // Start ember for a pan slot
    public void addPanEmber(int slot, float x, float y, float width, float height) {
        // Check if this slot already exists
        for (PanEmber p : panEmbers) {
            if (p.slot == slot) {
                // update existing pan position & size
                p.x = x;
                p.y = y;
                p.width = width;
                p.height = height;
                p.active = true;
                return;
            }
        }
        // Add new pan ember for this slot
        panEmbers.add(new PanEmber(slot, x, y, width, height));
    }

    // Stop ember for a pan slot
    public void removePanEmber(int slot) {
        for (PanEmber p : panEmbers) {
            if (p.slot == slot) {
                p.active = false;
                return;
            }
        }
    }
    public void startFreezerMist(float x, float y, float width, float height) {
        freezerMistActive = true;
        mistAreaX = x;
        mistAreaY = y;
        mistAreaW = width;
        mistAreaH = height;
    }

    public void stopFreezerMist() {
        freezerMistActive = false;
    }

    // Optional: stop all
    public void clearPanEmbers() {
        panEmbers.clear();
    }
    public void clear() {
        particles.clear();
    }
	
}
