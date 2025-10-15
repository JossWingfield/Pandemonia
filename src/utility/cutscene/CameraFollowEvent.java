package utility.cutscene;

import entity.npc.NPC;
import main.GamePanel;

public class CameraFollowEvent extends CutsceneEvent {

    private GamePanel gp;
    private NPC target;
    private float speed; // pixels per frame
    private float zoomTarget;
    private boolean zooming = false;

    public CameraFollowEvent(GamePanel gp, NPC target, float speed, float zoomTarget) {
        this.gp = gp;
        this.target = target;
        this.speed = speed;
        this.zoomTarget = zoomTarget;
        this.zooming = zoomTarget != gp.camera.zoom;
    }

    @Override
    public void update() {
        // Smooth camera pan
        float dx = target.hitbox.x - gp.camera.x;
        float dy = target.hitbox.y - gp.camera.y;
        gp.camera.x += dx * speed;
        gp.camera.y += dy * speed;

        // Optional smooth zoom
        if (zooming) {
            float dz = zoomTarget - gp.camera.zoom;
            gp.camera.zoom += dz * speed;
            if (Math.abs(dz) < 0.01f) {
            	gp.camera.zoom = zoomTarget;
                zooming = false;
            }
        }

        // Consider the event finished once camera is roughly at target
        if (Math.abs(dx) < 1 && Math.abs(dy) < 1 && !zooming) {
            finished = true;
        }
    }
}
