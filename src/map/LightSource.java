package map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import entity.Entity;
import main.GamePanel;

public class LightSource {
    public int x, y, radius;
    private Color color;
    private float intensity = 1f;

    public LightSource(int x, int y, Color color, int radius) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.radius = radius;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
    public float getIntensity() { return intensity; }
    public void setIntensity(float i) { 
    	intensity = Math.max(0, i); 
    }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getRadius() { return radius; }
    public Color getColor() { return color; }
}
