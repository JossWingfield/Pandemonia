package entity.buildings;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import entity.items.Item;
import main.GamePanel;

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
    public void drawHitboxes(Graphics2D g, float xDiff, float yDiff) {
        for(Building i: buildings) { //Loops through the items on the current map
            if(i != null) {
                i.drawHitbox(g, xDiff, yDiff);
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
	public void setDoorCooldowns() {
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals("Door 1")) {
					Door door = (Door)b;
					door.cooldown = 10;
				}
			}
		}
	}
	public Building findCorrectDoor(int facing) {
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals("Door 1")) {
					Door door = (Door)b;
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
		return null;
	}
	public Chair findChair(float x, float y, float w, float h) {
		Rectangle2D.Float hitbox = new Rectangle2D.Float(x, y, w, h);
		for(Building b: buildings) {
			if(b != null) {
				if(b.hitbox.intersects(hitbox)) {
					if(b.getName().equals("Chair 1")) {
						return (Chair)b;
					}
				}	
			}
		}
		return null;
	}
	public CornerTable findCornerTable(float x, float y, float w, float h) {
		Rectangle2D.Float hitbox = new Rectangle2D.Float(x, y, w, h);
		for(Building b: buildings) {
			if(b != null) {
					if(b.getName().equals("Table Corner 1")) {
						CornerTable table = (CornerTable)b;
						if(table.interactHitbox1.intersects(hitbox) || table.interactHitbox2.intersects(hitbox)) {
							return table;
						}
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
					if(chair.available) {
						chair.available = false;
						return chair;
					}
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
	public Door findToiletDoor() {
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals("Door 1")) {
					Door door = (Door)b;
					if(door.roomNum == 4) {
						return door;
					}
				}	
			}
		}
		return null;
	}
	public Door findExitDoor() {
		for(Building b: buildings) {
			if(b != null) {
				if(b.getName().equals("Door 1")) {
					Door door = (Door)b;
					if(door.roomNum == 0) {
						return door;
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
					if(chair.available) {
						return chair;
					}
				}	
			}
		}
		return null;
	}
	public boolean intersectsKitchenBuilding(GamePanel gp, Building build, Rectangle2D.Float interactHitbox) {
	    for (Building b : buildings) {
	        if (b == null || b == build) continue;

	        // Skip benign types like "Table Piece"
	        if (b.getName().equals("Table Piece")) continue;

	        // If it intersects and it's a restricted interaction
	        if (b.hitbox.intersects(interactHitbox)) {
	            String name = b.getName();
	            if (name.equals("Chopping Board 1") ||
	                name.equals("Kitchen Sink 1") ||
	                name.equals("Barrel") ||
	                name.equals("Bin 1") ||
	                name.equals("Small Cup")) {
	                return false;
	            }
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
	public void setCornerSlotItem(int arrayCounter, int slot, Item i) {
		if(buildings[arrayCounter].getName().equals("Table Corner 1")) {
			CornerTable building = (CornerTable)buildings[arrayCounter];
			if(slot == 1) {
				building.slot1 = i;
			} else {
				building.slot2 = i;
			}
		}
	}
	public Item getCornerTableItem(int arrayCounter, int slot) {
		if(buildings[arrayCounter].getName().equals("Table Corner 1")) {
			CornerTable building = (CornerTable)buildings[arrayCounter];
			if(slot == 1) {
				return building.slot1;
			} else {
				return building.slot2;
			}
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
		if(a == 0) {
			buildings[arrayCounter] = new Spill(gp, 15*gp.tileSize - 24, 8*gp.tileSize);
			arrayCounter++;
		} else if(a == 1) {
			buildings[arrayCounter] = new Spill(gp, 9*gp.tileSize - 24, 9*gp.tileSize);
			arrayCounter++;
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
    public void update() {
    	for(Building i: buildings) { //Loops through the items on the current map
            if(i != null) {
            	i.update(); //Draws the item
            }
        }
    }
    public void draw(Graphics2D g) {
    	
        for(Building i: buildings) { //Loops through the items on the current map
            if(i != null) {
            	i.draw(g); //Draws the item
            }
        }

    }


}
