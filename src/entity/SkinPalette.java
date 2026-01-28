package entity;

public class SkinPalette {
	
	//DEFAULT SELECTIONS
	public static final float[] BASE_SKIN = skinFromHex(
		    "#e6c9a9", // light
		    "#e7ac91", // mid
		    "#d9816b"  // dark
		);
	
    // 3 colors: light, mid, dark (RGB 0–1)
    public float[] from; // base sprite colors
    public float[] to;   // target colors
    private int index;

    public SkinPalette(int index) {
        this.index = index;
        from = BASE_SKIN;
        getSkins();
    }
    private void getSkins() {
        switch(index) {
        case 0:
        	to = BASE_SKIN;
        	break;
        case 1:
        	to = skinFromHex(
        		    "#a2786a", // light
        		    "#8c5d64", // mid
        		    "#714e61"  // dark
        		);;
        	break;
        case 2:
        	to = skinFromHex(
        		    "#c29979", // light
        		    "#a2786a", // mid
        		    "#8c5d64"  // dark
        		);;
        	break;
        case 3:
        	to = skinFromHex(
        		    "#e0bf9b", // light
        		    "#d8a881", // mid
        		    "#cc8f66"  // dark
        		);;
        	break;
        case 4:
        	to = skinFromHex(
        		    "#e0bea2", // light
        		    "#cca489", // mid
        		    "#cca189"  // dark
        		);;
        	break;
        case 5:
        	to = skinFromHex(
        		    "#d0aa8c", // light
        		    "#c08f6e", // mid
        		    "#b1714e"  // dark
        		);
        	break;
        case 6:
        	to = skinFromHex(
        		    "#cca68c", // light
        		    "#d09777", // mid
        		    "#c47b59"  // dark
        		);
        	break;
        }
    }
    public static float[] skinFromHex(String light, String mid, String dark) {
        float[] l = hexToRGB(light);
        float[] m = hexToRGB(mid);
        float[] d = hexToRGB(dark);

        return new float[] {
            l[0], l[1], l[2],
            m[0], m[1], m[2],
            d[0], d[1], d[2]
        };
    }
    public static float[] hexToRGB(String hex) {
        hex = hex.replace("#", "");

        if (hex.length() != 6) {
            throw new IllegalArgumentException("Hex color must be 6 characters");
        }

        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);

        return new float[] {
            r / 255f,
            g / 255f,
            b / 255f
        };
    }
    public int getIndex() {
		return index;
	}
}
