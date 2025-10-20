package utility.cutscene;

import entity.npc.NPC;
import main.GamePanel;

public class AddNPCEvent extends CutsceneEvent {

	  private GamePanel gp;
	  private NPC npc;

	    public AddNPCEvent(GamePanel gp, NPC npc) {
	        this.gp = gp;
	        this.npc = npc;
	        npc.setAbleToUpdate(false);
	    }

	    @Override
	    public void update() {
	    	npc.hitbox.x = gp.mapM.currentRoom.getSpawnX();
	    	npc.hitbox.y = gp.mapM.currentRoom.getSpawnY();
	    	gp.npcM.addNPC(npc);
	        finished = true;
	    }
}
