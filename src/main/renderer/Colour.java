package main.renderer;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Colour {

    public float r, g, b, a;

    public Colour(float r, float g, float b) {
        // If any value is >1, assume 0-255 input and normalize
        if (r > 1f || g > 1f || b > 1f) {
            this.r = r / 255f;
            this.g = g / 255f;
            this.b = b / 255f;
        } else {
            this.r = r;
            this.g = g;
            this.b = b;
        }
        a = 1.0f;
    }

    // From 0–255
    public static Colour from255(int r, int g, int b) {
        return new Colour(r / 255.0f, g / 255.0f, b / 255.0f);
    }

    public Colour(float r, float g, float b, float a) {
        // If any value is >1, assume 0-255 input and normalize
        if (r > 1f || g > 1f || b > 1f || a > 1f) {
            this.r = r / 255f;
            this.g = g / 255f;
            this.b = b / 255f;
            this.a = a / 255f;
        } else {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
        }
    }
    private static float normalize(float v) {
        // If value is > 1, treat as 0–255 and convert
        if (v > 1.0f) {
            return v / 255.0f;
        }
        // Already in 0–1 range
        return v;
    }

    public Vector4f toVec4() {
        return new Vector4f(r, g, b, a);
    }
    public Vector3f toVec3() {
        return new Vector3f(r, g, b);
    }

    // -----------------------------
    // Predefined colors
    // -----------------------------
    public static final Colour WHITE  = new Colour(1f, 1f, 1f, 1f);
    public static final Colour BLACK  = new Colour(0f, 0f, 0f, 1f);
    public static final Colour RED    = new Colour(1f, 0f, 0f, 1f);
    public static final Colour GREEN  = new Colour(0f, 1f, 0f, 1f);
    public static final Colour BLUE   = new Colour(0f, 0f, 1f, 1f);
    public static final Colour YELLOW = new Colour(1f, 1f, 0f, 1f);
    public static final Colour CYAN   = new Colour(0f, 1f, 1f, 1f);
    public static final Colour MAGENTA= new Colour(1f, 0f, 1f, 1f);
    public static final Colour ORANGE = new Colour(1f, 0.784f, 0f, 1f);
}
