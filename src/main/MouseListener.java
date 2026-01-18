package main;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

import main.renderer.GLSLCamera;

public class MouseListener {
	
	GamePanel gp;
    private double scrollX, scrollY;
    private double xPos, yPos, lastY, lastX, worldX, worldY, lastWorldX, lastWorldY;
    private boolean mouseButtonPressed[] = new boolean[9];
    private boolean isDragging;

    private int mouseButtonDown = 0;

    private Vector2f gameViewportPos = new Vector2f();
    private Vector2f gameViewportSize = new Vector2f();

    public MouseListener(GamePanel gp) {
    	this.gp = gp;
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
        this.lastWorldX = 0.0;
        this.lastWorldY = 0.0;
    }

    public void endFrame() {
        scrollY = 0.0;
        scrollX = 0.0;
        lastX = xPos;
        lastY = yPos;
        lastWorldX = worldX;
        lastWorldY = worldY;
    }
    
    public void clear() {
    	scrollX = 0.0;
    	scrollY = 0.0;
    	xPos = 0.0;
    	yPos = 0.0;
    	lastX = 0.0;
    	lastY = 0.0;
    	lastWorldX = 0.0;
      	lastWorldY = 0.0;
    	mouseButtonDown = 0;
    	isDragging = false;
    	Arrays.fill(mouseButtonPressed, false);
    }

    public void mousePosCallback(long window, double xpos, double ypos) {
        if(mouseButtonDown > 0) {
        	isDragging = true;
        	//System.out.println("Starting dragging");
        }

        lastX = xPos;
        lastY = yPos;
        lastWorldX = worldX;
        lastWorldY = worldY;
        xPos = xpos;
        yPos = ypos;
        worldX = getWorldX();
        worldY = getWorldY();
    }

    public  void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            mouseButtonDown++;

            if (button < mouseButtonPressed.length) {
                mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            mouseButtonDown--;

            if (button < mouseButtonPressed.length) {
                mouseButtonPressed[button] = false;
                isDragging = false;
                //System.out.println("Released");
            }
        }
    }

    public  void mouseScrollCallback(long window, double xOffset, double yOffset) {
        scrollX = xOffset;
        scrollY = yOffset;
    }

    public  float getX() {
        return (float)xPos;
    }

    public  float getY() {
        return (float)yPos;
    }

    public  float getWorldDx() {
        return (float)(lastWorldX - worldX);
    }

    public  float getWorldDy() {
        return (float)(lastWorldY - worldY);
    }

    public  float getScrollX() { return (float)scrollX; }

    public  float getScrollY() {
        return (float)scrollY;
    }

    public  boolean isDragging() { return isDragging; }

    public  boolean mouseButtonDown(int button) {
        if (button < mouseButtonPressed.length) {
            return mouseButtonPressed[button];
        } else {
            return false;
        }
    }

    public float getWorldX() {
        return getMouseScreenPosition().x;
    }

    public float getWorldY() {
        return getMouseScreenPosition().y;
    }

    public  Vector2f getWorld() {
        float currentX = getX() - gameViewportPos.x;
        currentX = (2.0f * (currentX / gameViewportSize.x)) - 1.0f;
        float currentY = (getY() - gameViewportPos.y);
        currentY = (2.0f * (1.0f - (currentY / gameViewportSize.y))) - 1.0f;
        GLSLCamera camera = gp.camera;
        Vector4f tmp = new Vector4f(currentX, currentY, 0, 1);
        Matrix4f inverseView = new Matrix4f(camera.getInverseViewMatrix());
        Matrix4f inverseProjection = new Matrix4f(camera.getInverseProjectionMatrix());
        tmp.mul(inverseView.mul(inverseProjection));
        return new Vector2f(tmp.x, tmp.y);
    }
    public  float getScreenX() {
        return getScreen().x;
    }
    public  float getScreenY() {
        return getScreen().y;
    }
    public  float getDx() {
    	return (float)(-xPos + lastX);
    }
    public  float getDy() {
    	return (float)(-yPos + lastY);
    }
    public Vector2f getMouseScreenPosition() {
        float sx = (float) (xPos - gameViewportPos.x);
        float sy = (float) (yPos - gameViewportPos.y);
        return new Vector2f(sx, sy);
    }
    public  Vector2f getScreen() {
        float currentX = getX() - gameViewportPos.x;
        currentX = (currentX / gameViewportSize.x) * gp.frameWidth;
        float currentY = getY() - gameViewportPos.y;
        currentY = gp.frameHeight - ((currentY / gameViewportSize.y) * gp.frameHeight);
        return new Vector2f(currentX, currentY);
    }

    public void setGameViewportPos(Vector2f gameViewportPos) { this.gameViewportPos.set(gameViewportPos); }

    public void setGameViewportSize(Vector2f gameViewportSize) {
        gameViewportSize.set(gameViewportSize);
    }
}