package entity;

public class HairStyle {

    private int index;

    public HairStyle(int index) {
        this.index = index;
        getHairs();
    }

    private void getHairs() {
        
    }

    public static float[] hairFromHex(String light, String dark) {
        float[] l = SkinPalette.hexToRGB(light);
        float[] d = SkinPalette.hexToRGB(dark);

        return new float[] {
            l[0], l[1], l[2],
            d[0], d[1], d[2]
        };
    }

    public int getIndex() {
        return index;
    }
}
