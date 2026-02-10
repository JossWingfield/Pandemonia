package entity;

import main.renderer.Colour;

public class PlayerAppearance {
    public SkinPalette skin;
    public HairPalette hair;
    public HairStyle hairStyle;
    public int hairVariant;

    public PlayerAppearance(SkinPalette skin, HairPalette hair, HairStyle hairStyle) {
        this.skin = skin;
        this.hair = hair;
        this.hairStyle = hairStyle;
    }

    public void setSkin(int num) {
        skin = new SkinPalette(num);
    }

    public void setHair(int num) {
        hair = new HairPalette(num);
    }
    public void setHairStyle(int num) {
    	hairStyle = new HairStyle(num);
	}
}
