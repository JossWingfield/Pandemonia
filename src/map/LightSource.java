package map;

import java.awt.Color;

import main.renderer.Colour;

public class LightSource {
	public enum Type { NORMAL, BLOOM_ONLY }
	
    public int x, y, radius;
    private Colour color;
    private float intensity = 1f;
    private Type type = Type.NORMAL;

    public LightSource(int x, int y, Colour color, int radius) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.radius = radius;
    }
    public LightSource(int x, int y, Colour color, int radius, Type type) {
        this(x, y, color, radius);
        this.type = type;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setColor(Colour color) {
        this.color = color;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
    public float getIntensity() { return intensity; }
    public void setIntensity(float i) { 
    	intensity = Math.max(0, i); 
    }
    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getRadius() { return radius; }
    public Colour getColor() { return color; }
}
