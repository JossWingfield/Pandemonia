package main.renderer;

import java.util.Map;

public class BitmapFont {
    private Texture textureAtlas;
    private Map<Character, Glyph> glyphs;
    private int lineHeight;

    public BitmapFont(Texture atlas, Map<Character, Glyph> glyphs, int lineHeight) {
        this.textureAtlas = atlas;
        this.glyphs = glyphs;
        this.lineHeight = lineHeight;
    }

    public Glyph getGlyph(char c) {
        return glyphs.get(c);
    }

    public float getAdvance(char c) {
    	Glyph reg = glyphs.get(c);
        return reg == null ? 0 : reg.xAdvance;
    }

    public int getLineHeight() {
        return lineHeight;
    }
    public float getTextWidth(String text) {
        float width = 0;

        for (char c : text.toCharArray()) {
        	Glyph glyph = glyphs.get(c);
            if (glyph != null) {
                width += glyph.xAdvance;
            }
        }

        return width;
    }

    public Texture getTexture() {
        return textureAtlas;
    }
}