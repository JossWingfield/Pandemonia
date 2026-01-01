package utility.cutscene;

import org.joml.Vector2f;

import main.GamePanel;

public class CameraPanEvent extends CutsceneEvent {

    private final GamePanel gp;
    private final Vector2f startPos;
    private final Vector2f targetPos;
    private final float speed;
    private float dx, dy;

    private boolean initialized = false;

    /**
     * @param dx world-space X offset (pixels)
     * @param dy world-space Y offset (pixels)
     * @param speed movement speed (pixels per frame-ish, try 6–12)
     */
    public CameraPanEvent(GamePanel gp, float dx, float dy, float speed) {
        this.gp = gp;
        this.dx = dx;
        this.dy = dy;
        this.startPos = new Vector2f();
        this.targetPos = new Vector2f();
        this.speed = speed;
    }

    @Override
    public void update(double dt) {
        if (!initialized) {
            // Stop following entities during pan
            gp.camera.setTarget(null);

            startPos.set(gp.camera.position);
            targetPos.set(
                startPos.x + dx,
                startPos.y + dy
            );

            initialized = true;
        }

        Vector2f pos = gp.camera.position;

        float dist = pos.distance(targetPos);
        if (dist < 1f) {
            pos.set(targetPos);
            finished = true;
            return;
        }

        Vector2f dir = new Vector2f(targetPos).sub(pos).normalize();
        pos.add(dir.mul(speed));
    }
}