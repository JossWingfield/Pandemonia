package utility.cutscene;

import main.GamePanel;

import java.awt.geom.Rectangle2D;
import java.util.function.BooleanSupplier;

public class ConditionalWaitEvent extends CutsceneEvent {

	GamePanel gp;
    private BooleanSupplier condition;

    public ConditionalWaitEvent(GamePanel gp, BooleanSupplier condition) {
    	this.gp = gp;
        this.condition = condition;
    }

    
    public void update() {
        gp.player.setControlEnabled(true);
        if(isFinished()) {
            gp.player.setControlEnabled(false);
            finished = true;
        }
    }

    
    public boolean isFinished() {
        // finished when condition returns true
        return condition.getAsBoolean();
    }
}