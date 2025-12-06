package entity.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import main.GamePanel;
import utility.ShopCatalogue;

public class Parcel extends Building {
	
	private List<Object> order;
	
	private int crateNum = -1;
	
	public Parcel(GamePanel gp, float xPos, float yPos, List<Object> order) {
		super(gp, xPos, yPos, 48, 48);
		this.order = order;
		
		isSolid = false;
		blueprint = false;
		drawWidth = 16*3;
		drawHeight = 16*3;
		canBePlaced = false;
		importImages();
	}
	public Parcel(GamePanel gp, float xPos, float yPos, int crateNum) {
		super(gp, xPos, yPos, 48, 48);
		this.crateNum = crateNum;
		
		isSolid = false;
		blueprint = false;
		drawWidth = 16*3;
		drawHeight = 16*3;
		canBePlaced = false;
		importImages();
	}
	public Building clone() {
		if(crateNum == -1) {
			return new Parcel(gp, hitbox.x, hitbox.y, order);
		} else {
			return new Parcel(gp, hitbox.x, hitbox.y, crateNum);
		}
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Package(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + "," + this.order + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new BufferedImage[1][1][1];
		
		name = "Package";
		animations[0][0][0] = importImage("/decor/crate-box.png").getSubimage(64, 0, 16, 16);	
		if(crateNum != -1) {
			animations[0][0][0] = importImage("/decor/crate-box.png").getSubimage(0, 0, 16, 16);	
		}
	}
	public void update(double dt) {
		super.update(dt);
	    if(gp.player.hitbox.intersects(hitbox)) {
	    	if(crateNum == -1) {
	    		gp.customiser.addOrderToInventory(new ArrayList<>(order));
	    	} else {
	    		//UNLOCK CATALOGUE
	    		gp.catalogue.unlockById(crateNum);
	    		ShopCatalogue catalogue = gp.catalogue.getCatalogueByID(crateNum);
	    		String text = "Added " + catalogue.getName() + " Catalogue!";
                gp.gui.addMessage(text, Color.YELLOW);

	    	}
	    	gp.buildingM.removeBuilding(this);
	    }
	}
	public void draw(Graphics2D g2, int xDiff, int yDiff) {
	    g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, drawWidth, drawHeight, null);
	    
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - xDiff, (int) (hitbox.y - yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
	    
	}
}
