package entity.buildings;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import entity.items.FryingPan;
import entity.items.Item;
import entity.items.SmallPan;
import main.GamePanel;
import main.renderer.Colour;
import main.renderer.Renderer;

public class BuildingManager {
	
	GamePanel gp;

    private Building[] buildings; //The array which stores all the buildings
    
    private int arrayCounter = 0;

    public BuildingManager(GamePanel gp) {
        this.gp = gp;
        buildings = new Building[250];
    }
    public int getArrayIndex() {
    	return arrayCounter;
    }
    public void setBuildings(Building[] buildings) {
    	this.buildings = buildings;
    	int i = 0;
    	for(Building b: this.buildings) {
    		if(b != null) {
    			b.setArrayCounter(i);
    		}
    		i++;
    	}
    }
    //Draw the item hit boxes
    public void drawHitboxes(Renderer renderer) {
        for(Building i: buildings) { //Loops through the items on the current map
            if(i != null) {
                i.drawHitbox(renderer);
            }
        }
        renderer.setColour(Colour.YELLOW);
        for(Building i: buildings) { //Loops through the items on the current map
            if(i != null) {
                i.drawBuildHitbox(renderer);
            }
        }
    }
    public void addBuilding(Building b) {
        buildings[arrayCounter] = b;
        b.setArrayCounter(arrayCounter);
        arrayCounter++;
    }
    public void addBuilding(Building b, int xPos, int yPos) {
        buildings[arrayCounter] = b;
        buildings[arrayCounter].hitbox.x = xPos;
        buildings[arrayCounter].hitbox.y = yPos;
        b.setArrayCounter(arrayCounter);
        arrayCounter++;
        b.onPlaced();
    }
    public void destroyBuilding(Building b) {
    	for(int i = 0; i < buildings.length; i++) {
    		if(buildings[i] != null) {
    			if(buildings[i] == b) {
    				buildings[i].destroy();
    				buildings[i] = null;
    				
    				if(gp.multiplayer) {
    					if(gp.serverHost) {
    						//Packet17DestroyBuilding packet = new Packet17DestroyBuilding((int)b.hitbox.x, (int)b.hitbox.y, (int)b.hitbox.width, (int)b.hitbox.height, b.getName());
    						//gp.socketServer.sendDataToOtherClients((PlayerMP)gp.player, packet.getData());
    					} else {
    						//Packet18RequestDestroyBuilding packet = new Packet18RequestDestroyBuilding(gp.player.getUsername(), (int)b.hitbox.x, (int)b.hitbox.y, (int)b.hitbox.width, (int)b.hitbox.height, b.getName());
    			            //packet.writeData(gp.socketClient);
    					}
    				}
    			}
    		}
    	}
    }
    public void setArrayCounter(int arrayCounter) {
    	this.arrayCounter = arrayCounter;
    }
    public Building[] getBuildings() {
    	return buildings;
    }
    public void removeAllWithName(String name) {
        for (int i = 0; i < buildings.length; i++) {
            Building b = buildings[i];
            if (b != null && b.getName().equals(name)) {

                // Clean up building state
                b.destroy();

                // Remove reference
                buildings[i] = null;
            }
        }
    }
    public void checkBuildingConnections() {
    	List<Building> shelves = gp.world.buildingM.findBuildingsWithName("Shelf");
	    for (Building b: shelves) {
	    	Shelf t = (Shelf)b;
	    	t.updateConnections();
	    }
    }
    public Building[] getBuildingsToDraw() {
    	Building[] b = new Building[buildings.length];
    	for(int i = 0; i < buildings.length; i++) {
    		if(buildings[i] != null) {
    			if(!buildings[i].isSecondLayer && !buildings[i].isThirdLayer && !buildings[i].isFourthLayer && !buildings[i].isBottomLayer && !buildings[i].isFifthLayer){
    				b[i] = buildings[i];
    			}
    		}
    	}
    	return b;
    }
    public Item[] getBuildingItems() {
    	Item[] b = new Item[buildings.length];
    	for(int i = 0; i < buildings.length; i++) {
    		if(buildings[i] != null) {
    			if(buildings[i] instanceof FloorDecor_Building build){
    				if(build.currentItem != null) {
    					b[i] = build.currentItem;
    				}
    			}
    		}
    	}
    	return b;
    }
    public Building[] getSecondLayer() {
    	Building[] b = new Building[buildings.length];
    	for(int i = 0; i < buildings.length; i++) {
    		if(buildings[i] != null) {
    			if(buildings[i].isSecondLayer){
    				b[i] = buildings[i];
    			}
    		}
    	}
    	return b;
    }
    public Building[] getThirdLayer() {
    	Building[] b = new Building[buildings.length];
    	for(int i = 0; i < buildings.length; i++) {
    		if(buildings[i] != null) {
    			if(buildings[i].isThirdLayer){
    				b[i] = buildings[i];
    			}
    		}
    	}
    	return b;
    }
    public Building[] getFourthLayer() {
    	Building[] b = new Building[buildings.length];
    	for(int i = 0; i < buildings.length; i++) {
    		if(buildings[i] != null) {
    			if(buildings[i].isFourthLayer){
    				b[i] = buildings[i];
    			}
    		}
    	}
    	return b;
    }
    public Building[] getFifthLayer() {
    	Building[] b = new Building[buildings.length];
    	for(int i = 0; i < buildings.length; i++) {
    		if(buildings[i] != null) {
    			if(buildings[i].isFifthLayer){
    				b[i] = buildings[i];
    			}
    		}
    	}
    	return b;
    }
    public Building[] getBottomLayer() {
    	Building[] b = new Building[buildings.length];
    	for(int i = 0; i < buildings.length; i++) {
    		if(buildings[i] != null) {
    			if(buildings[i].isBottomLayer){
    				b[i] = buildings[i];
    			}
    		}
    	}
    	return b;
    }
    public void outputMap() {
    	for(int i = 0; i < buildings.length; i++) {
    		if(buildings[i] != null) {
    			buildings[i].printOutput();
    		}
    	}
    	System.out.println();
    }
	public boolean entityCheck(float x, float y, float w, float h) {
		Rectangle2D.Float hitbox = new Rectangle2D.Float(x, y, w, h);
		for(Building b: buildings) {
			if(b != null) {
				if(b.isSolid) {
					if(b.hitbox.intersects(hitbox)) {
						return false;
					}	
				}
			}
		}
		return true;
	}
	
