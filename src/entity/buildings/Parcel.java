package entity.buildings;

import java.util.ArrayList;
import java.util.List;

import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;
import main.renderer.TextureRegion;
import utility.ShopCatalogue;

public class Parcel extends Building {
	
	private List<Object> order;
	
	private int crateNum = -1;
	
	public Parcel(GamePanel gp, float xPos, float yPos, List<Object> order) {
		super(gp, xPos, yPos, 48, 48);
		this.order = order;
		
		isSolid = false;
		
		drawWidth = 16*3;
		drawHeight = 16*3;
		canBePlaced = false;
		importImages();
	}
	public Parcel(GamePanel gp, float xPos, float yPos, int crateNum) {
		super(gp, xPos, yPos, 48, 48);
		this.crateNum = crateNum;
		
		isSolid = false;
		
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
		animations = new TextureRegion[1][1][1];
		
		name = "Package";
		animations[0][0][0] = importImage("/decor/crate-box.png").getSubimage(64, 0, 16, 16);	
		if(crateNum != -1) {
			animations[0][0][0] = importImage("/decor/crate-box.png").getSubimage(0, 0, 16, 16);	
		}
	}
	public void inputUpdate(double dt) {
		super.inputUpdate(dt);
	    if(gp.player.hitbox.intersects(hitbox)) {
	    	if(crateNum == -1) {
	    		gp.world.customiser.addOrderToInventory(new ArrayList<>(order));
	    	} else {
	    		//UNLOCK CATALOGUE
	    		gp.world.catalogue.unlockById(crateNum);
	    		ShopCatalogue catalogue = gp.world.catalogue.getCatalogueByID(crateNum);
	    		String text = "Added " + catalogue.getName() + " Catalogue!";
                gp.gui.addMessage(text, Colour.YELLOW);

	    	}
	    	gp.world.buildingM.removeBuilding(this);
	    }
	}
	public void draw(Renderer renderer) {
	    renderer.draw(animations[0][0][0], (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);
	    
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int) hitbox.x - xDrawOffset , (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		}
	    
	}
}
