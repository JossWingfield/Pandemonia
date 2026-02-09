package utility.cutscene;

import entity.npc.NPC;
import main.GamePanel;

public class RemoveNPCEvent extends CutsceneEvent {

	  private GamePanel gp;
	  private NPC npc;

	  public RemoveNPCEvent(GamePanel gp, NPC npc) {
		  this.gp = gp;
	      this.npc = npc;
	  }

	  public void update(double dt) {
		  npc.removeLights();
		  gp.world.npcM.removeNPC(npc);
	      finished = true;
	  }
}
