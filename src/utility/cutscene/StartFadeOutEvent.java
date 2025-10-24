package utility.cutscene;

import main.GamePanel;

public class StartFadeOutEvent extends CutsceneEvent {

	GamePanel gp;
	
	private boolean started = false;

    public StartFadeOutEvent(GamePanel gp) {
    	this.gp = gp;
    }

    @Override
    public void update() {
    	if(!started) {
    		gp.world.startFadeOut();
    		started = true;
    	}
    	if(!gp.world.fadingOut) {
            finished = true;
    	}
    }
}
