package entity.npc;

import java.awt.geom.Rectangle2D;

import main.GamePanel;
import utility.RoomHelperMethods;

public class GroupCustomer extends Customer {
	
	public GroupCustomer(GamePanel gp, float xPos, float yPos) {
		super(gp, xPos, yPos);
	}
	
	protected void findTable() {
		if(gp.mapM.currentRoom.equals(gp.mapM.getRoom(currentRoomNum))) {
			currentChair = gp.buildingM.takeGroupChair();
		} else {
			currentChair = gp.mapM.getRoom(currentRoomNum).takeGroupChair();
		}
		if(currentChair != null) {
			currentChair.setCustomer(this);
			walking = true;
		}
    }
	
}
