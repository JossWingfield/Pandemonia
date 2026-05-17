package entity;

public class PlayerAppearance {
    public SkinPalette skin;
    public HairPalette hair;
    public HairStyle hairStyle;
    public Hat hat;
    public Costume costume;
    public int hairVariant;

    public PlayerAppearance(SkinPalette skin, HairPalette hair, HairStyle hairStyle, Hat hat, Costume costume) {
        this.skin = skin;
        this.hair = hair;
        this.hairStyle = hairStyle;
        this.hat = hat;
        this.costume = costume;
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
    public void setHat(int num) {
        hat = new Hat(num);
    }
    public void setCostume(int num) {
        costume = new Costume(num);
    }
}
