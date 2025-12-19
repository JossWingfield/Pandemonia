package main.renderer;

public class Glyph {
    public TextureRegion region;
    public float xOffset;
    public float yOffset;
    public float xAdvance;

    public Glyph(TextureRegion region, float xOffset, float yOffset, float xAdvance) {
        this.region = region;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.xAdvance = xAdvance;
    }
}