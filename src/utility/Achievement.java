package utility;

import java.awt.Color;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class Achievement {

	GamePanel gp;
    private final String id;    // unique identifier for saving
    private final String name;            // display name
    private final String description;     // description shown to player
    private final BufferedImage icon;     // image to display
    
    private boolean unlocked;             // unlocked state
    private Runnable onUnlock;            // optional callback

    public Achievement(GamePanel gp, String id, String name, String description, BufferedImage icon) {
        this.gp = gp;
    	this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.unlocked = false;
    }

    // --- Getters ---

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BufferedImage getIcon() {
        return icon;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    // --- Unlock Logic ---

    public void unlock() {
        if (!unlocked) {
            unlocked = true;
            if (onUnlock != null) {
                onUnlock.run(); // trigger reward, cosmetic, etc.
                if (gp.progressM != null) {
                    gp.gui.showAchievementNotification(this);
                    gp.gui.addMessage("New Achievement Unlocked!", Color.YELLOW);
                }
            }
        }
    }
    public void unlockNoReward() {
        if (!unlocked) {
            unlocked = true;
        }
    }

    // Optional: assign a callback for when unlocked
    public void setOnUnlock(Runnable onUnlock) {
        this.onUnlock = onUnlock;
    }
}
