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

	    public void update(double dt) {
	        gp.camera.setTarget(target);
	        // --- Smooth zoom (optional but correct) ---
	        float currentZoom = gp.camera.getZoom();
	        float zoomLerp = 0.08f;
	        gp.camera.setZoom(
	            currentZoom + (zoomTarget - currentZoom) * zoomLerp
	        );

	        // --- Target center ---
	        float targetCenterX = target.hitbox.x + target.hitbox.width * 0.5f;
	        float targetCenterY = target.hitbox.y + target.hitbox.height * 0.5f;

	        // --- Follow ---
	        gp.camera.followEntityLerp(targetCenterX, targetCenterY, 0.12f);

	        // --- Compute desired camera position (top-left) ---
	        float viewW = gp.camera.getWidth() / gp.camera.getZoom();
	        float viewH = gp.camera.getHeight() / gp.camera.getZoom();

	        float desiredX = targetCenterX - viewW * 0.5f;
	        float desiredY = targetCenterY - viewH * 0.5f;

	        // --- Compare camera to desired ---
	        float dx = desiredX - gp.camera.position.x;
	        float dy = desiredY - gp.camera.position.y;
	        float dz = Math.abs(gp.camera.getZoom() - zoomTarget);

	        if (Math.abs(dx) < 0.5f && Math.abs(dy) < 0.5f && dz < 0.01f) {
	            gp.camera.setPosition(desiredX, desiredY);
	            gp.camera.setZoom(zoomTarget);
	            finished = true;
	        }
	    }
}