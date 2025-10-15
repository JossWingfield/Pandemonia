package utility.cutscene;

import entity.Entity;
import main.GamePanel;

public class EndCutscene extends CutsceneEvent {

	  private GamePanel gp;

	  public EndCutscene(GamePanel gp) {
	        this.gp = gp;
	    }

	    @Override
	    public void update() {
	        gp.camera.follow(gp.player);
	        gp.camera.resetToDefaultZoom();
	        finished = true;
	    }
}
