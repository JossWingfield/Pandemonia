package utility;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

import org.lwjgl.glfw.GLFW;

import main.GamePanel;

public class Debug {
	
	GamePanel gp;
	
    public boolean debugBloom = true;
    private int selectedBloomParam = 0; // 0 = threshold, 1 = strength, 2 = intensity
    
    private int adjustTimer = 0;
    private final int adjustDelay = 6; 
	
	public Debug(GamePanel gp) {
		this.gp = gp;
	}
	
	public void update(double dt) {
		handleDebugInput();
	}
	public void draw(Graphics2D g2) {
		 if (debugBloom) {
			 /*
	            int overlayHeight = 60;
	            int screenWidth = gp.frameWidth;
	            int screenHeight = gp.frameHeight;

	            // --- Background bar ---
	            g2.setComposite(AlphaComposite.SrcOver);
	            g2.setColor(new Color(0, 0, 0, 180)); // dark semi-transparent bar
	            renderer.fillRect(0, screenHeight - overlayHeight, screenWidth, overlayHeight);

	            // --- Text setup ---
	            g2.setColor(Color.WHITE);
	            g2.setFont(g2.getFont().deriveFont(14f));

	            String title = "🌙 Bloom Debug Mode (= to toggle, SHIFT to switch)";
	            String info = String.format(
	                "[%s] Threshold: %d   Strength: %d   Intensity: %.2f",
	                selectedBloomParam == 0 ? "Threshold" :
	                selectedBloomParam == 1 ? "Strength" : "Intensity",
	                gp.lightingM.bloomThreshold, gp.lightingM.bloomStrength, gp.lightingM.bloomIntensity
	            );

	            // --- Centered text ---
	            int titleWidth = g2.getFontMetrics().stringWidth(title);
	            int infoWidth  = g2.getFontMetrics().stringWidth(info);

	            renderer.drawString(title, (screenWidth - titleWidth) / 2, screenHeight - overlayHeight + 22);
	            renderer.drawString(info,  (screenWidth - infoWidth)  / 2, screenHeight - 15);
	           */
	        }
	}
	
	private void handleDebugInput() {
		// Switch selected parameter (SHIFT)
		if (gp.keyL.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
			selectedBloomParam = (selectedBloomParam + 1) % 3;
		}

		// Smooth key repeat limiter
		if (adjustTimer > 0) adjustTimer--;

		if (adjustTimer == 0) {
			if (gp.keyL.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
				adjustBloom(-1);
				adjustTimer = adjustDelay;
			}
			if (gp.keyL.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
				adjustBloom(1);
				adjustTimer = adjustDelay;
			}
		}
	}

    private void adjustBloom(int dir) {
        switch (selectedBloomParam) {
            case 0: // Threshold
            	gp.lightingM.bloomThreshold = Math.max(0, Math.min(255, gp.lightingM.bloomThreshold + dir * 5));
                break;
            case 1: // Strength
            	gp.lightingM.bloomStrength = Math.max(1, Math.min(32, gp.lightingM.bloomStrength + dir));
                break;
            case 2: // Intensity
            	gp.lightingM.bloomIntensity = Math.max(0f, Math.min(3f, gp.lightingM.bloomIntensity + dir * 0.01f));
                break;
        }
    }
	
}
