package utility.cutscene;

import main.GamePanel;

public class StartFadeOutEvent extends CutsceneEvent {

	GamePanel gp;
	
	private boolean started = false;

    public StartFadeOutEvent(GamePanel gp) {
    	this.gp = gp;
    }

    public void update(double dt) {
    	if(!started) {
    		gp.world.gameM.startFadeOut();
    		started = true;
    	}
    	if(!gp.world.gameM.fadingOut) {
            finished = true;
    	}
    }
}
