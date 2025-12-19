package main.renderer;

public class TextureRegion {
    public Texture texture;
    public float u0, v0, u1, v1;
    private int pixelWidth, pixelHeight;

    public TextureRegion(Texture texture, float u0, float v0, float u1, float v1) {
        this.texture = texture;
        this.u0 = u0;
        this.v0 = v0;
        this.u1 = u1;
        this.v1 = v1;
    }
    
    /** Width of this region in pixels */
    public int getPixelWidth() {
        // UV fraction * full texture pixel width
        return Math.round((u1 - u0) * texture.getWidth());
    }
    public void setPixelSize(int w, int h) {
        this.pixelWidth = w;
        this.pixelHeight = h;
    }

    /** Height of this region in pixels */
    public int getPixelHeight() {
        // UV fraction * full texture pixel height
        return Math.round((v1 - v0) * texture.getHeight());
    }
    public float getUWidth() { return u1 - u0; }
    public float getVHeight() { return v1 - v0; }
}
