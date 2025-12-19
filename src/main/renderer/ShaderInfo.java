package main.renderer;

import org.joml.Vector2f;


public class ShaderInfo {

	private Vector2f[] points;
	private float[] strengths;
	private int pointCount;
	
	public ShaderInfo() {
		points = new Vector2f[10];
		strengths = new float[10];
		for (int i = 0; i < 10; i++) {
			points[i] = new Vector2f(-1f, -1f);
			strengths[i] = 0f;
		}
	}
	
	public Vector2f[] getPoints() {
		return points;
	}
	
	public void setPoints(Vector2f[] newPoints) {
		points = newPoints;
	}
	
	public int getPointCount() {
		return pointCount;
	}
	public void setPointCount(int pointCount) {
		this.pointCount = pointCount;
	}
	public void setStrengths(float[] strengths) {
		this.strengths = strengths;
	}
	public float[] getStrengths() {
		return strengths;
	}
	
}
