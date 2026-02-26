package map.particles;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;

public class SeasoningRingParticle extends Particle {

    private float centerX;
    private float centerY;
    private float radius;
    private float angle; // degrees
    private float angularVelocity;

    public SeasoningRingParticle(
            GamePanel gp,
            int roomNum,
            float centerX,
            float centerY,
            float radius,
            float angle,
            float angularVelocity,
            Colour colour) {

        super(gp, roomNum, centerX, centerY, 0, 0,
                999f, // effectively infinite lifetime during minigame
                3f,
                colour);

        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.angle = angle;
        this.angularVelocity = angularVelocity;
    }

    public void setAngularVelocity(float vel) {
        this.angularVelocity = vel;
    }

    public float getAngle() {
        return angle;
    }

    @Override
    public void update(double dt) {

        angle += angularVelocity * dt;
        if (angle >= 360f) angle -= 360f;

        float rad = (float)Math.toRadians(angle);

        x = centerX + (float)Math.cos(rad) * radius;
        y = centerY + (float)Math.sin(rad) * radius;
    }

    @Override
    public void draw(Renderer renderer) {
        renderer.fillRect(x, y, size, size, colour);
    }

    @Override
    public void drawEmissive(Renderer renderer) {
        renderer.fillRect(x, y, size, size, colour);
    }
}
