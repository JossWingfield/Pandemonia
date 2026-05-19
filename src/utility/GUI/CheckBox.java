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
    private TextureRegion highlight1, highlight2, highlight3, highlight4;

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
		highlight1 = importImage("/UI/settings/Highlight1.png").toTextureRegion();
		highlight2 = importImage("/UI/settings/Highlight2.png").toTextureRegion();
		highlight3 = importImage("/UI/settings/Highlight3.png").toTextureRegion();
		highlight4 = importImage("/UI/settings/Highlight4.png").toTextureRegion();
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

        if(isHovering(boxX, boxY, size, size)) {
        	drawHover(renderer, boxX, boxY, size, size);
        	if(gp.mouseL.mouseButtonDown(0) && gp.gui.clickCooldown == 0) {
                toggler.run();
                gp.gui.clickCooldown = 0.33;
        	}
        }
    }
    private void drawHover(Renderer renderer, int x, int y, int w, int h) {

        int cornerSize = 16 * 3;
        int offset = 24;

        // Top-left  (was highlight2 -> now RIGHT side equivalent)
        renderer.draw(highlight1,
                x - cornerSize + offset,
                y - cornerSize + offset,
                cornerSize,
                cornerSize);

        // Top-right (was highlight1 -> now LEFT side equivalent)
        renderer.draw(highlight2,
                x + w - offset,
                y - cornerSize + offset,
                cornerSize,
                cornerSize);

        // Bottom-left (was highlight3 -> now RIGHT side equivalent)
        renderer.draw(highlight4,
                x - cornerSize + offset,
                y + h - offset,
                cornerSize,
                cornerSize);

        // Bottom-right (was highlight4 -> now LEFT side equivalent)
        renderer.draw(highlight3,
                x + w - offset,
                y + h - offset,
                cornerSize,
                cornerSize);
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
