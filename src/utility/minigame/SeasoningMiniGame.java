package utility.minigame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import entity.items.Plate;
import entity.items.Seasoning;

import java.util.Iterator;

import main.GamePanel;

public class SeasoningMiniGame {

    private GamePanel gp;
    private boolean active = false;

    private static class Beat {
        float x;
        boolean hit = false;
        float scoreWeight = 0f; // weight for this beat
    }

    private final List<Beat> beats = new ArrayList<>();
    private final Random random = new Random();

    // Beat timing
    private double beatSpawnTimer = 0;
    private double burstTimer = 0;       // Time between bursts
    private int beatsInBurst = 0;     // Remaining beats in this burst
    private float beatSpeed = 0.013f; // Movement speed

    // Hit zone
    private final float hitZoneCenter = 0.5f;
    private final float hitZoneWidth = 0.06f;

    // Feedback
    private String resultText = "";
    private double resultTimer = 0;
    private double clickCooldown = 0;

    // Progress
    private float totalScore = 0f; 
    private int beatsHit = 0;
    private int totalBeats = 0;
    private int maxBursts = 2;
    
    private BufferedImage borderImg;
    private BufferedImage hitZoneImg;
    private BufferedImage beatImg;
    
    private int SCALE = 3; 

    // Pattern modifiers
    private boolean fastPattern = false; // Whether current burst is tight
    private int currentBurstLength = 0;
    
    private Plate plate;
    private Seasoning seasoning;
    
    private Font nameFont = new Font("monogram", Font.PLAIN, 20);

    public SeasoningMiniGame(GamePanel gp) {
        this.gp = gp;
        loadImages();
    }
    
    private void loadImages() {
        try {
            borderImg = ImageIO.read(getClass().getResourceAsStream("/UI/minigame/SeasoningBorder.png"));
            hitZoneImg = ImageIO.read(getClass().getResourceAsStream("/UI/minigame/SeasoningIcon.png"));
            beatImg = ImageIO.read(getClass().getResourceAsStream("/UI/minigame/Beat.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(Plate p, Seasoning s) {
    	this.plate = p;
    	this.seasoning = s;
        active = true;
        beats.clear();
        resultText = "";
        resultTimer = 0;
        clickCooldown = 0.01;
        beatsHit = 0;
        totalBeats = 0;

        beatSpawnTimer = 0.66;
        burstTimer = 1;
        beatsInBurst = 0;
    }

    public boolean isActive() {
        return active;
    }

    public void update(double dt) {
        if (!active) return;

        // Handle bursts
        if (beatsInBurst > 0) {
            beatSpawnTimer-=dt;
            if (beatSpawnTimer <= 0) {
                spawnBeat();
                beatsInBurst--;
                beatSpawnTimer = fastPattern ? 0.1 + random.nextDouble(0.08) : 0.5 + random.nextDouble(0.25);
            }
        } else {
            burstTimer-=dt;
            if (burstTimer <= 0 && totalBeats / 4 < maxBursts) {
                currentBurstLength = 3 + random.nextInt(3);
                beatsInBurst = currentBurstLength;
                fastPattern = random.nextFloat() < 0.5f;
                beatSpawnTimer = 0;
                burstTimer = 1.4 + random.nextDouble(1.00);
            }
        }

        // Move beats
        Iterator<Beat> it = beats.iterator();
        while (it.hasNext()) {
            Beat beat = it.next();
            beat.x -= beatSpeed*dt;

            if (beat.x < -0.1f && !beat.hit) {
                resultText = "MISS!";
                resultTimer = 0.45;
                beat.scoreWeight = 0f; // miss
                totalScore += beat.scoreWeight;
                it.remove();
            } else if (beat.x < -0.2f) {
                it.remove();
            }
        }

    	if (clickCooldown > 0) {
			clickCooldown -= dt;        // subtract elapsed time in seconds
		    if (clickCooldown < 0) {
		    	clickCooldown = 0;      // clamp to zero
		    }
		}
        // Handle input
        if (gp.keyI.ePressed && clickCooldown == 0) {
            clickCooldown = 0.1;
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
                if (closestDist < hitZoneWidth * 0.4f) {
                    resultText = "PERFECT!";
                    closest.scoreWeight = 1.0f;
                } else {
                    resultText = "GOOD!";
                    closest.scoreWeight = 0.6f;
                }
                closest.hit = true;
                totalScore += closest.scoreWeight;
                beats.remove(closest);
                resultTimer = 0.45;
            } else {
                resultText = "MISS!";
                resultTimer = 0.45;
                totalScore += 0f; // miss
            }
        }

    	if (resultTimer > 0) {
    		resultTimer -= dt;        // subtract elapsed time in seconds
		    if (resultTimer < 0) {
		    	resultTimer = 0;      // clamp to zero
		    }
		}

        // End game
        if ((totalBeats / 4 >= maxBursts) && beats.isEmpty() && beatsInBurst == 0) {
            active = false;
            gp.minigameM.miniGameActive = false;

            // Calculate final quality
            float quality = totalBeats > 0 ? totalScore / totalBeats : 0f;

            if (plate != null && seasoning != null) {
                plate.addSeasoning(seasoning, quality);
            }
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

        // === Draw border background ===
        int borderW = borderImg.getWidth() * SCALE;
        int borderH = borderImg.getHeight() * SCALE;
        g2.drawImage(borderImg, centerX - borderW / 2, centerY - borderH / 2, borderW, borderH, null);

        // === Define bar area inside the border ===
        int innerBarWidth = (int)(borderW * 0.75);
        int barY = centerY;

        // === Draw hit zone ===
        int hitZoneW = hitZoneImg.getWidth() * SCALE;
        int hitZoneH = hitZoneImg.getHeight() * SCALE;
        int hitX = centerX - hitZoneW / 2;
        int hitY = barY - hitZoneH / 2;
        g2.drawImage(hitZoneImg, hitX, hitY, hitZoneW, hitZoneH, null);

        // === Draw beats ===
        for (Beat beat : beats) {
            int beatX = (int)(centerX - innerBarWidth / 2 + innerBarWidth * beat.x);
            int beatY = centerY - (beatImg.getHeight() * SCALE) / 2;
            g2.drawImage(beatImg,
                    beatX - (beatImg.getWidth() * SCALE) / 2,
                    beatY,
                    beatImg.getWidth() * SCALE,
                    beatImg.getHeight() * SCALE,
                    null);
        }

        // === Feedback ===
        if (resultTimer > 0 && !resultText.isEmpty()) {
            g2.setColor(Color.WHITE);
            g2.setFont(nameFont);
            g2.drawString(resultText, centerX - 25 * SCALE, centerY - borderH / 2 - 10 * SCALE);
        }

    }
}
