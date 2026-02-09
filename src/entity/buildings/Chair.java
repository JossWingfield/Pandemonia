package entity.buildings;

import java.awt.geom.Rectangle2D;

import entity.npc.Customer;
import main.GamePanel;
import main.renderer.Renderer;
import main.renderer.TextureRegion;

public class Chair extends Building {
	
	public boolean available = true;
	public Customer currentCustomer;
	public int facing;
	public TablePlate tablePlate = null;
	private boolean firstUpdate = true;
	private int roomNum = 0;
	public boolean groupChair = false;
	public LargeTable table;
	
	public Chair(GamePanel gp, float xPos, float yPos, int direction) {
		super(gp, xPos, yPos, 48, 48);
		this.facing = direction;
		
		isSolid = true;
		
		drawWidth = 16*3;
		drawHeight = 16*3;
		importImages();
		isSolid = false;
		canBePlaced = false;
		isBottomLayer = true;
		isKitchenBuilding = true;
		mustBePlacedOnFloor = true;
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*4, hitbox.y+3*4, hitbox.width-3*7, hitbox.height-3*6);
	}
	
	public void setGroupChair(LargeTable table) {
		this.table = table;
		this.groupChair = true;
	}
	public void onPlaced() {
		buildHitbox = new Rectangle2D.Float(hitbox.x+3*4, hitbox.y+3*4, hitbox.width-3*7, hitbox.height-3*6);
	}
	public Building clone() {
		Chair building = new Chair(gp, hitbox.x, hitbox.y, facing);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Chair(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	private void importImages() {
		animations = new TextureRegion[1][1][1];
		
		name = "Chair 1";
    	animations[0][0][0] = importImage("/decor/chair.png").getSubimage(2, 73, 16, 16);
	}
	public void setCustomer(Customer customer) {
		currentCustomer = customer;
	}
	public void refreshImages() {
		animations[0][0][0] = gp.world.mapM.getRooms()[roomNum].getChairSkin().getImage();
	}
	public void draw(Renderer renderer) {
		if(firstUpdate) {
			tablePlate = new TablePlate(gp, hitbox.x, hitbox.y, facing, this);
			gp.world.buildingM.addBuilding(tablePlate);
			firstUpdate = false;
		}
		 
	    renderer.draw(animations[0][0][0], (int)(hitbox.x - xDrawOffset ), (int) (hitbox.y )-yDrawOffset, drawWidth, drawHeight);

	     
		if(destructionUIOpen) {
		    renderer.draw(destructionImage, (int)(hitbox.x - xDrawOffset ), (int) (hitbox.y )-yDrawOffset, gp.tileSize, gp.tileSize);
		}
	    
	}
}
