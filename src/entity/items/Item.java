package entity.items;

import java.awt.geom.Rectangle2D;

import entity.Entity;
import main.GamePanel;
import main.renderer.Renderer;

public abstract class Item extends Entity {

    GamePanel gp;
    
    private int counter = -1;
    protected int drawWidth, drawHeight, yDrawOffset, xDrawOffset; //Variables to draw the item correctly
    public boolean weapon = false;
    protected String name;
    protected String outputName = "";
    protected String description = "";
    public boolean inRecipe;

    public Item(GamePanel gp, float xPos, float yPos) {
        super(gp, xPos, yPos, 48, 48);
        this.gp = gp;
        this.drawWidth = (int)hitbox.width;
        this.drawHeight = (int)hitbox.height;
        yDrawOffset = 0;
        xDrawOffset = 0;
    }
    
    public Item(GamePanel gp) {
        super();
        this.gp = gp;
    }
    
    //@Override
    public Item clone() {
        try {
            return (Item) super.clone(); // Shallow copy
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Item clone not supported", e);
        }
    }
    public String getName() {
    	return name;
    }
    public void refreshImages() {}
    public void createNewHitbox(float xPos, float yPos, float width, float height) {
        hitbox = new Rectangle2D.Float(xPos, yPos, width, height);
    }

    public void update(double dt) {}

    public void draw(Renderer renderer) {
        renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
    }
    public void drawOverlay(Renderer renderer) {
    }
    public String getDescription() {
    	return description;
    }
	public String getOutputName() {
		return outputName;
	}

}
