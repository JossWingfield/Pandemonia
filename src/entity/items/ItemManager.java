package entity.items;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import main.GamePanel;
import main.renderer.Renderer;

public class ItemManager {

    GamePanel gp;

    public List<Item> items = new ArrayList<>(); 

    public ItemManager(GamePanel gp) {
        this.gp = gp;
    }
    public void setItems(List<Item> items) {
    	this.items = items;
    }
    //Draw the item hitboxes
    public void drawItemHitboxes(Renderer renderer) {
        for(Item i: items) { //Loops through the items on the current map
            if(i != null) {
                i.drawHitbox(renderer);
            }
        }
    }
    public void addItem(Item i) {
    	items.add(i);
    }
    public void removeItem(Item i) {
    	items.remove(i);
    }

    public Item findItem(float x, float y, float w, float h) {
		Rectangle2D.Float hitbox = new Rectangle2D.Float(x, y, w, h);
		for(Item i: items) {
			if(i != null) {
				if(i.hitbox.intersects(hitbox)) {
					return i;
				}	
			}
		}
		return null;
	}
    public List<Item> getItems() {
    	return items;
    }

    public void update(double dt) {

    }

    public void draw(Renderer renderer) {
        for(Item i: items) { //Loops through the items on the current map
            if(i != null) {
            	i.draw(renderer); //Draws the item
            }
        }
    }

}

