package main.renderer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class GLSLCamera {
    
    private Matrix4f projectionMatrix, viewMatrix, inverseProjection, inverseView;
    public Vector2f position;
    
    public Vector4f clearColor = new Vector4f(1,1,1,1);
    private float zoom = 1.0f;
    private float width, height;
    
    public GLSLCamera(Vector2f position, float width, float height) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.inverseProjection = new Matrix4f();
        this.inverseView = new Matrix4f();
        adjustProjection();
    }
    
    public void adjustProjection() {
        projectionMatrix.identity();
        // top-left is (0,0), bottom-right is (width, height)
        projectionMatrix.ortho(
        	    0f, width * zoom,    // left to right
        	    height * zoom, 0f,   // top to bottom
        	    -1f, 1f
        	);
        projectionMatrix.invert(inverseProjection);
    }
    
    public Matrix4f getViewMatrix() {
        viewMatrix.identity();
        viewMatrix.translate(-position.x, -position.y, 0.0f);
        return viewMatrix;
    }
    public void followEntity(float targetX, float targetY) {
        float viewW = width * zoom;
        float viewH = height * zoom;

        // Convert from center → top-left
        this.position.x = targetX - viewW / 2f;
        this.position.y = targetY - viewH / 2f;
    }
    // in GLSLCamera
    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        adjustProjection();
    }
    public void lerpTo(Vector2f target, float factor) {
        this.position.lerp(target, factor);
    }
    public void followEntityLerp(float targetX, float targetY, float factor) {
        float viewW = width * zoom;
        float viewH = height * zoom;

        Vector2f desired = new Vector2f(
            targetX - viewW / 2f,
            targetY - viewH / 2f
        );

        this.position.lerp(desired, factor);
    }
    public void reset() {
        this.zoom = 1.0f;
        adjustProjection();
        this.position.set(0, 0);
    }
    public float getWidth() { return this.width; }
    public float getHeight() { return this.height; }
    
    public Matrix4f getProjectionMatrix() { return projectionMatrix; }
    public Matrix4f getInverseProjectionMatrix() { return inverseProjection; }
    public Matrix4f getInverseViewMatrix() { return inverseView; }
    
    public void setZoom(float zoom) { this.zoom = zoom; adjustProjection(); }
    public void addZoom(float value) { this.zoom += value; adjustProjection(); }
    public float getZoom() {
		return zoom;
	}
    public static Vector2f worldToScreen(Vector2f worldPos, Matrix4f view, Matrix4f proj, float screenWidth, float screenHeight) {
        Vector4f clipSpace = new Vector4f(worldPos.x, worldPos.y, 0, 1);
        proj.mul(view, new Matrix4f()).transform(clipSpace);

        clipSpace.x /= clipSpace.w;
        clipSpace.y /= clipSpace.w;

        float screenX = (clipSpace.x * 0.5f + 0.5f) * screenWidth;
        float screenY = screenHeight - ((clipSpace.y * 0.5f + 0.5f) * screenHeight); // flip Y
        return new Vector2f(screenX, screenY);
    }
    public Vector2f getProjectionSize() {
        return new Vector2f(width, height);
    }
    
    public void setPosition(float x, float y) { this.position.set(x, y); }
    public void addPosition(float dx, float dy) { this.position.add(dx, dy); }
}
