package utility.cutscene;

import main.GamePanel;

public class StartFadeInEvent extends CutsceneEvent {

	GamePanel gp;
	
	private boolean started = false;

    public StartFadeInEvent(GamePanel gp) {
    	this.gp = gp;
    }

    @Override
    public void update() {
    	if(!started) {
    		gp.world.startFadeIn();
    		started = true;
    	}
    	if(!gp.world.fadingIn) {
            finished = true;
    	}
    }
}