	public Building findBuilding(float x, float y, float w, float h) {
		Rectangle2D.Float hitbox = new Rectangle2D.Float(x, y, w, h);
		for(Building b: buildings) {
			if(b != null) {
					if(b.hitbox.intersects(hitbox)) {
						return b;
					}	
			}
		}
		return null;
	}
	public Toilet findFreeToilet() {
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals("Toilet 1")) {
					Toilet toilet = (Toilet)b;
					if(toilet.available) {
						toilet.available = false;
						return toilet;
					}
				}	
			}
		}
		return null;
	}
	public TablePlate findDirtyPlate() {
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals("Table Plate")) {
					TablePlate table = (TablePlate)b;
					if(table.showDirtyPlate && table.plate != null) {
						return table;
					}
				}	
			}
		}
		return null;
	}
	public Stove refreshStove() {
		if(gp.player.currentItem != null) {
			if(gp.player.currentItem instanceof FryingPan f) {
				f.refreshImages();
			} else if(gp.player.currentItem instanceof SmallPan f) {
				f.refreshImages();
			}
		}
		
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals("Stove")) {
					Stove table = (Stove)b;
					if(table.leftSlot != null) {
						table.leftSlot.refreshImages();
					}
					if(table.rightSlot != null) {
						table.rightSlot.refreshImages();
					}
				} else if(b.getName().equals("Table Piece")) {
					FloorDecor_Building t = (FloorDecor_Building)b;
					if(t.currentItem instanceof FryingPan f) {
						f.refreshImages();
					} else if(t.currentItem instanceof SmallPan f) {
						f.refreshImages();
					}
				}
			}
		}
		return null;
	}
	public Chair isFreeChair() {
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals("Chair 1")) {
					Chair chair = (Chair)b;
					if(chair.available && !chair.tablePlate.showDirtyPlate) {
						return chair;
					}
				}	
			}
		}
		return null;
	}
	public Chair isFreeSingleChair() {
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals("Chair 1")) {
					Chair chair = (Chair)b;
					if(chair.available && !chair.tablePlate.showDirtyPlate && !chair.groupChair) {
						return chair;
					}
				}	
			}
		}
		return null;
	}
	public Chair isGroupTable() {

	    for (Building b : buildings) {
	        if (b != null && b.getName().equals("Chair 1")) {

	            Chair chair = (Chair) b;

	            // Must be a group chair
	            if (!chair.groupChair) continue;

	            boolean tableFree = true;

	            // Check ALL chairs on this table
	            for (Chair c : chair.table.getChairs()) {

	                // If any chair is occupied OR has a dirty plate, the table isn't free
	                if (!c.available || c.tablePlate.showDirtyPlate) {
	                    tableFree = false;
	                    break;
	                }
	            }

	            // Only return the chair if the whole table is free & clean
	            if (tableFree) {
	                return chair;
	            }
	        }
	    }

	    return null;
	}
	public Chair takeGroupChair() {
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals("Chair 1")) {
					Chair chair = (Chair)b;
					if(chair.available && !chair.tablePlate.showDirtyPlate && chair.groupChair) {
						chair.available = false;
						return chair;
					}
				}	
			}
		}
		return null;
	}
	public Trapdoor findTrapdoor() {
	for(Building b: buildings) {
		if(b != null) {
			if(b.getName().equals("Trapdoor 1")) {
				Trapdoor door = (Trapdoor)b;
				return door;
			}	
		}
	}
	return null;
}
	public Chair findFreeChair() {
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals("Chair 1")) {
					Chair chair = (Chair)b;
					if(chair.available && !chair.tablePlate.showDirtyPlate && !chair.groupChair) {
						chair.available = false;
						return chair;
					}
				}	
			}
		}
		return null;
	}
	public Door findDoor(int roomNum) {
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals("Door 1")) {
					Door door = (Door)b;
					if(door.doorRoomNum == roomNum) {
						return door;
					}
				}	
			}
		}
		return null;
	}
	public FloorDecor_Building findTable(float x, float y, float w, float h) {
		Rectangle2D.Float hitbox = new Rectangle2D.Float(x, y, w, h);
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals("Table Piece")) {
					FloorDecor_Building table = (FloorDecor_Building)b;
					if(table.interactHitbox != null) {
						if(table.interactHitbox.intersects(hitbox)) {
							return table;
						}	
					}
				}
			}
		}
		return null;
	}
	
	public Building findBuildingWithName(String name) {
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals(name)) {
					return b;
				}
			}
		}
		return null;
	}
	public List<Building> findBuildingsWithName(String name) {
	    List<Building> result = new ArrayList<>();
	    for (Building b : buildings) {
	        if (b != null && b.getName().equals(name)) {
	            result.add(b);
	        }
	    }
	    return result;
	}
	public void setDoorCooldowns() {
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals("Door 1")) {
					Door door = (Door)b;
					door.doorCooldown = 0.3;
				} else if(b.getName().equals("Trapdoor 1")) {
					Trapdoor door = (Trapdoor)b;
					door.cooldown = 0.3;
				}
			}
		}
	}
	public Building findCorrectDoor(int facing, int doorRoomNum) {
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals("Door 1")) {
					Door door = (Door)b;
					if(doorRoomNum == door.doorRoomNum) {
						switch(facing) {
						case 0:
							if(door.facing == 1) {
								return b;
							}
							break;
						case 1:
							if(door.facing == 0) {
								return b;
							}
							break;
						case 2:
							if(door.facing == 3) {
								return b;
							}
							break;
						case 3:
							if(door.facing == 2) {
								return b;
							}
							break;
						}
					}
				}
			}
		}
		return null;
	}
	public void refreshBuildings() {
		for(Building b: buildings) {
			if(b != null) {
				b.refreshImages();
			}
		}
	}
	public boolean intersectsKitchenBuilding(GamePanel gp, Building build, Rectangle2D.Float interactHitbox) {
	    for (Building b : buildings) {
	        if (b == null || b == build) continue;

	        // Skip benign types like "Table Piece"
	        if (b.getName().equals("Table Piece")) continue;

	        // If it intersects and it's a restricted interaction
	        if (b.hitbox.intersects(interactHitbox)) {
	            /*
	        	String name = b.getName();
	            if (name.equals("Chopping Board 1") ||
	                name.equals("Kitchen Sink 1") ||
	                name.equals("Barrel") ||
	                name.equals("Bin 1") ||
	                name.equals("Small Cup")) {
	                return false;
	            }
	            */
	        	return false;
	        }
	    }
	    return true;
	}
	public void setBuildingItem(int arrayCounter, Item i) {
		if(buildings[arrayCounter].getName().equals("Table Piece")) {
			FloorDecor_Building building = (FloorDecor_Building)buildings[arrayCounter];
			building.currentItem = i; 
		} else if(buildings[arrayCounter].getName().equals("Table Corner")){
			CornerTable building = (CornerTable)buildings[arrayCounter];
			if(building.interactHitbox1.intersects(i.hitbox)) {
				building.slot1 = i;
			} else {
				building.slot2 = i;
			}
		}
	}
	public Item getBuildingItem(int arrayCounter) {
		if(buildings[arrayCounter].getName().equals("Table Piece")) {
			FloorDecor_Building building = (FloorDecor_Building)buildings[arrayCounter];
			return building.currentItem; 
		}
		return null;
	}
	public void checkBuildingDestruction(int x, int y) {
		for(Building b: buildings) {
			if(b != null) {
				if(b.hitbox.contains(x, y)) {
					b.openDestructionUI();
				} 
			}
		}
	}
	public void addSpill(int a) {
		if(gp.world.progressM.currentPhase == 0) {
			buildings[arrayCounter] = new Spill(gp, 15*gp.tileSize - 24, 8*gp.tileSize);
			arrayCounter++;
		} else if(gp.world.progressM.currentPhase == 1) {
			if(a == 0) {
				buildings[arrayCounter] = new Spill(gp, 15*gp.tileSize - 24, 8*gp.tileSize);
				arrayCounter++;
			} else if(a == 1) {
				buildings[arrayCounter] = new Spill(gp, 9*gp.tileSize - 24, 9*gp.tileSize);
				arrayCounter++;
			}
		}
	}
	public void addLeak(int a) {
		if(a == 0) {
			buildings[arrayCounter] = new Leak(gp, 9*gp.tileSize, 10*gp.tileSize + 24);
			arrayCounter++;
		} else if(a == 1) {
			buildings[arrayCounter] = new Leak(gp, 13*gp.tileSize, 10*gp.tileSize + 24);
			arrayCounter++;
		}
	}
	public boolean hasBuildingWithName(String name) {
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals(name)) {
					return true;
				}
			}
		}
		return false;
	}
	public void removeBuilding(Building building) {
		int counter = 0;
		for(Building b: buildings) {
			if(b != null) {
				if(building.equals(b)) {
					buildings[counter] = null;
				}
			}
			counter++;
		}
	}
	public Building getBuilding(int index) {
		return buildings[index];
	}
    public void updateState(double dt) {
    	for(Building i: buildings) { //Loops through the items on the current map
            if(i != null) {
            	i.updateState(dt); //Draws the item
            }
        }
    }
    public void inputUpdate(double dt) {
    	for(Building i: buildings) { //Loops through the items on the current map
            if(i != null) {
            	i.inputUpdate(dt); //Draws the item
            }
        }
    }
    public void draw(Renderer renderer) {
    	
        for(Building i: buildings) { //Loops through the items on the current map
            if(i != null) {
            	i.draw(renderer); //Draws the item
            }
        }

    }


}
