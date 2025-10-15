package main;

import java.awt.Graphics2D;

import entity.Entity;

public class Camera {
    private GamePanel gp;

    public float x, y;        // camera center in world coordinates
    public float zoom = 1.0f; // current zoom
    public float targetZoom = 1.0f;
    public float lerpSpeed = 0.1f;

    private Entity target;    // the entity camera follows

    public Camera(GamePanel gp) {
        this.gp = gp;
    }

    public void follow(Entity target) {
        this.target = target;
        if(target != null) {
            // instantly center on target
            x = target.hitbox.x + target.hitbox.width / 2f;
            y = target.hitbox.y + target.hitbox.height / 2f;
        }
    }

    public void setZoom(float zoom) {
        this.targetZoom = zoom;
    }
    public void resetToDefaultZoom() {
    	targetZoom = 1.0f;
    }

    public void update() {
        // Smooth zoom
        zoom += (targetZoom - zoom) * lerpSpeed;
        
        float epsilon = 0.01f; // threshold
        if (Math.abs(zoom - targetZoom) < epsilon) {
            zoom = targetZoom;
        }

        if (target != null && zoom != 1.0f) {
            float targetX = (target.hitbox.x + target.hitbox.width / 2f);
            float targetY = (target.hitbox.y + target.hitbox.height / 2f);

            // lerp speed in world units, scaled by zoom so it feels consistent
            float worldLerp = lerpSpeed;

            x += (targetX - x) * worldLerp;
            y += (targetY - y) * worldLerp;
        } else {
            // optional: center on room if no target
            x = gp.mapM.currentRoom.mapWidth * gp.tileSize / 2f;
            y = gp.mapM.currentRoom.mapHeight * gp.tileSize / 2f;
        }
    }

    public int getXDiff() {
    	float viewWidthWorld  = gp.frameWidth / zoom;

    	// Top-left of camera view in world coords
    	float viewLeftWorld = x - viewWidthWorld * 0.5f;

    	// These are *positive* offsets from world to screen
    	int xDiff = Math.round(viewLeftWorld);
    	return xDiff;
    }

    public int getYDiff() {
    	float viewHeightWorld = gp.frameHeight / zoom;

    	// Top-left of camera view in world coords
    	float viewTopWorld  = y - viewHeightWorld * 0.5f;

    	// These are *positive* offsets from world to screen
    	int yDiff = Math.round(viewTopWorld);
    	return yDiff;
    }

}
