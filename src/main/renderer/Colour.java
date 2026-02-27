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
    public Colour(String hex) {
        // Remove '#' if present
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }

        // Must be 6 (RGB) or 8 (RGBA) characters
        if (hex.length() != 6 && hex.length() != 8) {
            throw new IllegalArgumentException("Hex color must be 6 or 8 characters long: " + hex);
        }

        // Parse hex components
        int ri = Integer.parseInt(hex.substring(0, 2), 16);
        int gi = Integer.parseInt(hex.substring(2, 4), 16);
        int bi = Integer.parseInt(hex.substring(4, 6), 16);
        int ai = 255; // default alpha

        if (hex.length() == 8) {
            ai = Integer.parseInt(hex.substring(6, 8), 16);
        }

        // Normalize 0–255 to 0–1 floats
        this.r = ri / 255f;
        this.g = gi / 255f;
        this.b = bi / 255f;
        this.a = ai / 255f;
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
    public static final Colour BASE_COLOUR = new Colour(0.2f, 0.235f, 0.227f, 1f);
}
