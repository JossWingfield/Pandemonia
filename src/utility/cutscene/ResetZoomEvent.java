package utility.cutscene;

import entity.Entity;
import main.GamePanel;

public class ResetZoomEvent extends CutsceneEvent {

	  private GamePanel gp;

	    public ResetZoomEvent(GamePanel gp) {
	        this.gp = gp;
	    }

	    
	    public void update() {
	        // Camera will automatically lerp to target position and zoom
	        gp.camera.follow(gp.player);
	        gp.camera.resetToDefaultZoom();
	        finished = true;
	    }
}