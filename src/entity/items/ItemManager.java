package entity.items;

import main.GamePanel;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import entity.buildings.Building;
import entity.npc.NPC;

import static utility.CollisionMethods.entityCheck;

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
    public void drawItemHitboxes(Graphics2D g, float xDiff, float yDiff) {
        for(Item i: items) { //Loops through the items on the current map
            if(i != null) {
                i.drawHitbox(g, xDiff, yDiff);
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

    public void update() {

    }

    public void draw(Graphics2D g, int xDiff, int yDiff) {
        for(Item i: items) { //Loops through the items on the current map
            if(i != null) {
            	i.draw(g, xDiff, yDiff); //Draws the item
            }
        }
    }

}

