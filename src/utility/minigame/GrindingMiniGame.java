package utility.minigame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.lwjgl.glfw.GLFW;

import entity.buildings.SpiceTable;
import entity.items.Seasoning;
import main.GamePanel;
import main.renderer.AssetPool;
import main.renderer.BitmapFont;
import main.renderer.Colour;
import main.renderer.Renderer;

public class GrindingMiniGame {

    private GamePanel gp;
    private boolean active = false;
    private boolean finished = false;

    private float sprinkleAngle = 0f;
    private float rotationSpeed = 200f;
    private boolean holding = false;

    private float targetCenterAngle;
    private float targetWidth = 45f;
    private float targetMoveSpeed = 35f;

    private float targetMinAngle;
    private float targetMaxAngle;

    private BitmapFont font;
    private float bowlRadius = 70f;

    private List<SprinkleParticle> particles = new ArrayList<>();
    private String resultText = "";
    private double resultTimer = 0;

    private SpiceTable plate;
    private Seasoning seasoning;

    private Random rand = new Random();
    
    private ArrayList<Colour> particleColours = null;

    public GrindingMiniGame(GamePanel gp) {
        this.gp = gp;
        font = AssetPool.getBitmapFont("/UI/monogram.ttf", 32);
    }

    public void start(SpiceTable plate, Seasoning seasoning) {
        this.plate = plate;
        this.seasoning = seasoning;

        active = true;
        finished = false;
        holding = false;
        sprinkleAngle = 0f;
        resultText = "";
        resultTimer = 0;
        particles.clear();
        
        setParticleColours(seasoning.getColours());

        generateTargetZone();
        gp.world.minigameM.miniGameActive = true;
    }

    private void generateTargetZone() {
        targetCenterAngle = rand.nextFloat() * 360f;
        updateTargetBounds();
    }

    private void updateTargetBounds() {
        targetMinAngle = targetCenterAngle - targetWidth / 2f;
        targetMaxAngle = targetCenterAngle + targetWidth / 2f;
    }
    public void setParticleColours(ArrayList<Colour> colours) {
        this.particleColours = colours;
    }
    public boolean isActive() {
        return active;
    }

    public void inputUpdate(double dt) {
        if (!active || finished) return;

        boolean keyDown = gp.keyL.isKeyPressed(GLFW.GLFW_KEY_E);

        if (keyDown && !holding) {
            holding = true; // start rotating
        }

        // Key released
        if (!keyDown && holding) {
            holding = false;
            // Only now finish and check if inside the target
            finish(); 
        }
    }

    public void updateState(double dt) {
        if (!active) return;

        // Only move target if minigame not finished
        if (!finished) {
            targetCenterAngle += targetMoveSpeed * dt;
            targetCenterAngle = normalizeAngle(targetCenterAngle);
            updateTargetBounds();
        }

        // Rotate sprinkle if holding
        if (holding && !finished) {
            sprinkleAngle += rotationSpeed * dt;
            sprinkleAngle = normalizeAngle(sprinkleAngle);
            spawnSprinkleParticles();
        }

        updateParticles(dt);

        // Handle result display timer
        if (finished) {
            resultTimer -= dt;
            if (resultTimer <= 0) {
                resultTimer = 0;
                active = false;
                finished = false;
                particles.clear();
                sprinkleAngle = 0f;
                resultText = "";
                gp.world.minigameM.miniGameActive = false;
            }
        }
    }

    private void finish() {
        if (finished) return;

        float finalAngle = normalizeAngle(sprinkleAngle);
        float quality = 0f;

        if (isAngleInside(finalAngle, targetMinAngle, targetMaxAngle)) {
            float dist = angleDistance(finalAngle, targetCenterAngle);
            float maxDist = targetWidth / 2f;
            float score = 1f - (dist / maxDist);
            quality = score;

            if (score > 0.8f) {
                resultText = "PERFECT GRIND!";
                gp.screenShake(12, 3);
            } else {
                resultText = "GOOD GRIND!";
            }
        } else {
            resultText = "MISS!";
            quality = 0.2f;
        }

        if (plate != null && seasoning != null && !resultText.equals("MISS!")) {
            plate.addSeasoning(seasoning, quality);
        }

        resultTimer = 0.9;  // show result for 0.9s
        finished = true;
        holding = false;    // stop rotation immediately
    }

    // --------------------------
    // Angle helpers
    // --------------------------
    private float normalizeAngle(float angle) {
        angle %= 360f;
        if (angle < 0) angle += 360f;
        return angle;
    }

