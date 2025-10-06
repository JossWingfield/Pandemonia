package utility.minigame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Iterator;

import main.GamePanel;

public class SeasoningMiniGame {

    private GamePanel gp;
    private boolean active = false;

    private static class Beat {
        float x;
        boolean hit = false;
    }

    private final List<Beat> beats = new ArrayList<>();
    private final Random random = new Random();

    // Beat timing
    private int beatSpawnTimer = 0;
    private int burstTimer = 0;      // Time between bursts
    private int beatsInBurst = 0;    // How many beats remain in this burst
    private float beatSpeed = 0.015f; // Speed (slightly faster for tighter timing)

    // Hit zone
    private final float hitZoneCenter = 0.5f;
    private final float hitZoneWidth = 0.06f;

    // Feedback
    private String resultText = "";
    private int resultTimer = 0;
    private int clickCooldown = 0;

    // Progress
    private int beatsHit = 0;
    private int totalBeats = 0;
    private int maxBursts = 4; // total waves before end

    public SeasoningMiniGame(GamePanel gp) {
        this.gp = gp;
    }

    public void start() {
        active = true;
        beats.clear();
        resultText = "";
        resultTimer = 0;
        clickCooldown = 2;
        beatsHit = 0;
        totalBeats = 0;

        beatSpawnTimer = 40;
        burstTimer = 60;
        beatsInBurst = 0;
    }

    public boolean isActive() {
        return active;
    }

    public void update() {
        if (!active) return;

        // Handle bursts
        if (beatsInBurst > 0) {
            beatSpawnTimer--;
            if (beatSpawnTimer <= 0) {
                spawnBeat();
                beatsInBurst--;
                beatSpawnTimer = 25 + random.nextInt(10); // spacing between beats in the same burst
            }
        } else {
            burstTimer--;
            if (burstTimer <= 0 && totalBeats / 4 < maxBursts) { 
                // Start new burst
                beatsInBurst = 3 + random.nextInt(3); // 3–5 beats per burst
                beatSpawnTimer = 0;
                burstTimer = 120 + random.nextInt(30); // time between bursts
            }
        }

        // Move beats
        Iterator<Beat> it = beats.iterator();
        while (it.hasNext()) {
            Beat beat = it.next();
            beat.x -= beatSpeed;

            if (beat.x < -0.1f && !beat.hit) {
                resultText = "MISS!";
                resultTimer = 25;
                it.remove();
            } else if (beat.x < -0.2f) {
                it.remove();
            }
        }

        if (clickCooldown > 0) clickCooldown--;

        // Input check
        if (gp.keyI.ePressed && clickCooldown == 0) {
            clickCooldown = 8;
            Beat closest = null;
            float closestDist = Float.MAX_VALUE;

            for (Beat beat : beats) {
                float diff = Math.abs(beat.x - hitZoneCenter);
                if (diff < closestDist) {
                    closestDist = diff;
                    closest = beat;
                }
            }

            if (closest != null && closestDist < 0.12f) {
                if (closestDist < hitZoneWidth * 0.4f) resultText = "PERFECT!";
                else resultText = "GOOD!";
                closest.hit = true;
                beatsHit++;
                beats.remove(closest);
                resultTimer = 25;
            } else {
                resultText = "MISS!";
                resultTimer = 25;
            }
        }

        if (resultTimer > 0) resultTimer--;

        // End game when enough bursts are complete and no beats remain
        if ((totalBeats / 4 >= maxBursts) && beats.isEmpty() && beatsInBurst == 0) {
            active = false;
            gp.minigameM.miniGameActive = false;
            // TODO: Apply seasoning accuracy bonus here
        }
    }

    private void spawnBeat() {
        Beat beat = new Beat();
        beat.x = 1.2f;
        beats.add(beat);
        totalBeats++;
    }

    public void draw(Graphics2D g2) {
        if (!active) return;

        int centerX = gp.frameWidth / 2;
        int centerY = gp.frameHeight / 2;

        // Smaller bar
        int barWidth = 250;
        int barHeight = 8;

        // Base bar
        g2.setColor(new Color(60, 60, 60));
        g2.fillRect(centerX - barWidth / 2, centerY - barHeight / 2, barWidth, barHeight);

        // Hit zone
        int hitW = (int) (barWidth * hitZoneWidth);
        int hitX = (int) (centerX - hitW / 2);
        g2.setColor(new Color(100, 255, 100, 120));
        g2.fillRect(hitX, centerY - barHeight / 2 - 8, hitW, barHeight + 16);

        // Beats
        for (Beat beat : beats) {
            int x = (int) (centerX - barWidth / 2 + barWidth * beat.x);
            g2.setColor(Color.WHITE);
            g2.fillRect(x - 4, centerY - 12, 8, 24);
        }

        // Feedback
        if (resultTimer > 0 && !resultText.isEmpty()) {
            g2.setColor(Color.WHITE);
            g2.drawString(resultText, centerX - 25, centerY - 35);
        }

        // Score
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawString("Hits: " + beatsHit + " / " + totalBeats, centerX - 40, centerY + 40);
    }
}
