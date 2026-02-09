package utility.cutscene;

import main.GamePanel;

public class StartFadeInEvent extends CutsceneEvent {

	GamePanel gp;
	
	private boolean started = false;

    public StartFadeInEvent(GamePanel gp) {
    	this.gp = gp;
    }

    public void update(double dt) {
    	if(!started) {
    		gp.world.gameM.startFadeIn();
    		started = true;
    	}
    	if(!gp.world.gameM.fadingIn) {
            finished = true;
    	}
    }
}
