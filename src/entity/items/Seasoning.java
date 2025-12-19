package entity.items;

import org.lwjgl.glfw.GLFW;

import main.GamePanel;

public class Seasoning extends Food {
	
	private boolean miniGameStarted = false;
	
	public Seasoning(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		foodLayer = 5;
	}
	
	public void update(double dt) {
            // Check for nearby table or plate
            var table = gp.buildingM.findTable(
                gp.player.interactHitbox.x, 
                gp.player.interactHitbox.y, 
                gp.player.interactHitbox.width, 
                gp.player.interactHitbox.height
            );

            if (table != null && table.currentItem instanceof Plate plate) {
                // If player presses E to apply seasoning
                if (gp.keyL.isKeyPressed(GLFW.GLFW_KEY_E) && !miniGameStarted && gp.player.clickCounter == 0) {
                    miniGameStarted = true;
                    gp.minigameM.startSeasoningMiniGame(plate, this);
                    gp.player.currentItem = null;
                    gp.player.clickCounter = 0.1;
                }
            }
        }
	
}
