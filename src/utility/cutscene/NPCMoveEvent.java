package utility.cutscene;

import java.awt.geom.Rectangle2D;

import entity.buildings.Building;
import entity.npc.NPC;
import main.GamePanel;

public class NPCMoveEvent extends CutsceneEvent {

    private NPC npc;
    private Building targetBuilding;
    private int targetX, targetY;
    private Rectangle2D.Float targetHitbox;

    public NPCMoveEvent(NPC npc, Building building) {
        this.npc = npc;
        this.targetBuilding = building;

        npc.walking = true;
    }
    public NPCMoveEvent(NPC npc, int x, int y) {
        this.npc = npc;
        this.targetX = x;
        this.targetY = y;
        targetHitbox = new Rectangle2D.Float(x*48, y*48, 48, 48);

        npc.walking = true;
    }

    @Override
    public void update() {
        if (npc == null) {
            finished = true;
            return;
        }
        
        npc.walking = true;
        
        if(targetBuilding != null) {
        	// Move NPC along path
	        if(npc.walkToBuilding(targetBuilding)) {
	            finished = true;
	            npc.hitbox.x = targetBuilding.hitbox.x;
	            npc.hitbox.y = targetBuilding.hitbox.y;
	            npc.walking = false; // stop moving
	        };
        }
        
        if(targetX != 0) {
        	npc.walkToPoint(targetX, targetY);
            npc.walking = true; 
        	if(npc.hitbox.intersects(targetHitbox)) {
	            finished = true;
	            npc.walking = false; // stop moving
	        };
        }
    }
}
