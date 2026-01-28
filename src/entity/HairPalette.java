package entity;

public class HairPalette {

    // THESE must match the base hair colours in the sprite sheet
	public static final float[] BASE_HAIR = hairFromHex("#dcc0c2", "#ebdadb");


    public float[] from;
    public float[] to;
    private int index;

    public HairPalette(int index) {
        this.index = index;
        from = BASE_HAIR;
        getHairs();
    }

    private void getHairs() {
        switch (index) {
        	case 0:
        		to = hairFromHex("#333c3a", "#333c3a");
            break;
            case 1:
                to = hairFromHex("#573e56", "#714e61");
                break;
            case 2:
                to = hairFromHex("#714e61", "#8c5d64");
                break;
            case 3:
                to = hairFromHex("#8c5d64", "#a2786a");
                break;
            case 4:
                to = hairFromHex("#c29979", "#d1b28e");
                break;
            case 5:
                to = hairFromHex("#f2d16b", "#e0b84f");
                break;
            case 6:
                to = hairFromHex("#97543c", "#bb724b");
                break;
            case 7:
                to = hairFromHex("#ca5d67", "#d9816b");
                break;
            case 8:
                to = hairFromHex("#acacaa", "#ccccca");
                break;
            case 9:
                to = hairFromHex("#8a8a89", "#acacaa");
                break;
        }
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
