package utility;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.TextureRegion;

public class Achievement {

	GamePanel gp;
    private final String id;    // unique identifier for saving
    private final String name;            // display name
    private final String description;     // description shown to player
    private final TextureRegion icon;     // image to display
    
    private boolean unlocked;             // unlocked state
    private Runnable onUnlock;            // optional callback

    public Achievement(GamePanel gp, String id, String name, String description, TextureRegion icon) {
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

    public TextureRegion getIcon() {
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
                if (gp.world.progressM != null) {
                    gp.gui.showAchievementNotification(this);
                    gp.gui.addMessage("New Achievement Unlocked!", Colour.YELLOW);
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
