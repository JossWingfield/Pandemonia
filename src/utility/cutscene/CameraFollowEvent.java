package utility.cutscene;

import entity.Entity;
import entity.npc.NPC;
import main.GamePanel;

public class CameraFollowEvent extends CutsceneEvent {

	  private GamePanel gp;
	    private Entity target;
	    private float zoomTarget;

	    public CameraFollowEvent(GamePanel gp, Entity target, float zoomTarget) {
	        this.gp = gp;
	        this.target = target;
	        this.zoomTarget = zoomTarget;
	    }

	    @Override
	    public void update() {
	        // Camera will automatically lerp to target position and zoom
	        gp.camera.follow(target);
	        gp.camera.setZoom(zoomTarget);

	        // Consider the event finished once camera is roughly at target
	        float dx = (target.hitbox.x + target.hitbox.width / 2f) - gp.camera.x;
	        float dy = (target.hitbox.y + target.hitbox.height / 2f) - gp.camera.y;
	        float dz = Math.abs(gp.camera.zoom - zoomTarget);

	        if (Math.abs(dx) < 1 && Math.abs(dy) < 1 && dz < 0.01f) {
	            finished = true;
	        }
	    }
}