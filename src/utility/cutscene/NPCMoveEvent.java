package utility.cutscene;

import entity.buildings.Building;
import entity.npc.NPC;
import main.GamePanel;

public class NPCMoveEvent extends CutsceneEvent {

    private NPC npc;
    private Building targetBuilding;
    private float tolerance;
    private GamePanel gp;

    public NPCMoveEvent(NPC npc, Building building, GamePanel gp) {
        this.npc = npc;
        this.targetBuilding = building;
        this.gp = gp;
        this.tolerance = 2f; // small buffer to consider "arrived"
        

        // Set up initial path to building
        if (building != null) {
            float bx = building.hitbox.x + building.hitbox.width / 2f;
            float by = building.hitbox.y + building.hitbox.height / 2f;
            int goalCol = (int)(bx / gp.tileSize);
            int goalRow = (int)(by / gp.tileSize);
            npc.searchPath(goalCol, goalRow);
            npc.walking = true;
            npc.setAbleToUpdate(false);
        }
    }

    @Override
    public void update() {
        if (npc == null || targetBuilding == null) {
            finished = true;
            return;
        }

        // Move NPC along path
        npc.followPath();

        // Check if NPC has actually reached the building
        if (targetBuilding.hitbox.intersects(npc.hitbox)) {
            finished = true;
            npc.walking = false; // stop moving
            npc.setAbleToUpdate(true);
        }
    }
}
