package entity;

import main.renderer.Colour;

public class PlayerAppearance {
    public SkinPalette skin;
    public HairPalette hair;
    public int hairVariant;

    public PlayerAppearance(SkinPalette skin, HairPalette hair) {
        this.skin = skin;
        this.hair = hair;
    }

    public void setSkin(int num) {
        skin = new SkinPalette(num);
    }

    public void setHair(int num) {
        hair = new HairPalette(num);
    }
}
