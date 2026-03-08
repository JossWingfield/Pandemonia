package entity.items;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.TextureRegion;

public abstract class Coating extends Food {
	
	public Coating(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
		foodLayer = 5;
		drawWidth = 48;
		drawHeight = 48;
	}
	public void inputUpdate(double dt) {
		super.inputUpdate(dt);
            // Check for nearby table or plate
            var table = gp.world.buildingM.findTable(
                gp.player.interactHitbox.x, 
                gp.player.interactHitbox.y, 
                gp.player.interactHitbox.width, 
                gp.player.interactHitbox.height
            );

            if (table != null && table.currentItem instanceof Food food) {
                // If player presses E to apply seasoning
                if (gp.keyL.keyBeginPress(GLFW.GLFW_KEY_E) && gp.player.clickCounter == 0) {
                	if(food.canBeCoated()) {
	                	food.addCoating(this);
	                    gp.player.currentItem = null;
	                    gp.player.clickCounter = 0.1;
                	}
                }
            }
	}
	
}
