package utility.GUI;

import java.util.function.BooleanSupplier;

import main.GamePanel;
import main.renderer.AssetPool;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.Texture;
import main.renderer.TextureRegion;

public class CheckBox {
	
	GamePanel gp;
	
    public int x, y;          // position
    private int size;           // size of the checkbox
    private String label;       // text to display
    private BooleanSupplier getter; // function to get current value
    private Runnable toggler;       // function to toggle value
    private Colour titleColour1;
    private TextureRegion uncheckedBox, checkedBox;    

    public CheckBox(GamePanel gp, int x, int y, int size, String label, BooleanSupplier getter, Runnable toggler) {
        this.gp = gp;
    	this.x = x;
        this.y = y;
        this.size = size;
        this.label = label;
        this.getter = getter;
        this.toggler = toggler;
		titleColour1 = Colour.BLACK;
		uncheckedBox = importImage("/UI/settings/CheckBox.png").getSubimage(0, 0, 9, 9);
		checkedBox = importImage("/UI/settings/CheckBox.png").getSubimage(9, 0, 9, 9);
    }
	protected Texture importImage(String filePath) {
		Texture texture = AssetPool.getTexture(filePath);
	    return texture;
	}
    // Call this every frame
    public void draw(Renderer renderer) {
        renderer.setColour(titleColour1);
        renderer.drawString(label, x, y);

        int boxX = x + 220; // offset for checkbox
        int boxY = y - 20;

        if (getter.getAsBoolean()) {
            renderer.draw(checkedBox, boxX, boxY, size, size);
        } else {
            renderer.draw(uncheckedBox, boxX, boxY, size, size);
        }

        if (isHovering(boxX, boxY, size, size) && gp.mouseL.mouseButtonDown(0) && gp.gui.clickCooldown == 0) {
            toggler.run();
            gp.gui.clickCooldown = 0.33;
        }
    }

    // Move checkbox down (used for scrolling)
    public void move(int dy) {
        this.y += dy;
    }
    private boolean isHovering(int x, int y, int width, int height) {
        return gp.mouseL.getWorldX() >= x && gp.mouseL.getWorldX() <= x + width &&
        	gp.mouseL.getWorldY() >= y && gp.mouseL.getWorldY() <= y + height;
    }
}
