package utility.cutscene;

import entity.npc.NPC;

public class NPCMoveEvent extends CutsceneEvent {

    private NPC npc;
    private float targetX, targetY;
    private float speed;

    public NPCMoveEvent(NPC npc, float targetX, float targetY, float speed) {
        this.npc = npc;
        this.targetX = targetX;
        this.targetY = targetY;
        this.speed = speed;
    }

    @Override
    public void update() {
        float dx = targetX - npc.hitbox.x;
        float dy = targetY - npc.hitbox.y;

        npc.hitbox.x += dx * speed;
        npc.hitbox.y += dy * speed;

        if (Math.abs(dx) < 1 && Math.abs(dy) < 1) {
            npc.hitbox.x = targetX;
            npc.hitbox.y = targetY;
            finished = true;
        }
    }
}
