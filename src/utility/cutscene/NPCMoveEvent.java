package utility.cutscene;

import java.awt.geom.Rectangle2D;

import entity.buildings.Building;
import entity.npc.NPC;
import main.GamePanel;

public class NPCMoveEvent extends CutsceneEvent {

	GamePanel gp;
	
    private NPC npc;
    private Building targetBuilding;
    private String targetBuildingName;
    private int targetX, targetY;
    private Rectangle2D.Float targetHitbox;
    private int roomNum;

    public NPCMoveEvent(GamePanel gp, NPC npc, Building building, int roomNum) {
    	this.gp = gp;
        this.npc = npc;
        this.targetBuilding = building;
        this.roomNum = roomNum;
        targetBuildingName = targetBuilding.getName();

        npc.walking = true;
    }
    public NPCMoveEvent(GamePanel gp, NPC npc, String buildingName, int roomNum) {
    	this.gp = gp;
        this.npc = npc;
        this.roomNum = roomNum;
        this.targetBuildingName = buildingName;

        npc.walking = true;
    }
    public NPCMoveEvent(GamePanel gp, NPC npc, int x, int y) {
    	this.gp = gp;
        this.npc = npc;
        this.targetX = x;
        this.targetY = y;
        targetHitbox = new Rectangle2D.Float(x*48, y*48, 48, 48);

        npc.walking = true;
    }

    @Override
    public void update() {
        if (npc == null) {
            finished = true;
            return;
        }
        
        npc.walking = true;
        
        if(targetBuilding != null) {
        	if(npc.currentRoomNum != gp.player.currentRoomIndex) {
        		gp.mapM.changeRoom(npc.currentRoomNum, gp.buildingM.findDoor(npc.currentRoomNum));
        	}
	        if(npc.walkToBuildingInRoom(targetBuilding.getName(), roomNum)) {
	            finished = true;
	            npc.hitbox.x = targetBuilding.hitbox.x;
	            npc.hitbox.y = targetBuilding.hitbox.y;
	            npc.walking = false; // stop moving
	        };
        } else if(targetBuildingName != null) {
        	if(npc.currentRoomNum != gp.player.currentRoomIndex) {
        		gp.mapM.changeRoom(npc.currentRoomNum, gp.buildingM.findDoor(npc.currentRoomNum));
        	}
        	if(npc.walkToBuildingInRoom(targetBuildingName, roomNum)) {
	            finished = true;
	            npc.walking = false; // stop moving
	        };
        }
        
        if(targetX != 0) {
        	npc.walkToPoint(targetX, targetY);
            npc.walking = true; 
        	if(npc.hitbox.intersects(targetHitbox)) {
	            finished = true;
	            npc.walking = false; // stop moving
	        };
        }
    }
}
