package utility.cutscene;

import entity.buildings.Building;
import entity.npc.NPC;
import main.GamePanel;

public class NPCMoveEvent extends CutsceneEvent {

    private NPC npc;
    private Building targetBuilding;

    public NPCMoveEvent(NPC npc, Building building) {
        this.npc = npc;
        this.targetBuilding = building;
        

        npc.walking = true;
    }

    @Override
    public void update() {
        if (npc == null || targetBuilding == null) {
            finished = true;
            return;
        }
        
        npc.walking = true;

        // Move NPC along path
        if(npc.walkToBuilding(targetBuilding)) {
            finished = true;
            npc.hitbox.x = targetBuilding.hitbox.x;
            npc.hitbox.y = targetBuilding.hitbox.y;
            npc.walking = false; // stop moving
        };

    }
}
