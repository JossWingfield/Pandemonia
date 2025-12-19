package utility.cutscene;

import entity.Entity;
import entity.npc.NPC;
import main.GamePanel;

public class EndCutscene extends CutsceneEvent {

	  private GamePanel gp;
	  private NPC npc;

	  public EndCutscene(GamePanel gp) {
	        this.gp = gp;
	    }
	  public EndCutscene(GamePanel gp, NPC npc) {
	        this.gp = gp;
	        this.npc = npc;
	    }

	    
	    public void update(double dt) {
	        gp.camera.reset();
	        finished = true;
	        if(npc != null) {
	            npc.setAbleToUpdate(true);
	        }
	    }
}