    private float angleDistance(float a, float b) {
        float diff = Math.abs(a - b) % 360f;
        return diff > 180f ? 360f - diff : diff;
    }

    private boolean isAngleInside(float angle, float min, float max) {
        angle = normalizeAngle(angle);
        min = normalizeAngle(min);
        max = normalizeAngle(max);
        return min < max ? angle >= min && angle <= max : angle >= min || angle <= max;
    }

    private float clamp(float v) { return Math.max(0f, Math.min(1f, v)); }

    // --------------------------
    // Rendering
    // --------------------------
    public void draw(Renderer renderer) {
        if (!active) return;

        for (SprinkleParticle p : particles) p.draw(renderer);
        drawTargetIndicator(renderer);

        if (finished && resultTimer > 0 && !resultText.isEmpty()) {
            renderer.drawString(
                    font,
                    resultText,
                    gp.frameWidth / 2 - 70,
                    gp.frameHeight / 2 - 90,
                    1.0f,
                    Colour.WHITE
            );
        }
    }
    public void drawEmissive(Renderer renderer) {
    	 if (!active) return;

         for (SprinkleParticle p : particles) p.drawEmissive(renderer);
         drawTargetIndicator(renderer);
    }

    private void drawTargetIndicator(Renderer renderer) {
        int centerX = gp.frameWidth / 2;
        int centerY = gp.frameHeight / 2;

        for (int i = 0; i < 24; i++) {
            float t = i / 23f;
            float angle = targetMinAngle + (targetWidth * t);
            float rad = (float)Math.toRadians(angle);
            float x = centerX + (float)Math.cos(rad) * (bowlRadius + 12f);
            float y = centerY + (float)Math.sin(rad) * (bowlRadius + 12f);
            renderer.fillRect(x - 2, y - 2, 4, 4,
                    new Colour(1f, 0.85f, 0.2f, 1f));
        }
    }
    private void spawnSprinkleParticles() {
        int centerX = gp.frameWidth / 2;
        int centerY = gp.frameHeight / 2;

        float rad = (float)Math.toRadians(sprinkleAngle);
        float spawnX = centerX + (float)Math.cos(rad) * bowlRadius;
        float spawnY = centerY + (float)Math.sin(rad) * bowlRadius;

        Colour base;
        if (particleColours != null && !particleColours.isEmpty()) {
            base = particleColours.get(rand.nextInt(particleColours.size()));
        } else {
            base = Colour.GREEN;
        }

        for (int i = 0; i < 3; i++) {
            float offsetX = rand.nextFloat() * 6f - 3f;
            float offsetY = rand.nextFloat() * 6f - 3f;
            float vx = rand.nextFloat() * 0.6f - 0.3f;
            float vy = 1.2f + rand.nextFloat() * 0.8f;

            // Make a copy of the colour for this particle
            Colour bright = new Colour(
                clamp(base.r * 1.3f),  // increase red
                clamp(base.g * 1.3f),  // increase green
                clamp(base.b * 1.3f),  // increase blue
                base.a                 // keep alpha
            );

            particles.add(new SprinkleParticle(
                spawnX + offsetX,
                spawnY + offsetY,
                vx,
                vy,
                bright
            ));
        }
    }
    private void updateParticles(double dt) {
        Iterator<SprinkleParticle> it = particles.iterator();
        while (it.hasNext()) {
            SprinkleParticle p = it.next();
            p.update(dt);
            if (p.dead) it.remove();
        }
    }

    // --------------------------
    // Sprinkle Particle
    // --------------------------
    private static class SprinkleParticle {
        float x, y, vx, vy, lifetime, size;
        boolean dead = false;
        Colour colour;

        SprinkleParticle(float x, float y, float vx, float vy, Colour colour) {
            this.x = x; this.y = y; this.vx = vx; this.vy = vy;
            this.colour = colour;
            this.lifetime = 0.5f + (float)Math.random() * 0.3f;
            this.size = 3f + (float)Math.random() * 2f;
        }

        void update(double dt) {
            x += vx * 60f * dt;
            y += vy * 60f * dt;
            lifetime -= dt;

            // Fade alpha from 1 -> 0 over lifetime
            //colour.a = clamp(lifetime / 0.8f);

            if (lifetime <= 0) dead = true;
        }
        private float clamp(float v) { return Math.max(0f, Math.min(1f, v)); }

        void draw(Renderer renderer) {
            renderer.fillRect(x - size / 2f, y - size / 2f, size, size, colour);
        }
        void drawEmissive(Renderer renderer) {
            renderer.fillRect(x - size / 2f, y - size / 2f, size, size, colour);
        }
    }
}