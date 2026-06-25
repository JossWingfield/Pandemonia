package utility.cutscene;

import main.GamePanel;

import java.awt.geom.Rectangle2D;
import java.util.function.BooleanSupplier;

public class ConditionalWaitEvent extends CutsceneEvent {

	GamePanel gp;
    private BooleanSupplier condition;

    public ConditionalWaitEvent(GamePanel gp, BooleanSupplier condition, boolean canChangeRoom) {
    	this.gp = gp;
        this.condition = condition;
    }

    
    public void update(double dt) {
        gp.player.setControlEnabled(true);
        gp.player.canChangeRoom = false;
        if(isFinished()) {
            gp.player.setControlEnabled(false);
            finished = true;
            gp.player.canChangeRoom = true;
        }
    }

    
    public boolean isFinished() {
        // finished when condition returns true
        return condition.getAsBoolean();
    }
}