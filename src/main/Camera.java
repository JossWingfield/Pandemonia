package main;

import java.awt.Graphics2D;

import entity.Entity;

public class Camera {
    private GamePanel gp;

    public float x, y;        // camera position (world coordinates)
    public float zoom = 1.0f; // zoom level
    public float targetZoom = 1.0f;
    public float lerpSpeed = 0.1f;

    private Entity target;    // the entity camera follows

    public Camera(GamePanel gp) {
        this.gp = gp;
    }

    public void follow(Entity target) {
        this.target = target;
    }

    public void setZoom(float zoom) {
        this.targetZoom = zoom;
    }
    public int getXDiff() {
    	float viewWidthWorld  = gp.frameWidth  / zoom;

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
    public void update() {
        // Smooth zoom
        zoom += (targetZoom - zoom) * lerpSpeed;

        float effectiveLerpSpeed = lerpSpeed;

        if (target != null && zoom != 1.0f) {
            // World coords of target center
            float targetX = target.hitbox.x + target.hitbox.width / 2f;
            float targetY = target.hitbox.y + target.hitbox.height / 2f;

            // Adjust lerp speed by zoom so movement feels consistent
            effectiveLerpSpeed = lerpSpeed / zoom;

            x += (targetX - x) * effectiveLerpSpeed;
            y += (targetY - y) * effectiveLerpSpeed;
        } else if (zoom == 1.0f) {
            // Center camera on room
            x = gp.mapM.currentRoom.mapWidth * gp.tileSize / 2f;
            y = gp.mapM.currentRoom.mapHeight * gp.tileSize / 2f;
        }
    }

    public void applyTransform(Graphics2D g2) {
        g2.translate(gp.frameWidth / 2.0, gp.frameHeight / 2.0);
        g2.scale(zoom, zoom);
        g2.translate(-x - gp.frameWidth / 2.0, -y - gp.frameHeight / 2.0);
    }
}
