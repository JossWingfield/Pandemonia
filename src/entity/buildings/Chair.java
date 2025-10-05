package entity.buildings;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entity.npc.Customer;
import main.GamePanel;

public class Chair extends Building {
	
	public boolean available = true;
	public Customer currentCustomer;
	public int facing;
	public TablePlate tablePlate = null;
	private boolean firstUpdate = true;
	private int roomNum = 0;
	
	public Chair(GamePanel gp, float xPos, float yPos, int direction) {
		super(gp, xPos, yPos, 48, 48);
		this.facing = direction;
		
		isSolid = true;
		blueprint = false;
		drawWidth = 16*3;
		drawHeight = 16*3;
		importImages();
		isSolid = false;
		isBottomLayer = true;
		isKitchenBuilding = true;
		mustBePlacedOnFloor = true;
	}
	public Building clone() {
		Chair building = new Chair(gp, hitbox.x, hitbox.y, facing);
		return building;
    }
	public void printOutput() {
		//System.out.println("buildings[arrayCounter] = new Chair(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		//System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new BufferedImage[1][1][1];
		
		name = "Chair 1";
    	animations[0][0][0] = importImage("/decor/chair.png").getSubimage(2, 73, 16, 16);
	}
	public void setCustomer(Customer customer) {
		currentCustomer = customer;
	}
	public void refreshImages() {
		animations[0][0][0] = gp.mapM.getRooms()[roomNum].getChairSkin().getImage();
	}
	public void draw(Graphics2D g2) {
		if(firstUpdate) {
			tablePlate = new TablePlate(gp, hitbox.x, hitbox.y, facing, this);
			gp.buildingM.addBuilding(tablePlate);
			firstUpdate = false;
		}
		 
	     g2.drawImage(animations[0][0][0], (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, drawWidth, drawHeight, null);

	     
		if(destructionUIOpen) {
		    g2.drawImage(destructionImage, (int) hitbox.x - xDrawOffset - gp.player.xDiff, (int) (hitbox.y - gp.player.yDiff)-yDrawOffset, gp.tileSize, gp.tileSize, null);
		}
	    
	}
}
