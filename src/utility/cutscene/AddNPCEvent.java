package utility.cutscene;

import entity.npc.NPC;
import main.GamePanel;

public class AddNPCEvent extends CutsceneEvent {

	  private GamePanel gp;
	  private NPC npc;
	  private int spawnX = -1, spawnY = -1;

	    public AddNPCEvent(GamePanel gp, NPC npc) {
	        this.gp = gp;
	        this.npc = npc;
	        //npc.setAbleToUpdate(false);
	    }
	    public AddNPCEvent(GamePanel gp, NPC npc, int x, int y) {
	        this.gp = gp;
	        this.npc = npc;
	        this.spawnX = x;
	        this.spawnY = y;
	        //npc.setAbleToUpdate(false);
	    }

	    
	    public void update(double dt) {
	    	npc.hitbox.x = gp.world.mapM.currentRoom.getSpawnX();
	    	npc.hitbox.y = gp.world.mapM.currentRoom.getSpawnY();
	    	if(spawnX != -1) {
	        	npc.hitbox.x = spawnX*gp.tileSize;
	          	npc.hitbox.y = spawnY*gp.tileSize;
	    	}
	    	
	    	gp.world.npcM.addNPC(npc);
	        finished = true;
	    }
}
