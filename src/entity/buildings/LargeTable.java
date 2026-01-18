package entity.buildings;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import entity.npc.Customer;
import main.GamePanel;
import main.renderer.TextureRegion;
import utility.RoomHelperMethods;

public class LargeTable extends Building {
	
	private Chair chair1;
	private Chair chair2;
	private Chair chair3;
	private Chair chair4;
	
	private boolean firstUpdate = true;
	
	public LargeTable(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos, 48, 48*2);
		
		importImages();
		isSolid = true;
		drawWidth = 32*3;
    	drawHeight = 48*3;
    	hitbox.width = 32*3 - 16;
    	hitbox.height = 32*3 - 16;
		isSecondLayer = true;
		xDrawOffset = 8;
		yDrawOffset = 8;
		//isBottomLayer = true;
		isKitchenBuilding = true;
		mustBePlacedOnFloor = true;
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*3, hitbox.width-3*3, hitbox.height-3*4);
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*3, hitbox.width-3*3, hitbox.height-3*4);
	}
	public Building clone() {
		LargeTable building = new LargeTable(gp, hitbox.x, hitbox.y);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new LargeTable(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	public void refreshImages() {
		animations[0][0][0] = gp.mapM.getRooms()[roomNum].getTableSkin().getImage1();
	}
	private void importImages() {
		animations = new TextureRegion[1][1][1];
		
		name = "Large Table";
    	animations[0][0][0] = importImage("/decor/table.png").getSubimage(160, 64, 32, 48);

    	chair1 = new Chair(gp, hitbox.x -48, hitbox.y + hitbox.height/2 - 24 - 24, 2);
		chair2 = new Chair(gp, hitbox.x -48, hitbox.y + hitbox.height/2 - 24 + 24, 2);
		chair3 = new Chair(gp, hitbox.x + hitbox.width + 8 + 20, hitbox.y + hitbox.height/2 - 24 - 24, 1);
		chair4 = new Chair(gp, hitbox.x + hitbox.width + 8 + 20, hitbox.y + hitbox.height/2 - 24 + 24, 1);
		chair1.setGroupChair(this);
		chair2.setGroupChair(this);
		chair3.setGroupChair(this);
		chair4.setGroupChair(this);
	}
	public List<Chair> getChairs() {
		List<Chair> chairs = new ArrayList<>();
		chairs.add(chair1);
		chairs.add(chair2);
		chairs.add(chair3);
		chairs.add(chair4);
		return chairs;
	}
	public List<Customer> getSeatedCustomers() {
		List<Customer> customers = new ArrayList<>();
		if(chair1.currentCustomer != null) {
			customers.add(chair1.currentCustomer);
		}
		if(chair2.currentCustomer != null) {
			customers.add(chair2.currentCustomer);
		}
		if(chair3.currentCustomer != null) {
			customers.add(chair3.currentCustomer);
		}
		if(chair4.currentCustomer != null) {
			customers.add(chair4.currentCustomer);
		}
		return customers;
	}
	public void update(double dt) {
		super.update(dt);
		if(firstUpdate) {
			firstUpdate = false;
	    	if(roomNum == RoomHelperMethods.MAIN) {
	    		if(gp.mapM.isInRoom(roomNum)) {
					if(chair1 != null) {
						gp.buildingM.addBuilding(chair1);
					}
					if(chair2 != null) {
						gp.buildingM.addBuilding(chair2);
					}
					if(chair3 != null) {
						gp.buildingM.addBuilding(chair3);
					}
					if(chair4 != null) {
						gp.buildingM.addBuilding(chair4);
					}
	    		} else {
	    			if(chair1 != null) {
	    				gp.mapM.getRoom(roomNum).addBuilding(chair1);
					}
					if(chair2 != null) {
						gp.mapM.getRoom(roomNum).addBuilding(chair2);
					}
					if(chair3 != null) {
						gp.mapM.getRoom(roomNum).addBuilding(chair3);
					}
					if(chair4 != null) {
						gp.mapM.getRoom(roomNum).addBuilding(chair4);
					}
	    		}
	    	}

		}
	}
}
