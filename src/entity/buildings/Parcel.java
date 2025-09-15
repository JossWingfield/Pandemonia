package entity.buildings;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import main.GamePanel;

public class Parcel extends Building {
	
	private List<Object> order;
	
	public Parcel(GamePanel gp, float xPos, float yPos, List<Object> order) {
		super(gp, xPos, yPos, 48, 48);
		this.order = order;
		
		isSolid = false;
		blueprint = false;
		drawWidth = 16*3;
		drawHeight = 16*3;
		importImages();
	}
	public Building clone() {
		Parcel building = new Parcel(gp, hitbox.x, hitbox.y, order);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Package(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + "," + this.order + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new BufferedImage[1][1][1];
		
		name = "Package";
		animations[0][0][0] = importImage("/decor/crate-box.png").getSubimage(64, 0, 16, 16);	
	}
	public void draw(Graphics2D g2) {
		 
	    g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);
		
	    if(gp.player.hitbox.intersects(hitbox)) {
	    	gp.customiser.addOrderToInventory(new ArrayList<>(order));
	    	gp.buildingM.removeBuilding(this);
	    }
	    
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
	    
	}
}
