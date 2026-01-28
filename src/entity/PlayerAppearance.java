package entity;

import main.renderer.Colour;

public class PlayerAppearance {
    public SkinPalette skin;
    public int hairVariant;
    // later: eyeColor, clothingColor, etc.

    public PlayerAppearance(SkinPalette skin) {
        this.skin = skin;
    }
    
    public void setSkin(int num) {
    	skin = new SkinPalette(num);
    }
}
