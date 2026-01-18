package entity.buildings;

import java.awt.geom.Rectangle2D;

import main.GamePanel;
import main.renderer.TextureRegion;
import utility.RoomHelperMethods;

public class Table extends Building {
	
	public boolean doubleChaired = false;
	private Chair chair1;
	private Chair chair2;
	public String chairFacing;
	
	private boolean firstUpdate = true;
	
	public Table(GamePanel gp, float xPos, float yPos, String chairFacing, boolean doubleChaired) {
		super(gp, xPos, yPos, 48, 48*2);
		this.doubleChaired = doubleChaired;
		this.chairFacing = chairFacing;
		
		isSolid = true;
		
		importImages();
		isSolid = true;
		isSecondLayer = true;
		isKitchenBuilding = true;
		mustBePlacedOnFloor = true;
		switch(chairFacing) {
    	case "Left":
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*3, hitbox.width-3*3, hitbox.height-3*4);
    		break;
    	case "Right":
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*3, hitbox.width-3*3, hitbox.height-3*4);
    		break;
    	case "Up":
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*3, hitbox.width-3*3, hitbox.height-3*2);
    		break;
       	case "Down":
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*3, hitbox.width-3*3, hitbox.height-3*2);
    		break;
    	}	
	}
	public void onPlaced() {
		switch(chairFacing) {
    	case "Left":
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*3, hitbox.width-3*3, hitbox.height-3*4);
    		break;
    	case "Right":
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*3, hitbox.width-3*3, hitbox.height-3*4);
    		break;
    	case "Up":
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*3, hitbox.width-3*3, hitbox.height-3*2);
    		break;
       	case "Down":
    		buildHitbox = new Rectangle2D.Float(hitbox.x+3*1, hitbox.y+3*3, hitbox.width-3*3, hitbox.height-3*2);
    		break;
    	}
	}
	public Building clone() {
		Table building = new Table(gp, hitbox.x, hitbox.y, this.chairFacing, this.doubleChaired);
		return building;
    }
	public void printOutput() {
		System.out.println("buildings[arrayCounter] = new Table(gp, " + (int)hitbox.x + ", " + (int)hitbox.y + ");");
		System.out.println("arrayCounter++;");	
	}
	public void refreshImages() {
		animations[0][0][0] = gp.mapM.getRooms()[roomNum].getTableSkin().getImage1();
	}
	private void importImages() {
		animations = new TextureRegion[1][1][1];
		
		name = "Table 1";
    	
    	switch(chairFacing) {
    	case "Left":
        	animations[0][0][0] = importImage("/decor/table.png").getSubimage(48, 24, 16, 40);
    		drawWidth = 16*3;
    		drawHeight = 40*3;
    		if(!doubleChaired) {
    			chair1 = new Chair(gp, hitbox.x + hitbox.width -8, hitbox.y + hitbox.height/2 - 24, 1);
    		} else {
    			chair1 = new Chair(gp, hitbox.x + hitbox.width -8, hitbox.y + hitbox.height/2 - 24 - 24, 1);
    			chair2 = new Chair(gp, hitbox.x + hitbox.width -8, hitbox.y + hitbox.height/2 - 24 + 24, 1);
    		}
    		break;
    	case "Right":
        	animations[0][0][0] = importImage("/decor/table.png").getSubimage(48, 24, 16, 40);
    		drawWidth = 16*3;
    		drawHeight = 40*3;
    		if(!doubleChaired) {
    			chair1 = new Chair(gp, hitbox.x -48+8, hitbox.y + hitbox.height/2 - 24, 2);
    		} else {
    			chair1 = new Chair(gp, hitbox.x -48+8, hitbox.y + hitbox.height/2 - 24 - 24, 2);
    			chair2 = new Chair(gp, hitbox.x -48+8, hitbox.y + hitbox.height/2 - 24 + 24, 2);
    		}
    		break;
    	case "Up":
    		hitbox.width = 48*2;
    		hitbox.height = 48;
        	animations[0][0][0] = importImage("/decor/table.png").getSubimage(32, 0, 32, 24);
        	drawWidth = 32*3;
    		drawHeight = 24*3;
    		if(!doubleChaired) {
    			chair1 = new Chair(gp, hitbox.x + hitbox.width/2 - 24, hitbox.y + hitbox.height/2 + 16-8, 3);
    		} else {
    			chair1 = new Chair(gp, hitbox.x + hitbox.width/2 - 24 - 24, hitbox.y + hitbox.height/2 + 16-8, 3);
    			chair2 = new Chair(gp, hitbox.x + hitbox.width/2 - 24 + 24, hitbox.y + hitbox.height/2 + 16-8, 3);

    		}
    		break;
       	case "Down":
    		hitbox.width = 48*2;
    		hitbox.height = 48;
        	animations[0][0][0] = importImage("/decor/table.png").getSubimage(32, 0, 32, 24);
        	drawWidth = 32*3;
    		drawHeight = 24*3;
    		if(!doubleChaired) {
    			chair1 = new Chair(gp, hitbox.x + hitbox.width/2 - 24, hitbox.y -48+8, 3);
    		} else {
    			chair1 = new Chair(gp, hitbox.x + hitbox.width/2 - 24-24, hitbox.y -48+8, 3);
    			chair2 = new Chair(gp, hitbox.x + hitbox.width/2 - 24+24, hitbox.y -48+8, 3);
    		}
    		break;
    	}
    	
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
	    		} else {
	    			if(chair1 != null) {
						gp.mapM.getRoom(roomNum).addBuilding(chair1);
					}
					if(chair2 != null) {
						gp.mapM.getRoom(roomNum).addBuilding(chair2);
					}
	    		}
	    	}

		}
	}
}
